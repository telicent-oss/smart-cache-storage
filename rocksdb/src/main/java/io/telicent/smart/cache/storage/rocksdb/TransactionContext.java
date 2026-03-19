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

import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

import java.util.List;
import java.util.function.Consumer;

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

    /**
     * Gets whether the transaction remains active i.e. hasn't been committed/closed
     *
     * @return True if the transaction remains active, false otherwise
     */
    boolean isActive();

    /**
     * Iterates over the given column family applying a consumer to each key value pair
     * <p>
     * This method should be used rarely <strong>only</strong> in scenarios where full column family iteration is
     * required e.g. data migration.  For very large iterations it may be better to use
     * {@link #iterator(ColumnFamilyHandle)} instead as that provides the caller more control over how the column family
     * is iterated.
     * </p>
     * <p>
     * Note that the {@link KeyValue} passed to the consumer is a temporary pointer into the underlying storage so the
     * consumer <strong>MUST</strong> perform any processing of the key and/or value immediately.  It
     * <strong>MUST</strong> also not hold onto pointers to the key/value references beyond a single invocation of
     * itself as those pointers <strong>MAY NOT</strong> remain valid over time.
     * </p>
     *
     * @param handle   Column family handle
     * @param consumer Consumer function
     */
    void forEach(ColumnFamilyHandle handle, Consumer<KeyValue> consumer);

    /**
     * Obtains a RocksDB iterator for the column family within the context of this transaction
     * <p>
     * The returned iterator will be a raw iterator not positioned anywhere, the caller should call
     * {@link RocksIterator#seekToFirst()} or {@link RocksIterator#seek(byte[])} before starting to use the iterator
     * </p>
     *
     * @param handle Column family handle
     * @return Rocks Iterator
     */
    RocksIterator iterator(ColumnFamilyHandle handle);
}
