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

import org.rocksdb.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A RocksDB transaction context intended for short-lived transactions, e.g. within the context of a single method call
 * by a storage implementation derived from {@link AbstractRocksDBStorage}.
 */
public class ShortLivedTransactionContext implements TransactionContext {

    private final WriteOptions writeOptions;
    private final ReadOptions readOptions;
    private final boolean ownsOptions;
    private Transaction rocksTransaction;

    /**
     * Creates a new short-lived transaction context that <strong>owns</strong> the supplied options,
     * and will close them when this context is committed/closed.
     *
     * @param db           Transactional Rocks DB
     * @param readOptions  Read options
     * @param writeOptions Write options
     */
    public ShortLivedTransactionContext(TransactionDB db, ReadOptions readOptions, WriteOptions writeOptions) {
        this(db, readOptions, writeOptions, true);
    }

    /**
     * Creates a new short-lived transaction context.
     *
     * @param db           Transactional Rocks DB
     * @param readOptions  Read options
     * @param writeOptions Write options
     * @param ownsOptions  Whether this context owns the supplied options.
     */
    public ShortLivedTransactionContext(TransactionDB db, ReadOptions readOptions, WriteOptions writeOptions,
                                        boolean ownsOptions) {
        Objects.requireNonNull(db, "db cannot be null");
        this.readOptions = Objects.requireNonNull(readOptions, "readOptions cannot be null");
        this.writeOptions = Objects.requireNonNull(writeOptions, "writeOptions cannot be null");
        this.ownsOptions = ownsOptions;
        this.rocksTransaction = db.beginTransaction(this.writeOptions);
        this.rocksTransaction.setSnapshot();
    }

    private void ensureNotClosed() {
        if (this.rocksTransaction == null) {
            throw new IllegalStateException("Transaction already closed");
        }
    }

    @Override
    public byte[] get(ColumnFamilyHandle cfHandle, byte[] key) throws RocksDBException {
        ensureNotClosed();
        return this.rocksTransaction.get(this.readOptions, cfHandle, key);
    }

    @Override
    public void put(ColumnFamilyHandle cfHandle, byte[] key, byte[] value) throws RocksDBException {
        ensureNotClosed();
        this.rocksTransaction.put(cfHandle, key, value);
    }

    @Override
    public List<byte[]> multiGetAsList(List<ColumnFamilyHandle> cfHandles, List<byte[]> queryKeys) throws
            RocksDBException {
        ensureNotClosed();
        return this.rocksTransaction.multiGetAsList(this.readOptions, cfHandles, queryKeys);
    }

    /**
     * Attempts to commit the transaction to RocksDB
     *
     * @throws RocksDBException RocksDB
     */
    @Override
    public void commit() throws RocksDBException {
        if (this.rocksTransaction != null) {
            this.rocksTransaction.commit();
            this.rocksTransaction.close();
            this.rocksTransaction = null;
            closeOwnedOptions();
        }
     }

    @Override
    public void close() {
        try {
            if (this.rocksTransaction != null) {
                this.rocksTransaction.rollback();
                this.rocksTransaction.close();
                this.rocksTransaction = null;
            }
        } catch (RocksDBException e) {
            throw new RuntimeException("Failed to rollback RocksDB transaction", e);
        } finally {
            closeOwnedOptions();
        }
    }

    /**
     * Closes the read/write options if this context owns them. RocksDB native option objects are safe to
     * close more than once so this method is idempotent.
     */
    private void closeOwnedOptions() {
        if (this.ownsOptions) {
            this.readOptions.close();
            this.writeOptions.close();
        }
    }

    @Override
    public long count(ColumnFamilyHandle handle) {
        ensureNotClosed();
        try (RocksIterator iterator = this.rocksTransaction.getIterator(handle)) {
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
        try (RocksIterator iterator = this.rocksTransaction.getIterator(handle)) {
            iterator.seekToFirst();
            return !iterator.isValid();
        }
    }

    @Override
    public void forEach(ColumnFamilyHandle handle, Consumer<KeyValue> consumer) {
        ensureNotClosed();
        try (RocksIterator iterator = this.rocksTransaction.getIterator(handle)) {
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
        return this.rocksTransaction.getIterator(handle);
    }

    @Override
    public void delete(ColumnFamilyHandle handle, byte[] key) throws RocksDBException {
        this.rocksTransaction.delete(handle, key);
    }

    /**
     * Gets whether the transaction remains active
     *
     * @return True if active, false otherwise
     */
    public boolean isActive() {
        return this.rocksTransaction != null;
    }
}
