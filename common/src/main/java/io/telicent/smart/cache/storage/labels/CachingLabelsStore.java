/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.telicent.smart.cache.storage.AbstractStorage;

import java.util.Base64;
import java.util.Objects;

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
    public byte[] labelForId(long id) {
        return this.idsToLabels.get(id, this.store::labelForId);
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
