/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.telicent.smart.cache.storage.AbstractStorage;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * A caching decorator over another label dictionary store
 */
public class CachingDictionaryLabelsStore extends AbstractStorage implements DictionaryLabelsStore {

    public static final String HASH_PREFIX = "sha512:";
    private final DictionaryLabelsStore dictionaryStore;
    private final Cache<String, Long> labelsToIds;
    private final Cache<Long, byte[]> idsToLabels;
    protected final Base64.Encoder encoder = Base64.getEncoder();
    protected final DigestHelper digest;

    /**
     * Creates a new caching store with the same cache size used for each of the individual caches
     * <p>
     * For some use cases it may be better to use the
     * {@link #CachingDictionaryLabelsStore(DictionaryLabelsStore, int, int)} constructor that lets you customise each
     * cache size individually
     * </p>
     *
     * @param dictionaryStore Underlying labels store
     * @param cacheSize       Size of the caches
     */
    public CachingDictionaryLabelsStore(DictionaryLabelsStore dictionaryStore, int cacheSize) {
        this(dictionaryStore, cacheSize, cacheSize);
    }

    /**
     * Creates a new caching store with different cache sizes used for each of the individual caches
     *
     * @param dictionaryStore      Underlying labels store
     * @param labelsToIdsCacheSize Cache size for the labels to ID cache (used for {@link #idForLabel(byte[])} and
     *                             {@link #idsForLabels(List)} calls)
     * @param idsToLabelsCacheSize Cache size for the IDS to labels cache (used for {@link #labelForId(long)} and
     *                             {@link #labelsForIds(List)} calls)
     */
    public CachingDictionaryLabelsStore(DictionaryLabelsStore dictionaryStore, int labelsToIdsCacheSize,
                                        int idsToLabelsCacheSize) {
        this.dictionaryStore = Objects.requireNonNull(dictionaryStore, "Underlying store cannot be null");
        validateCacheSize(labelsToIdsCacheSize);
        validateCacheSize(idsToLabelsCacheSize);
        this.labelsToIds = Caffeine.newBuilder()
                                   .initialCapacity(labelsToIdsCacheSize / 4)
                                   .maximumSize(labelsToIdsCacheSize)
                                   .build();
        this.idsToLabels = Caffeine.newBuilder()
                                   .initialCapacity(idsToLabelsCacheSize / 4)
                                   .maximumSize(idsToLabelsCacheSize)
                                   .build();

        this.digest = new DigestHelper("SHA512");
    }

    /**
     * Validates that a given cache size meets the constraint of being a positive number greater than 1
     *
     * @param cacheSize Cache size to validate
     * @throws IllegalArgumentException Thrown if the cache size is zero or negative
     */
    protected static void validateCacheSize(int cacheSize) {
        if (cacheSize <= 0) {
            throw new IllegalArgumentException("Cache size must be greater than zero");
        }
    }


    @Override
    public final long idForLabel(byte[] label) {
        if (DictionaryLabelsStore.isInvalidByteSequence(label)) {
            throw new NullPointerException("label cannot be null/empty");
        }
        String encoded = toCacheKey(label);
        return this.labelsToIds.get(encoded, k -> this.dictionaryStore.idForLabel(label));
    }

    /**
     * Given a label converts it for use as a cache key
     *
     * @param label Label byte sequence
     * @return Cache key
     */
    private String toCacheKey(byte[] label) {
        if (label.length <= 1024) {
            // For short labels just Base64 encode
            return this.encoder.encodeToString(label);
        } else {
            // For anything larger take a SHA512 hash instead
            return HASH_PREFIX + Hex.encodeHexString(this.digest.digest(label));
        }
    }

    @Override
    public final Map<byte[], Long> idsForLabels(List<byte[]> labels) {
        if (CollectionUtils.isEmpty(labels)) {
            return Collections.emptyMap();
        }

        // Can everything be satisfied from the cache?
        boolean allCached = true;
        Map<byte[], Long> ids = new LinkedHashMap<>();
        for (byte[] label : labels) {
            if (DictionaryLabelsStore.isInvalidByteSequence(label)) {
                continue;
            }
            String encoded = toCacheKey(label);
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
        // We also update the cache as we do this to improve subsequent insert performance
        List<byte[]> uncachedLabels =
                ids.entrySet().stream().filter(e -> e.getValue() == null).map(Map.Entry::getKey).toList();
        Map<byte[], Long> uncachedIds = this.dictionaryStore.idsForLabels(uncachedLabels);
        for (Map.Entry<byte[], Long> entry : uncachedIds.entrySet()) {
            this.labelsToIds.put(toCacheKey(entry.getKey()), entry.getValue());
        }
        ids.putAll(uncachedIds);

        return ids;
    }

    @Override
    public final byte[] labelForId(long id) {
        return this.idsToLabels.get(id, this.dictionaryStore::labelForId);
    }

    @Override
    public final Map<Long, byte[]> labelsForIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }

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
        // We also update the cache to improve subsequent lookup performance
        List<Long> uncachedIds =
                labels.entrySet().stream().filter(e -> e.getValue() == null).map(Map.Entry::getKey).toList();
        Map<Long, byte[]> uncachedLabels = this.dictionaryStore.labelsForIds(uncachedIds);
        for (Map.Entry<Long, byte[]> entry : uncachedLabels.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            this.idsToLabels.put(entry.getKey(), entry.getValue());
        }
        labels.putAll(uncachedLabels);

        return labels;

    }

    @Override
    public final long labelCount() {
        return this.dictionaryStore.labelCount();
    }

    @Override
    protected void closeInternal() {
        try {
            this.dictionaryStore.close();
        } finally {
            this.labelsToIds.invalidateAll();
            this.idsToLabels.invalidateAll();
        }
    }
}
