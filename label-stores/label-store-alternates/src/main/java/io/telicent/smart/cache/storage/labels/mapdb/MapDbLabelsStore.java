/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.mapdb;

import io.telicent.smart.cache.storage.AbstractStorage;
import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * MapDB-based implementation of DictionaryLabelsStore.
 * Uses two HTreeMaps and a persistent AtomicLong for the ID counter.
 */
public class MapDbLabelsStore extends AbstractStorage implements DictionaryLabelsStore {

    private final DB db;
    private final HTreeMap<byte[], Long> labelsToIds;
    private final HTreeMap<Long, byte[]> idsToLabels;
    private final AtomicLong nextId;
    private final org.mapdb.Atomic.Long persistentCounter;

    private final Object lock = new Object();

    public MapDbLabelsStore(String filePath) throws IOException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null) {
            Files.createDirectories(parent.toPath());
        }

        this.db = DBMaker
                .fileDB(file)
                .fileMmapEnableIfSupported()
                .transactionEnable()
                .make();

        this.labelsToIds = db
                .hashMap("labels_to_ids", Serializer.BYTE_ARRAY, Serializer.LONG)
                .createOrOpen();

        this.idsToLabels = db
                .hashMap("ids_to_labels", Serializer.LONG, Serializer.BYTE_ARRAY)
                .createOrOpen();

        this.persistentCounter = db.atomicLong("next_id").createOrOpen();
        long initial = persistentCounter.get();
        if (initial <= 0L) {
            initial = 1L;
            persistentCounter.set(initial);
            db.commit();
        }
        this.nextId = new AtomicLong(initial);
    }

    @Override
    public long idForLabel(byte[] labelBytes) {
        ensureNotClosed();
        Objects.requireNonNull(labelBytes, "Label cannot be null");

        synchronized (lock) {

            Long existing = labelsToIds.get(labelBytes);
            if (existing != null) {
                return existing;
            }

            long newId = nextId.getAndIncrement();

            labelsToIds.put(labelBytes, newId);
            idsToLabels.put(newId, labelBytes);

            persistentCounter.set(nextId.get());
            db.commit();

            return newId;
        }
    }

    @Override
    public Map<byte[], Long> idsForLabels(List<byte[]> labels) {
        ensureNotClosed();
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
        ensureNotClosed();
        if (id <= 0) return null;
        return idsToLabels.get(id);
    }

    @Override
    public Map<Long, byte[]> labelsForIds(List<Long> ids) {
        ensureNotClosed();
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
    public long labelSize() {
        ensureNotClosed();
        return this.labelsToIds.size();
    }

    @Override
    protected void closeInternal() {
        try {
            db.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to close MapDB resources", e);
        }
    }
}
