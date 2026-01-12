/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.*;

/**
 * A caching decorator over another labels store
 */
public class CachingLabelsStore extends CachingDictionaryLabelsStore implements LabelsStore {

    private final LabelsStore labelStore;
    private final Cache<String, Long> keysToLabels;

    /**
     * Creates a new caching store with the same cache size used for each of the individual caches
     * <p>
     * For some use cases it may be better to use the {@link #CachingLabelsStore(LabelsStore, int, int, int)}
     * constructor that lets you customise each cache size individually
     * </p>
     *
     * @param labelStore Underlying labels store
     * @param cacheSize  Size of the caches
     */
    public CachingLabelsStore(LabelsStore labelStore, int cacheSize) {
        this(labelStore, cacheSize, cacheSize, cacheSize);
    }

    /**
     * Creates a new caching store with different cache sizes used for each of the individual caches
     *
     * @param labelStore            Underlying labels store
     * @param labelsToIdsCacheSize  Cache size for the labels to ID cache (used for {@link #idForLabel(byte[])} and
     *                              {@link #idsForLabels(List)} calls)
     * @param idsToLabelsCacheSize  Cache size for the IDS to labels cache (used for {@link #labelForId(long)} and
     *                              {@link #labelsForIds(List)} calls)
     * @param keysToLabelsCacheSize Cache size for the keys to labels cache (used for {@link #getLabel(byte[])} calls)
     */
    public CachingLabelsStore(LabelsStore labelStore, int labelsToIdsCacheSize, int idsToLabelsCacheSize,
                              int keysToLabelsCacheSize) {
        super(labelStore, labelsToIdsCacheSize, idsToLabelsCacheSize);
        this.labelStore = Objects.requireNonNull(labelStore, "Underlying store cannot be null");
        validateCacheSize(keysToLabelsCacheSize);
        this.keysToLabels = Caffeine.newBuilder()
                                    .initialCapacity(keysToLabelsCacheSize / 4)
                                    .maximumSize(keysToLabelsCacheSize)
                                    .build();
    }

    @Override
    protected void closeInternal() {
        try {
            super.closeInternal();
        } finally {
            this.keysToLabels.invalidateAll();
        }
    }

    @Override
    public final void setLabel(byte[] key, long labelId) {
        this.labelStore.setLabel(key, labelId);

        // If set succeeds then update cache
        String encoded = this.encoder.encodeToString(key);
        this.keysToLabels.put(encoded, labelId);
    }

    @Override
    public final void setLabels(Map<byte[], Long> keysToLabels) {
        if (MapUtils.isEmpty(keysToLabels)) {
            return;
        }
        this.labelStore.setLabels(keysToLabels);

        // If set succeeds then update cache
        for (Map.Entry<byte[], Long> entry : keysToLabels.entrySet()) {
            if (DictionaryLabelsStore.isInvalidByteSequence(entry.getKey()) || entry.getValue() == null) {
                continue;
            }
            this.keysToLabels.put(this.encoder.encodeToString(entry.getKey()), entry.getValue());
        }
    }

    @Override
    public final Long getLabel(byte[] key) {
        if (DictionaryLabelsStore.isInvalidByteSequence(key)) {
            throw new NullPointerException("key cannot be null/empty");
        }
        String encoded = this.encoder.encodeToString(key);
        return this.keysToLabels.get(encoded, k -> this.labelStore.getLabel(key));
    }

    @Override
    public final long keyCount() {
        return this.labelStore.keyCount();
    }
}
