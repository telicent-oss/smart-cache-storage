/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels;

import io.telicent.smart.cache.storage.AbstractStorage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A simple non-persistent in-memory implementation of a labels store purely to have something to write abstract test
 * suite against that can then be reused for real concrete implementations
 * <p>
 * This is intentionally included only in the {@code tests} module as this merely provides an upper limit for maximum
 * performance and <strong>MUST NOT</strong> ever be used in a production setting because it isn't persistent!
 * </p>
 */
public class MemoryLabelsStore extends AbstractStorage implements LabelsStore {

    private final AtomicLong ids = new AtomicLong(0);
    private final Map<String, Long> labelsToIds = new ConcurrentHashMap<>();
    private final Map<Long, String> idsToLabels = new ConcurrentHashMap<>();
    private final Map<String, Long> keysToLabelIds = new ConcurrentHashMap<>();
    private final Object lock = new Object();
    private final Base64.Encoder encoder = Base64.getEncoder();
    private final Base64.Decoder decoder = Base64.getDecoder();

    @Override
    public long idForLabel(byte[] label) {
        ensureNotClosed();
        if (DictionaryLabelsStore.isInvalidByteSequence(label)) {
            throw new NullPointerException("label cannot be null/empty");
        }

        synchronized (this.lock) {
            Long existingId = this.labelsToIds.get(this.encoder.encodeToString(label));
            if (existingId != null) {
                return existingId;
            } else {
                long newId = this.ids.incrementAndGet();
                this.labelsToIds.put(this.encoder.encodeToString(label), newId);
                this.idsToLabels.put(newId, this.encoder.encodeToString(label));
                return newId;
            }
        }
    }

    @Override
    public Map<byte[], Long> idsForLabels(List<byte[]> labels) {
        ensureNotClosed();
        if (CollectionUtils.isEmpty(labels)) {
            return Collections.emptyMap();
        }

        Map<byte[], Long> ids = new LinkedHashMap<>();
        for (byte[] label : labels) {
            // Per contract ignore null/empty labels
            if (DictionaryLabelsStore.isInvalidByteSequence(label)) {
                continue;
            }
            ids.put(label, this.idForLabel(label));
        }
        return ids;
    }

    @Override
    public byte[] labelForId(long id) {
        ensureNotClosed();
        String encoded = this.idsToLabels.get(id);
        if (encoded == null) {
            return null;
        }
        return this.decoder.decode(encoded);
    }

    @Override
    public Map<Long, byte[]> labelsForIds(List<Long> ids) {
        ensureNotClosed();
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }

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
            labels.put(id, this.labelForId(id));
        }
        return labels;
    }

    @Override
    protected void closeInternal() {
        synchronized (this.lock) {
            this.labelsToIds.clear();
            this.idsToLabels.clear();
            this.keysToLabelIds.clear();
        }
    }

    @Override
    public void setLabel(byte[] key, long labelId) {
        ensureNotClosed();
        if (DictionaryLabelsStore.isInvalidByteSequence(key)) {
            throw new NullPointerException("key cannot be null/empty");
        }

        this.keysToLabelIds.put(this.encoder.encodeToString(key), labelId);
    }

    @Override
    public void setLabels(Map<byte[], Long> keysToLabels) {
        ensureNotClosed();
        if (MapUtils.isEmpty(keysToLabels)) {
            return;
        }

        for (Map.Entry<byte[], Long> entry : keysToLabels.entrySet()) {
            if (DictionaryLabelsStore.isInvalidByteSequence(entry.getKey()) || entry.getValue() == null) {
                continue;
            }

            this.keysToLabelIds.put(this.encoder.encodeToString(entry.getKey()), entry.getValue());
        }
    }

    @Override
    public Long getLabel(byte[] key) {
        ensureNotClosed();
        if (DictionaryLabelsStore.isInvalidByteSequence(key)) {
            throw new NullPointerException("key cannot be null/empty");
        }
        return this.keysToLabelIds.get(this.encoder.encodeToString(key));
    }

    @Override
    public long labelCount() {
        ensureNotClosed();
        return this.labelsToIds.size();
    }

    @Override
    public long keyCount() {
        ensureNotClosed();
        return this.keysToLabelIds.size();
    }
}
