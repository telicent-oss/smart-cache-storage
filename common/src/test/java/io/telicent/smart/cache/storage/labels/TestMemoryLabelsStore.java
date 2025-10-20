/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels;

public class TestMemoryLabelsStore extends AbstractDictionaryLabelStoreTests{
    @Override
    protected DictionaryLabelsStore newStore() {
        return new MemoryLabelsStore();
    }
}
