/**
 * Copyright (C) Telicent Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
