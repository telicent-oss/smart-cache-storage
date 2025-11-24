/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.lmdb;
import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import org.lmdbjava.*;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.lmdbjava.ByteArrayProxy.PROXY_BA;
import static org.lmdbjava.DbiFlags.MDB_CREATE;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * LMDB-based implementation of DictionaryLabelsStore.
 * Uses three named databases:
 *  - labels_to_ids  : label bytes -> long id
 *  - ids_to_labels  : long id -> label bytes
 *  - meta           : metadata (currently just next_id)
 */
public class LMDBLabelsStore implements DictionaryLabelsStore, Closeable {

    private static final byte[] ID_COUNTER_KEY = "next_id".getBytes(UTF_8);

    private final Env<byte[]> env;
    private final Dbi<byte[]> labelsToIdsDb;
    private final Dbi<byte[]> idsToLabelsDb;
    private final Dbi<byte[]> metaDb;

    private final AtomicLong nextId = new AtomicLong();
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final Object lock = new Object();

    public LMDBLabelsStore(String path) throws IOException {
        File dir = new File(path);
        Files.createDirectories(dir.toPath());

        this.env = Env.create(PROXY_BA)
                .setMapSize(1_024L * 1_024L * 1_024L)
                .setMaxDbs(4)
                .open(dir);

        this.labelsToIdsDb = env.openDbi("labels_to_ids", MDB_CREATE);
        this.idsToLabelsDb = env.openDbi("ids_to_labels", MDB_CREATE);
        this.metaDb        = env.openDbi("meta", MDB_CREATE);

        initCounter();
    }

    private void initCounter() {
        synchronized (lock) {
            ensureOpen();
            try (Txn<byte[]> txn = env.txnWrite()) {
                byte[] stored = metaDb.get(txn, ID_COUNTER_KEY);
                long initial = 1L;
                if (stored != null) {
                    initial = bytesToLong(stored);
                }
                nextId.set(initial);
                metaDb.put(txn, ID_COUNTER_KEY, longToBytes(initial));
                txn.commit();
            }
        }
    }

    private void ensureOpen() {
        if (closed.get()) {
            throw new IllegalStateException("LMDBLabelsStore has been closed");
        }
    }

    private static byte[] longToBytes(long x) {
        ByteBuffer buf = ByteBuffer.allocate(Long.BYTES);
        buf.putLong(x);
        return buf.array();
    }

    private static long bytesToLong(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getLong();
    }

    @Override
    public long idForLabel(byte[] labelBytes) {
        if (labelBytes == null) {
            throw new IllegalArgumentException("Label cannot be null.");
        }

        synchronized (lock) {
            ensureOpen();
            try (Txn<byte[]> txn = env.txnWrite()) {
                byte[] existing = labelsToIdsDb.get(txn, labelBytes);
                if (existing != null) {
                    return bytesToLong(existing);
                }

                long newId = nextId.getAndIncrement();
                byte[] idBytes = longToBytes(newId);

                labelsToIdsDb.put(txn, labelBytes, idBytes);
                idsToLabelsDb.put(txn, idBytes, labelBytes);
                metaDb.put(txn, ID_COUNTER_KEY, longToBytes(nextId.get()));

                txn.commit();
                return newId;
            }
        }
    }

    @Override
    public Map<byte[], Long> idsForLabels(List<byte[]> labels) {
        if (labels == null || labels.isEmpty()) {
            return Map.of();
        }
        Map<byte[], Long> result = new HashMap<>();
        for (byte[] label : labels) {
            if (label != null) {
                long id = idForLabel(label);
                result.put(label, id);
            }
        }
        return result;
    }

    @Override
    public byte[] labelForId(long id) {
        ensureOpen();
        if (id <= 0) return null;
        try (Txn<byte[]> txn = env.txnRead()) {
            byte[] idBytes = longToBytes(id);
            return idsToLabelsDb.get(txn, idBytes);
        }
    }

    @Override
    public Map<Long, byte[]> labelsForIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        Map<Long, byte[]> result = new HashMap<>();
        for (Long id : ids) {
            if (id != null && id > 0) {
                byte[] label = labelForId(id);
                if (label != null) {
                    result.put(id, label);
                }
            }
        }
        return result;
    }

    @Override
    public void close() {
        if (!closed.compareAndSet(false, true)) {
            return;
        }
        try {
            labelsToIdsDb.close();
            idsToLabelsDb.close();
            metaDb.close();
            env.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to close LMDB resources", e);
        }
    }
}
