/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels;

import io.telicent.smart.cache.storage.AbstractStorage;

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
public class MemoryLabelsStore extends AbstractStorage implements DictionaryLabelsStore {

    private final AtomicLong ids = new AtomicLong(0);
    private final Map<String, Long> labelsToIds = new ConcurrentHashMap<>();
    private final Map<Long, String> idsToLabels = new ConcurrentHashMap<>();
    private final Object lock = new Object();
    private final Base64.Encoder encoder = Base64.getEncoder();
    private final Base64.Decoder decoder = Base64.getDecoder();

    @Override
    public long idForLabel(byte[] label) {
        ensureNotClosed();
        Objects.requireNonNull(label, "Label cannot be null");

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
        Map<byte[], Long> ids = new LinkedHashMap<>();
        for (byte[] label : labels) {
            // Per contract ignore null labels
            if (label == null) {
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
        }
    }
}
