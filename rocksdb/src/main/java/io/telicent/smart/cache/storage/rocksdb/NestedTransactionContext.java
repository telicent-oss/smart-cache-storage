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
package io.telicent.smart.cache.storage.rocksdb;

import io.telicent.smart.cache.storage.rocksdb.metrics.MetricsHolder;
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
     * @param metrics Metrics
     */
    public NestedTransactionContext(TransactionDB db, ReadOptions readOptions,
                                    WriteOptions writeOptions, MetricsHolder metrics) {
        super(db, readOptions, writeOptions, metrics);
    }

    /**
     * Creates a new nested transaction context.
     *
     * @param db           Transactional Rocks DB
     * @param readOptions  Read options
     * @param writeOptions Write options
     * @param ownsOptions  Whether this context owns the supplied options
     */
    public NestedTransactionContext(TransactionDB db, ReadOptions readOptions, WriteOptions writeOptions,
                                    boolean ownsOptions, MetricsHolder metrics) {
        super(db, readOptions, writeOptions, ownsOptions, metrics);
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
