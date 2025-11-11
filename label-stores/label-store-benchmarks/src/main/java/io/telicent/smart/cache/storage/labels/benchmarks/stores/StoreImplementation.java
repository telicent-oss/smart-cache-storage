/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks.stores;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;

/**
 * A simple facade implementation to facilitate easy testing of different label storage backends
 */
public interface StoreImplementation {

    /**
     * Creates a new fresh empty instance of the store
     *
     * @return New fresh empty store
     */
    DictionaryLabelsStore newStore();

    /**
     * Does any necessary setup for benchmarking e.g. starting/stopping test containers for the storage backend
     */
    void setup();

    /**
     * Does any necessary teardown for benchmarking e.g. stopping/destroying test containers
     */
    void teardown();
}
