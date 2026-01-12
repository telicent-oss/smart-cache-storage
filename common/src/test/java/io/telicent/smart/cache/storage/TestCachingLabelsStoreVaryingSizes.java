/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage;

import io.telicent.smart.cache.storage.labels.CachingLabelsStore;
import io.telicent.smart.cache.storage.labels.LabelsStore;
import io.telicent.smart.cache.storage.labels.MemoryLabelsStore;
import io.telicent.smart.cache.storage.labels.TestCachingLabelsStore;

public class TestCachingLabelsStoreVaryingSizes extends TestCachingLabelsStore {
    @Override
    protected LabelsStore newStore() {
        return new CachingLabelsStore(new MemoryLabelsStore(), 1_000, 5_000, 10_000);
    }
}
