/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rocksdb;

import org.rocksdb.*;

import java.util.List;
import java.util.Objects;

/**
 * A RocksDB transaction context intended for short-lived transactions, e.g. within the context of a single method call
 * by a storage implementation derived from {@link AbstractRocksDBStorage}.
 */
public class ShortLivedTransactionContext implements TransactionContext {

    private final WriteOptions writeOptions;
    private final ReadOptions readOptions;
    private Transaction rocksTransaction;

    public ShortLivedTransactionContext(TransactionDB db, ReadOptions readOptions, WriteOptions writeOptions) {
        Objects.requireNonNull(db, "db cannot be null");
        this.readOptions = Objects.requireNonNull(readOptions, "readOptions cannot be null");
        this.writeOptions = Objects.requireNonNull(writeOptions, "writeOptions cannot be null");
        this.rocksTransaction = db.beginTransaction(this.writeOptions);
        this.rocksTransaction.setSnapshot();
    }

    @Override
    public byte[] get(ColumnFamilyHandle cfHandle, byte[] key) throws RocksDBException {
        return this.rocksTransaction.get(this.readOptions, cfHandle, key);
    }

    @Override
    public void put(ColumnFamilyHandle cfHandle, byte[] key, byte[] value) throws RocksDBException {
        this.rocksTransaction.put(cfHandle, key, value);
    }

    @Override
    public List<byte[]> multiGetAsList(List<ColumnFamilyHandle> cfHandles, List<byte[]> queryKeys) throws
            RocksDBException {
        return this.rocksTransaction.multiGetAsList(this.readOptions, cfHandles, queryKeys);
    }

    /**
     * Attempts to commit the transaction to RocksDB
     *
     * @throws RocksDBException RocksDB
     */
    @Override
    public void commit() throws RocksDBException {
        this.rocksTransaction.commit();
        this.rocksTransaction.close();
        this.rocksTransaction = null;
    }

    @Override
    public void close() {
        try {
            if (this.rocksTransaction != null) {
                this.rocksTransaction.rollback();
                this.rocksTransaction.close();
            }
        } catch (RocksDBException e) {
            throw new RuntimeException("Failed to rollback RocksDB transaction", e);
        } finally {
            this.readOptions.close();
            this.writeOptions.close();
        }
    }

    @Override
    public long count(ColumnFamilyHandle handle) {
        RocksIterator iterator = this.rocksTransaction.getIterator(handle);

        try {
            long count = 0;
            iterator.seekToFirst();
            while (iterator.isValid()) {
                count++;
                iterator.next();
            }
            return count;
        } finally {
            iterator.close();
        }
    }

}
