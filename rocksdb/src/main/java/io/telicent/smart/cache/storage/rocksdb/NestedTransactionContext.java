/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rocksdb;

import org.rocksdb.ReadOptions;
import org.rocksdb.RocksDBException;
import org.rocksdb.TransactionDB;
import org.rocksdb.WriteOptions;

import java.util.concurrent.atomic.AtomicInteger;

public class NestedTransactionContext extends ShortLivedTransactionContext {

    private final AtomicInteger nesting = new AtomicInteger(1);

    public NestedTransactionContext(TransactionDB db, ReadOptions readOptions,
                                    WriteOptions writeOptions) {
        super(db, readOptions, writeOptions);
    }

    /**
     * Increments the level of nesting
     */
    NestedTransactionContext increment() {
        this.nesting.incrementAndGet();
        return this;
    }

    @Override
    public void commit() throws RocksDBException {
        if (this.nesting.get() == 1) {
            super.commit();
        }
    }

    @Override
    public void close() {
        if (this.nesting.decrementAndGet() == 0) {
            super.close();
        }
    }

}
