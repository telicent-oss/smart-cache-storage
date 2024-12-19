/**
 * Copyright (C) 2022 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate;

import lombok.experimental.Delegate;

import java.util.Objects;

/**
 * A long-lived transaction context, this is a wrapper around another transaction context that shields the inner context
 * from {@link #commit()} and {@link #close()} calls
 */
final class LongLivedTransactionContext implements TransactionContext {
    @Delegate
    private final TransactionContext transaction;

    public LongLivedTransactionContext(TransactionContext transaction) {
        this.transaction = Objects.requireNonNull(transaction);
    }

    /**
     * Gets the inner transaction context
     * @return Transaction Context
     */
    public TransactionContext getInnerContext() {
        return this.transaction;
    }

    @Override
    public void commit() {
        // Don't commit, commit needs to happen externally
    }

    @Override
    public void close() {
        // Don't close, close needs to happen externally
    }
}
