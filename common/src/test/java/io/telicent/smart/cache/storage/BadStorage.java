/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage;

public class BadStorage extends ExampleStorage {
    @Override
    protected void closeInternal() {
        throw new RuntimeException("Failed to close storage");
    }
}
