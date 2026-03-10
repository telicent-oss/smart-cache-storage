/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rocksdb;

import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDBException;

import java.util.List;

/**
 * A transaction context helper for {@link AbstractRocksDBStorage} derived storage implementations
 */
public interface TransactionContext extends AutoCloseable {
    /**
     * Gets a single key within the transaction context
     *
     * @param cfHandle Column family handle
     * @param key      Key
     * @return Value, possibly {@code null} if no such key exists in the column family
     * @throws RocksDBException Thrown if RocksDB is unable to process the read operation
     */
    byte[] get(ColumnFamilyHandle cfHandle, byte[] key) throws RocksDBException;

    /**
     * Puts a single key within the transaction context
     *
     * @param cfHandle Column family handle
     * @param key      Key
     * @param value    Value
     * @throws RocksDBException Thrown if RocksDB is unable to process the write operation
     */
    void put(ColumnFamilyHandle cfHandle, byte[] key, byte[] value) throws RocksDBException;

    /**
     * Performs a multi-get operation allowing for multiple keys to be looked up in a single operation (from the
     * callers' perspective)
     * <p>
     * Using this is generally preferred over {@link #get(ColumnFamilyHandle, byte[])} unless only a single key is
     * required as multi-get operations allow RocksDB to amortize some of the costs of a key lookup across multiple
     * lookups.
     * </p>
     *
     * @param cfHandles Column family handles corresponding to the keys to be queried
     * @param queryKeys Keys to be queried
     * @return List of values, each of which may be {@code null} if no such key existing in the column family
     * @throws RocksDBException Thrown if RocksDB is unable to process the read operation
     */
    List<byte[]> multiGetAsList(List<ColumnFamilyHandle> cfHandles, List<byte[]> queryKeys) throws
            RocksDBException;

    /**
     * Commits any changes in the transaction releasing any resources it was holding
     * <p>
     * If this is not called prior to calling {@link #close()} then the transaction will be rolled back and not
     * committed.  Therefore, developers <strong>MUST</strong> ensure they always call {@link #commit()} when they are
     * done with a transaction otherwise any changes will be lost!
     * </p>
     *
     * @throws RocksDBException Thrown if the transaction cannot be committed
     */
    void commit() throws RocksDBException;

    /**
     * Closes the transaction releasing any resources it was holding
     * <p>
     * If this is called without {@link #commit()} having previously been called then the transaction is rolled back and
     * any changes are lost.
     * </p>
     */
    @Override
    void close();

    /**
     * Counts the number of keys in the given column family
     *
     * @param handle Column family handle
     * @return Number of keys in the given column family
     */
    long count(ColumnFamilyHandle handle);

    /**
     * Determines whether the given column family is empty i.e. has no keys
     *
     * @param handle Column family handle
     * @return True if empty, false if non-empty
     */
    boolean isEmpty(ColumnFamilyHandle handle);
}
