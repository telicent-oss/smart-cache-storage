/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks.stores;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.LabelsStore;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

import java.util.Map;

@AllArgsConstructor
public class PlaceholderLabelsStore implements LabelsStore {

    @Delegate
    private final DictionaryLabelsStore dictionaryLabelsStore;

    @Override
    public void setLabel(byte[] key, long labelId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLabels(Map<byte[], Long> keysToLabels) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Long getLabel(byte[] key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long keyCount() {
        throw new UnsupportedOperationException();
    }
}
