/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.telicent.smart.cache.storage.AbstractStorage;

import java.util.*;

/**
 * A caching decorator over another labels store
 */
public class CachingLabelsStore extends AbstractStorage implements DictionaryLabelsStore {

    private final DictionaryLabelsStore store;
    private final Cache<String, Long> labelsToIds;
    private final Cache<Long, byte[]> idsToLabels;
    private final Base64.Encoder encoder = Base64.getEncoder();

    /**
     * Creates a new caching store
     *
     * @param store Underlying labels store
     */
    public CachingLabelsStore(DictionaryLabelsStore store, int cacheSize) {
        this.store = Objects.requireNonNull(store, "Underlying store cannot be null");
        if (cacheSize <= 0) {
            throw new IllegalArgumentException("Cache size must be greater than zero");
        }
        this.labelsToIds = Caffeine.newBuilder().initialCapacity(cacheSize / 4).maximumSize(cacheSize).build();
        this.idsToLabels = Caffeine.newBuilder().initialCapacity(cacheSize).maximumSize(cacheSize).build();
    }

    @Override
    public long idForLabel(byte[] label) {
        Objects.requireNonNull(label, "Label cannot be null");
        String encoded = this.encoder.encodeToString(label);
        return this.labelsToIds.get(encoded, k -> this.store.idForLabel(label));
    }

    @Override
    public Map<byte[], Long> idsForLabels(List<byte[]> labels) {
        // Can everything be satisfied from the cache?
        boolean allCached = true;
        Map<byte[], Long> ids = new LinkedHashMap<>();
        for (byte[] label : labels) {
            if (label == null) {
                continue;
            }
            String encoded = this.encoder.encodeToString(label);
            Long id = this.labelsToIds.getIfPresent(encoded);
            ids.put(label, id);
            if (id == null) {
                allCached = false;
            }
        }

        if (allCached) {
            return ids;
        }

        // If not then get the rest from the underlying store and combine the results
        List<byte[]> uncachedLabels =
                ids.entrySet().stream().filter(e -> e.getValue() == null).map(Map.Entry::getKey).toList();
        Map<byte[], Long> uncachedIds = this.store.idsForLabels(uncachedLabels);
        ids.putAll(uncachedIds);

        return ids;
    }

    @Override
    public byte[] labelForId(long id) {
        return this.idsToLabels.get(id, this.store::labelForId);
    }

    @Override
    public Map<Long, byte[]> labelsForIds(List<Long> ids) {
        // Can everything be satisfied from the cache?
        boolean allCached = true;
        Map<Long, byte[]> labels = new LinkedHashMap<>();
        for (Long id : ids) {
            // Ignore null IDs
            if (id == null) {
                continue;
            }
            // NB - The list might contain duplicate IDs
            if (labels.containsKey(id)) {
                continue;
            }
            labels.put(id, this.idsToLabels.getIfPresent(id));
            if (labels.get(id) == null) {
                allCached = false;
            }
        }
        if (allCached) {
            return labels;
        }

        // For any uncached IDs lookup in the underlying store then combine the results
        List<Long> uncachedIds =
                labels.entrySet().stream().filter(e -> e.getValue() == null).map(Map.Entry::getKey).toList();
        Map<Long, byte[]> uncachedLabels = this.store.labelsForIds(uncachedIds);
        labels.putAll(uncachedLabels);

        return labels;

    }

    @Override
    protected void closeInternal() {
        try {
            this.store.close();
        } finally {
            this.labelsToIds.invalidateAll();
            this.idsToLabels.invalidateAll();
        }
    }
}
