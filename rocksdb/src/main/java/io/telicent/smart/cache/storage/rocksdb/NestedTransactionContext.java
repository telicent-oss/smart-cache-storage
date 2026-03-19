/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rocksdb;

import org.rocksdb.ReadOptions;
import org.rocksdb.RocksDBException;
import org.rocksdb.TransactionDB;
import org.rocksdb.WriteOptions;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A virtual "nested" transaction context that allows for longer lived transactions that appear "nested" from the
 * application perspective but are actually just one shared longer running transaction.  The transaction is only
 * actually committed/closed when the {@link #commit()} or {@link #close()} methods are called at the outermost level of
 * nesting.
 */
public class NestedTransactionContext extends ShortLivedTransactionContext {

    private final AtomicInteger nesting = new AtomicInteger(1);

    /**
     * Creates a new nested transaction context
     *
     * @param db           Transactional Rocks DB
     * @param readOptions  Read options
     * @param writeOptions Write options
     */
    public NestedTransactionContext(TransactionDB db, ReadOptions readOptions,
                                    WriteOptions writeOptions) {
        super(db, readOptions, writeOptions);
    }

    /**
     * Increments the level of nesting
     *
     * @return Self
     */
    NestedTransactionContext increment() {
        this.nesting.incrementAndGet();
        return this;
    }

    @Override
    public void commit() throws RocksDBException {
        // We only commit the transaction if we've reached the outermost level of nesting, commit()'s at inner nesting
        // levels are effectively postponed until the outermost commit()
        if (this.nesting.get() == 1) {
            super.commit();
        }
    }

    @Override
    public void close() {
        // We only close the transaction once we've reached the outermost level of nesting, close()'s at inner nesting
        // levels are effectively postponed under the outermost close()
        if (this.nesting.decrementAndGet() == 0) {
            super.close();
        }
    }

}
