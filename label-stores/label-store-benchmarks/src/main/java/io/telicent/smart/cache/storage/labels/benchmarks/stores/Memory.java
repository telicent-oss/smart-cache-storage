package io.telicent.smart.cache.storage.labels.benchmarks.stores;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.MemoryLabelsStore;

public class Memory implements StoreImplementation{
    @Override
    public DictionaryLabelsStore newStore() {
        return new MemoryLabelsStore();
    }

    @Override
    public void setup() {

    }

    @Override
    public void teardown() {

    }
}
