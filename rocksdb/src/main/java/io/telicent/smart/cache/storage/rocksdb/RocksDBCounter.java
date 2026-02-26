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

import org.apache.commons.lang3.StringUtils;
import org.rocksdb.*;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import static io.telicent.smart.cache.storage.rocksdb.AbstractRocksDBStorage.bytesToLong;
import static io.telicent.smart.cache.storage.rocksdb.AbstractRocksDBStorage.longToBytes;

/**
 * Represents a counter backed by a RocksDB database, the counters next available value is persisted in the database as
 * a single key value pair within a column family, thus a single column family can contain many counters.
 */
public class RocksDBCounter {

    public static final long INITIAL_ID = 1L;

    private final byte[] key;
    private final AtomicLong counter = new AtomicLong(INITIAL_ID);
    private final TransactionDB db;
    private final ColumnFamilyHandle cfHandle;

    /**
     * Creates a new counter instance
     *
     * @param db       RocksDB
     * @param cfHandle Column Family in which the counter is stored
     * @param key      Key for the counter
     * @throws RocksDBException Thrown if the counter value cannot be {@link #sync()}'d from the database
     */
    public RocksDBCounter(TransactionDB db, ColumnFamilyHandle cfHandle, String key) throws RocksDBException {
        this.db = Objects.requireNonNull(db, "db cannot be null");
        this.cfHandle = Objects.requireNonNull(cfHandle, "cfHandle cannot be null");
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("key cannot be blank/empty");
        }
        this.key = key.getBytes(StandardCharsets.UTF_8);

        // Sync the counter with stored database state
        this.sync();
    }

    /**
     * Resynchronise the counter with the stored database state
     *
     * @throws RocksDBException Thrown if the counter cannot be synchronised
     */
    public void sync() throws RocksDBException {
        byte[] storedIdBytes = db.get(this.cfHandle, this.key);
        long initialId = INITIAL_ID;
        if (storedIdBytes != null) {
            initialId = bytesToLong(storedIdBytes);
        } else {
            this.db.put(this.cfHandle, this.key, longToBytes(initialId));
        }
        this.counter.set(initialId);
    }

    /**
     * Gets the next available value
     * <p>
     * <strong>NB:</strong> This method <strong>DOES NOT</strong> update the underlying database state.  Only use this
     * overload if you know that you will be calling {@link #next(TransactionContext)} or
     * {@link #update(TransactionContext)} at some point in the near future otherwise the in-memory state will not be
     * in-sync with the database state which could lead to issues in the event of a crash.
     * </p>
     *
     * @return Next available value
     * @throws RocksDBException Never thrown by this overload
     */
    public long next() throws RocksDBException {
        return next(null);
    }

    /**
     * Gets the next available value, optionally also updating the stored counter in the database
     *
     * @param transaction Optional transaction in which to update the new counter value in the database
     * @return Next available value
     * @throws RocksDBException Thrown if the database counter cannot be updated
     */
    public long next(TransactionContext transaction) throws RocksDBException {
        long next = this.counter.getAndIncrement();
        if (transaction != null) {
            this.update(transaction);
        }
        return next;
    }

    /**
     * Updates the database with the current value of the counter
     *
     * @param transaction Transaction to apply the update to
     * @throws RocksDBException Thrown if the database counter cannot be updated
     */
    public void update(TransactionContext transaction) throws RocksDBException {
        Objects.requireNonNull(transaction, "write batch cannot be null");
        transaction.put(this.cfHandle, this.key, longToBytes(counter.get()));
    }
}
