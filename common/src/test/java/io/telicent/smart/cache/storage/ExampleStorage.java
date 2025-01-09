/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage;

public class ExampleStorage extends AbstractStorage {
    @Override
    protected void closeInternal() {
        // No-op
    }

    public void testEnsureNotClosed() {
        this.ensureNotClosed();
    }
}
