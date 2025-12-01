/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks.stores;

import io.telicent.smart.cache.storage.labels.LabelsStore;
import io.telicent.smart.cache.storage.labels.MemoryLabelsStore;

/**
 * Tests the non-persistent {@link MemoryLabelsStore} which provides an upper limit for maximum performance
 */
public class Memory implements StoreImplementation{
    @Override
    public LabelsStore newStore() {
        return new MemoryLabelsStore();
    }

    @Override
    public void setup() {

    }

    @Override
    public void teardown() {

    }
}
