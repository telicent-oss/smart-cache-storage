/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage;

/**
 * Abstract base class for closeable storage implementations
 */
public abstract class AbstractStorage implements AutoCloseable {
    /**
     * The exception message thrown as an {@link IllegalStateException} by {@link #ensureNotClosed()}
     */
    public static final String STORAGE_ALREADY_CLOSED = "Storage already closed";

    private volatile boolean closed = false;

    /**
     * Ensures that the storage has not been closed and throws an {@link IllegalStateException} if it has
     * <p>
     * This is intended for use by developers in their derived storage implementations, it should be called at the start
     * of any method that queries/modifies the storage so that the operation can be promptly aborted if the caller is
     * attempting operations after {@link #close()} has been called.
     * </p>
     *
     * @throws IllegalStateException Thrown when the storage has been closed
     */
    protected final void ensureNotClosed() {
        if (this.closed) {
            throw new IllegalStateException(STORAGE_ALREADY_CLOSED);
        }
    }

    /**
     * Gets whether the storage has been closed
     * <p>
     * For most developer focused use cases your derived implementations likely want to call {@link #ensureNotClosed()}
     * instead as that throws an explicit {@link IllegalStateException} if the storage is closed and can be used to
     * quickly abort any action that is attempted after {@link #close()} has been called
     * </p>
     *
     * @return True if closed, false otherwise
     */
    public boolean isClosed() {
        return this.closed;
    }

    @Override
    public final synchronized void close() {
        if (!this.closed) {
            try {
                this.closeInternal();
            } finally {
                // Always mark as closed even if internal close fails, this is done in the finally block so if an
                // exception occurs it still gets thrown upwards for the caller to deal with
                this.closed = true;
            }
        }
    }

    /**
     * Implements storage implementation specific close logic, this is guaranteed to be called once, and only once,
     * however many times {@link #close()} might be called
     */
    protected abstract void closeInternal();
}
