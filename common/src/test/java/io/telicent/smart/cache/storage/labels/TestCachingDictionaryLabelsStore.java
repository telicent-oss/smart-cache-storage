/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels;

import org.testng.annotations.Test;

public class TestCachingDictionaryLabelsStore extends AbstractDictionaryLabelStoreTests{
    @Override
    protected DictionaryLabelsStore newDictionaryStore() {
        return new CachingDictionaryLabelsStore(new MemoryLabelsStore(), 1_000);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*greater than zero")
    public void givenZeroCacheSize_whenCreatingStore_thenIllegalArgument() {
        // Given, When and Then
        new CachingDictionaryLabelsStore(new MemoryLabelsStore(), 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*greater than zero")
    public void givenNegativeCacheSize_whenCreatingStore_thenIllegalArgument() {
        // Given, When and Then
        new CachingDictionaryLabelsStore(new MemoryLabelsStore(), Integer.MIN_VALUE);
    }
}
