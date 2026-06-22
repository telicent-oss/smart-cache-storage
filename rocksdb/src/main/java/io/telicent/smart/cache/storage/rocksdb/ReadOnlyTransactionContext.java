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
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.ReadOptions;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.rocksdb.TransactionDB;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A standalone read-only context that performs direct RocksDB reads without allocating a transaction.
 */
public class ReadOnlyTransactionContext implements TransactionContext {

    private final TransactionDB db;
    private final ReadOptions readOptions;
    private final boolean ownsOptions;
    private boolean closed = false;
    private MetricsHolder metrics;

    /**
     * Creates a new read-only context that owns the supplied options.
     *
     * @param db          Transactional Rocks DB
     * @param readOptions Read options
     */
    public ReadOnlyTransactionContext(TransactionDB db, ReadOptions readOptions, MetricsHolder metrics) {
        this(db, readOptions, true, metrics);
    }

    /**
     * Creates a new read-only context.
     *
     * @param db          Transactional Rocks DB
     * @param readOptions Read options
     * @param ownsOptions Whether this context owns the supplied options
     */
    public ReadOnlyTransactionContext(TransactionDB db, ReadOptions readOptions, boolean ownsOptions, MetricsHolder metrics) {
        this.db = Objects.requireNonNull(db, "db cannot be null");
        this.readOptions = Objects.requireNonNull(readOptions, "readOptions cannot be null");
        this.ownsOptions = ownsOptions;
        this.metrics = Objects.requireNonNull(metrics, "Metrics cannot be null");
    }

    private void ensureNotClosed() {
        if (this.closed) {
            throw new UnsupportedOperationException("Transaction is no longer active");
        }
    }

    private void closeOwnedOptions() {
        if (this.ownsOptions) {
            this.readOptions.close();
        }
    }

    @Override
    public byte[] get(ColumnFamilyHandle cfHandle, byte[] key) throws RocksDBException {
        ensureNotClosed();
        return this.db.get(cfHandle, this.readOptions, key);
    }

    @Override
    public void put(ColumnFamilyHandle cfHandle, byte[] key, byte[] value) {
        throw new UnsupportedOperationException("Read-only transactions do not support writes");
    }

    @Override
    public List<byte[]> multiGetAsList(List<ColumnFamilyHandle> cfHandles, List<byte[]> queryKeys) throws
            RocksDBException {
        ensureNotClosed();
        return this.db.multiGetAsList(this.readOptions, cfHandles, queryKeys);
    }

    @Override
    public void commit() {
        if (!this.closed) {
            this.metrics.incrementReadOnlyTransactions();
            this.closed = true;
            try {
                closeOwnedOptions();
            } finally {
                this.metrics.decrementActiveTransactions();
            }
        }
    }

    @Override
    public void close() {
        commit();
    }

    @Override
    public long count(ColumnFamilyHandle handle) {
        ensureNotClosed();
        try (RocksIterator iterator = this.db.newIterator(handle, this.readOptions)) {
            long count = 0;
            iterator.seekToFirst();
            while (iterator.isValid()) {
                count++;
                iterator.next();
            }
            return count;
        }
    }

    @Override
    public boolean isEmpty(ColumnFamilyHandle handle) {
        ensureNotClosed();
        try (RocksIterator iterator = this.db.newIterator(handle, this.readOptions)) {
            iterator.seekToFirst();
            return !iterator.isValid();
        }
    }

    @Override
    public boolean isActive() {
        return !this.closed;
    }

    @Override
    public void forEach(ColumnFamilyHandle handle, Consumer<KeyValue> consumer) {
        ensureNotClosed();
        try (RocksIterator iterator = this.db.newIterator(handle, this.readOptions)) {
            iterator.seekToFirst();
            KeyValue keyValue = KeyValue.of(iterator);
            while (iterator.isValid()) {
                consumer.accept(keyValue);
                iterator.next();
            }
        }
    }

    @Override
    public RocksIterator iterator(ColumnFamilyHandle handle) {
        ensureNotClosed();
        return this.db.newIterator(handle, this.readOptions);
    }

    @Override
    public void delete(ColumnFamilyHandle handle, byte[] key) {
        throw new UnsupportedOperationException("Read-only transactions do not support deletes");
    }
}
