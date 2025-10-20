package io.telicent.smart.cache.storage.labels.benchmarks.stores;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;

public interface StoreImplementation {
    DictionaryLabelsStore newStore();

    void setup();

    void teardown();
}
