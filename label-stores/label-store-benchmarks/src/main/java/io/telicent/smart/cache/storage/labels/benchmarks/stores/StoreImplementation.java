/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks.stores;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;

public interface StoreImplementation {
    DictionaryLabelsStore newStore();

    void setup();

    void teardown();
}
