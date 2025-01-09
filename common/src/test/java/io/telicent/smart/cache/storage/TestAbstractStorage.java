/**
 * Copyright (C) 2024-2025 Telicent Limited
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
