/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.rocksdb;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import org.rocksdb.*;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.rocksdb.RocksDB.DEFAULT_COLUMN_FAMILY;

/**
 * Basic RocksDB implementation of a thread-safe DictionaryLabelsStore.
 * Doesn't have a canonicising method or anything clever to figure out Ids, just bumps up a number
 */
public class RocksDbLabelsStore implements DictionaryLabelsStore, Closeable {

    // Map labels to IDs
    private static final byte[] LABELS_TO_IDS_CF = "labels_to_ids".getBytes(StandardCharsets.UTF_8);
    // Map IDs to labels
    private static final byte[] IDS_TO_LABELS_CF = "ids_to_labels".getBytes(StandardCharsets.UTF_8);

    private static final byte[] ID_COUNTER_CF = "id_counter".getBytes(StandardCharsets.UTF_8);
    private static final byte[] ID_COUNTER_KEY = "next_id".getBytes(StandardCharsets.UTF_8);

    private final RocksDB db;
    private final Options options;
    private final Map<String, ColumnFamilyHandle> columnFamilyHandles;
    private final AtomicLong nextId;
    private final Object lock = new Object();

    private final AtomicBoolean closed = new AtomicBoolean(false);

    private void ensureOpen() {
        if (closed.get()) {
            throw new IllegalStateException("RocksDbLabelsStore has been closed");
        }
    }

    private static byte[] longToBytes(long x) {
        return ByteBuffer.allocate(Long.BYTES).putLong(x).array();
    }

    private static long bytesToLong(byte[] bytes) {
        if (bytes == null || bytes.length != Long.BYTES) {
            throw new IllegalArgumentException("Byte array must be " + Long.BYTES + " bytes long to represent a long.");
        }
        return ByteBuffer.wrap(bytes).getLong();
    }


    public RocksDbLabelsStore(String dbPath) throws IOException, RocksDBException {
        // Load the native library
        RocksDB.loadLibrary();

        // We start with a list that includes the default column family and our three custom CFs.
        ColumnFamilyOptions cfOptions = new ColumnFamilyOptions();
        ColumnFamilyDescriptor defaultCFD = new ColumnFamilyDescriptor(DEFAULT_COLUMN_FAMILY, cfOptions);
        ColumnFamilyDescriptor labelsToIdsCFD = new ColumnFamilyDescriptor(LABELS_TO_IDS_CF, cfOptions);
        ColumnFamilyDescriptor idsToLabelsCFD = new ColumnFamilyDescriptor(IDS_TO_LABELS_CF, cfOptions);
        ColumnFamilyDescriptor idCounterCFD = new ColumnFamilyDescriptor(ID_COUNTER_CF, cfOptions);

        List<ColumnFamilyDescriptor> cfDescriptors = List.of(defaultCFD, labelsToIdsCFD, idsToLabelsCFD, idCounterCFD);
        List<ColumnFamilyHandle> cfHandles = new ArrayList<>();

        // 2. Open RocksDB with Options
        options = new Options().setCreateIfMissing(true).setCreateMissingColumnFamilies(true);
        File dbDir = new File(dbPath);
        Files.createDirectories(dbDir.toPath());

        DBOptions dbOptions = new DBOptions(options);

        try {
            db = RocksDB.open(dbOptions, dbDir.getAbsolutePath(), cfDescriptors, cfHandles);
        } catch (RocksDBException e) {
            cfDescriptors.forEach(cf -> cf.getOptions().close());
            throw e;
        }

        columnFamilyHandles = new HashMap<>();
        for (int i = 0; i < cfDescriptors.size(); i++) {
            String name = new String(cfDescriptors.get(i).getName(), StandardCharsets.UTF_8);
            columnFamilyHandles.put(name, cfHandles.get(i));
        }
        this.nextId = initializeNextId();
    }

    private ColumnFamilyHandle getHandle(byte[] cfNameBytes) {
        String cfName = new String(cfNameBytes, StandardCharsets.UTF_8);
        return columnFamilyHandles.get(cfName);
    }


    private AtomicLong initializeNextId() throws RocksDBException {
        ColumnFamilyHandle counterHandle = getHandle(ID_COUNTER_CF);
        byte[] storedIdBytes = db.get(counterHandle, ID_COUNTER_KEY);
        long initialId = 1L;
        if (storedIdBytes != null) {
            initialId = bytesToLong(storedIdBytes);
        } else {
            db.put(counterHandle, ID_COUNTER_KEY, longToBytes(initialId));
        }
        return new AtomicLong(initialId);
    }

