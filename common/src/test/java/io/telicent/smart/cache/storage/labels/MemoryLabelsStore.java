/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels;

import io.telicent.smart.cache.storage.AbstractStorage;

import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A simple non-persistent in-memory implementation of a labels store purely to have something to write abstract test
 * suite against that can then be reused for real concrete implementations
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
    public byte[] labelForId(long id) {
        ensureNotClosed();
        String encoded = this.idsToLabels.get(id);
        if (encoded == null) {
            return null;
        }
        return this.decoder.decode(encoded);
    }

    @Override
    protected void closeInternal() {
        synchronized (this.lock) {
            this.labelsToIds.clear();
            this.idsToLabels.clear();
        }
    }
}
