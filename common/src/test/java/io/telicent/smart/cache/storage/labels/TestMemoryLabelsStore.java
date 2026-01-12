/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels;

public class TestMemoryLabelsStore extends AbstractLabelStoreTests{
    @Override
    protected LabelsStore newStore() {
        return new MemoryLabelsStore();
    }
}