    @Override
    public long idForLabel(byte[] labelBytes) {
        if (labelBytes == null) throw new IllegalArgumentException("Label cannot be null.");

        ColumnFamilyHandle labelToIdHandle = getHandle(LABELS_TO_IDS_CF);

        synchronized (lock) {
            ensureOpen();
            try {
                byte[] idBytes = db.get(labelToIdHandle, labelBytes);
                if (idBytes != null) {
                    return bytesToLong(idBytes);
                }

                long newId = nextId.getAndIncrement();
                byte[] newIdBytes = longToBytes(newId);

                ColumnFamilyHandle idToLabelHandle = getHandle(IDS_TO_LABELS_CF);
                ColumnFamilyHandle counterHandle = getHandle(ID_COUNTER_CF);

                try (WriteBatch batch = new WriteBatch();
                     WriteOptions writeOptions = new WriteOptions()) {

                    batch.put(labelToIdHandle, labelBytes, newIdBytes);
                    batch.put(idToLabelHandle, newIdBytes, labelBytes);
                    batch.put(counterHandle, ID_COUNTER_KEY, longToBytes(nextId.get()));

                    db.write(writeOptions, batch);
                }
                return newId;

            } catch (RocksDBException e) {
                throw new RuntimeException("Error accessing RocksDB", e);
            }
        }
    }

    @Override
    public Map<byte[], Long> idsForLabels(List<byte[]> labels) {
        if (labels == null || labels.isEmpty()) {
            return Map.of();
        }

        synchronized (lock) {
            ensureOpen();

            ColumnFamilyHandle labelToIdHandle = getHandle(LABELS_TO_IDS_CF);
            ColumnFamilyHandle idToLabelHandle = getHandle(IDS_TO_LABELS_CF);
            ColumnFamilyHandle counterHandle = getHandle(ID_COUNTER_CF);

            List<byte[]> queryLabels = new ArrayList<>();
            List<ColumnFamilyHandle> cfHandles = new ArrayList<>();

            for (byte[] label : labels) {
                if (label != null) {
                    queryLabels.add(label);
                    cfHandles.add(labelToIdHandle);
                }
            }

            if (queryLabels.isEmpty()) {
                return Map.of();
            }

            // 1) Bulk lookup existing IDs
            List<byte[]> existing;
            try (ReadOptions readOptions = new ReadOptions()) {
                existing = db.multiGetAsList(readOptions, cfHandles, queryLabels);
            } catch (RocksDBException e) {
                throw new RuntimeException("Error executing MultiGet on RocksDB for labels", e);
            }

            Map<byte[], Long> result = new HashMap<>();
            List<byte[]> newLabels = new ArrayList<>();

            for (int i = 0; i < queryLabels.size(); i++) {
                byte[] idBytes = existing.get(i);
                byte[] label = queryLabels.get(i);

                if (idBytes != null) {
                    // already had an id
                    result.put(label, bytesToLong(idBytes));
                } else {
                    // needs a new id
                    newLabels.add(label);
                }
            }

            if (!newLabels.isEmpty()) {
                try (WriteBatch batch = new WriteBatch();
                     WriteOptions writeOptions = new WriteOptions()) {

                    for (byte[] label : newLabels) {
                        long newId = nextId.getAndIncrement();
                        byte[] newIdBytes = longToBytes(newId);

                        result.put(label, newId);

                        batch.put(labelToIdHandle, label, newIdBytes);
                        batch.put(idToLabelHandle, newIdBytes, label);
                    }

                    // persist updated counter
                    batch.put(counterHandle, ID_COUNTER_KEY, longToBytes(nextId.get()));

                    db.write(writeOptions, batch);
                } catch (RocksDBException e) {
                    throw new RuntimeException("Error writing to RocksDB in bulk idsForLabels", e);
                }
            }

            return result;
        }
    }

    @Override
    public byte[] labelForId(long id) {
        ensureOpen();

        if (id <= 0) return null;

        byte[] idBytes = longToBytes(id);
        ColumnFamilyHandle idToLabelHandle = getHandle(IDS_TO_LABELS_CF);
        try {
            byte[] result = db.get(idToLabelHandle, idBytes);
            return result;
        } catch (RocksDBException e) {
            throw new RuntimeException("Error accessing RocksDB", e);
        }
    }

    @Override
    public Map<Long, byte[]> labelsForIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }

        ensureOpen();

        List<Long> validIds = new ArrayList<>();
        List<byte[]> keys = new ArrayList<>();

        ColumnFamilyHandle idToLabelHandle = getHandle(IDS_TO_LABELS_CF);
        List<ColumnFamilyHandle> cfHandles = new ArrayList<>();

        for (Long id : ids) {
            if (id != null && id > 0) {
                validIds.add(id);
                keys.add(longToBytes(id));
                cfHandles.add(idToLabelHandle);
            }
        }

        if (keys.isEmpty()) {
            return Map.of();
        }

        List<byte[]> results;
        try (ReadOptions readOptions = new ReadOptions()) {
            results = db.multiGetAsList(readOptions, cfHandles, keys);
        } catch (RocksDBException e) {
            throw new RuntimeException("Error executing MultiGet on RocksDB", e);
        }

        Map<Long, byte[]> labels = new HashMap<>();
        for (int i = 0; i < validIds.size(); i++) {
            byte[] labelBytes = results.get(i);
            if (labelBytes != null) {
                labels.put(validIds.get(i), labelBytes);
            }
        }
        return labels;
    }

    @Override
    public void close() {
        try {
            if (!closed.compareAndSet(false, true)) {
                return; // already closed
            }
            for (final ColumnFamilyHandle cfHandle : columnFamilyHandles.values()) {
                cfHandle.close();
            }
            db.close();
            options.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to close RocksDB resources", e);
        }
    }
}
