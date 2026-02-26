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
package io.telicent.smart.cache.storage;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestAbstractStorage {

    @Test
    public void givenExampleStorage_whenClosing_thenMarkedAsClosed() {
        // Given
        try (ExampleStorage storage = new ExampleStorage()) {
            // When
            Assert.assertFalse(storage.isClosed());
            storage.close();

            // Then
            Assert.assertTrue(storage.isClosed());
        }
    }

    @Test
    public void givenBadStorage_whenClosing_thenMarkedAsClosed() {
        // Given
        try (ExampleStorage storage = new BadStorage()) {
            // When
            Assert.assertFalse(storage.isClosed());
            Assert.assertThrows(RuntimeException.class, storage::close);

            // Then
            Assert.assertTrue(storage.isClosed());
        }
    }

    @Test
    public void givenGoodStorage_whenCheckingIfClosed_thenOk() {
        // Given
        try (ExampleStorage storage = new ExampleStorage()) {
            // When and Then
            storage.testEnsureNotClosed();
        }
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = AbstractStorage.STORAGE_ALREADY_CLOSED)
    public void givenGoodStorage_whenClosedAndCheckingIfClosed_thenIllegalStateException() {
        // Given
        try (ExampleStorage storage = new ExampleStorage()) {
            // When and Then
            storage.close();
            storage.testEnsureNotClosed();
        }

    }
}
