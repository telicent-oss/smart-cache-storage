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

import io.telicent.smart.cache.storage.AbstractStorage;
import org.rocksdb.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/**
 * Abstract implementation of RocksDB backed storage, see {@link #AbstractRocksDBStorage(File)} for customisation
 * options.
 * <p>
 * The primary usage pattern for this in derived implementations is as follows:
 * </p>
 * <code>
 * // Start a fresh transaction
 * try (TransactionContext context = this.begin()) {
 *     // Get a column family handle
 *     ColumnFamilyHandle cfHandle = this.getHandle(NAME_OF_COLUMN_FAMILY);
 * 
 *     // Perform some operations
 *     context.put(key, value);
 *
 *     // Commit the transaction
 *     context.commit();
 * }
 * </code>
 */
public abstract class AbstractRocksDBStorage extends AbstractStorage {

    private final TransactionDB db;
    private final Options options;
    private final TransactionDBOptions transactionOptions;
    private final Map<String, ColumnFamilyHandle> columnFamilyHandles;
    private final Map<String, RocksDBCounter> counters;

    /**
     * Creates a new instance of RocksDB backed storage
     * <p>
     * Derived implementations <strong>MUST</strong> implement the following methods:
     * </p>
     * <ul>
     *     <li>{@link #prepareColumnFamilyDescriptors(ColumnFamilyOptions)}  to prepare the {@link ColumnFamilyDescriptor}'s needed by the storage implementation.</li>
     * </ul>
     * <p>
     * Derived implementations <em>MAY</em> also choose to override one/more of the following methods as needed:
     * </p>
     * <ul>
     *     <li>{@link #defaultColumnFamilyOptions()} to provide alternative default {@link ColumnFamilyOptions} used in obtaining {@link ColumnFamilyHandle}'s.</li>
     *     <li>{@link #createDefaultOptions()} to customise the default options used in creating/opening the RocksDB database.</li>
     *     <li>{@link #createDefaultTransactionOptions()} to customise the transaction options used in accessing the RocksDB database transactionally.</li>
     *     <li>{@link #prepareCounters()} if they want to use one/more {@link RocksDBCounter}'s as part of their storage implementation.</li>
     * </ul>
     *
     * @param dbDir Database directory
     * @throws IOException      Thrown if the directory is not accessible
     * @throws RocksDBException Thrown if the RocksDB storage cannot be initialised for any reason
     */
    public AbstractRocksDBStorage(File dbDir) throws IOException, RocksDBException {
        // Load the native library
        RocksDB.loadLibrary();

        // Prepare column family descriptors
        ColumnFamilyOptions cfOptions = defaultColumnFamilyOptions();
        List<ColumnFamilyDescriptor> cfDescriptors = prepareColumnFamilyDescriptors(cfOptions);
        List<ColumnFamilyHandle> cfHandles = new ArrayList<>();

        // 2. Open RocksDB with Options
        this.options = createDefaultOptions();
        this.transactionOptions = createDefaultTransactionOptions();
        Files.createDirectories(dbDir.toPath());
        DBOptions dbOptions = new DBOptions(this.options);
        try {
            this.db = TransactionDB.open(dbOptions, this.transactionOptions, dbDir.getAbsolutePath(), cfDescriptors,
                                         cfHandles);
        } catch (RocksDBException e) {
            cfDescriptors.forEach(cf -> cf.getOptions().close());
            throw e;
        }

        // 3. Obtain our handles for each column family
        this.columnFamilyHandles = new HashMap<>();
        for (int i = 0; i < cfDescriptors.size(); i++) {
            String name = new String(cfDescriptors.get(i).getName(), StandardCharsets.UTF_8);
            this.columnFamilyHandles.put(name, cfHandles.get(i));
        }

        // 4. Prepare any counters we need
        this.counters = this.prepareCounters();
    }

    /**
     * Creates the default transaction options used for the RocksDB database
     *
     * @return Default transaction options
     */
    protected TransactionDBOptions createDefaultTransactionOptions() {
        return new TransactionDBOptions();
    }

    /**
     * Creates the default options for creating/opening the RocksDB database
     *
     * @return Default options for creating/opening the database
     */
    @SuppressWarnings("resource")
    protected Options createDefaultOptions() {
        return new Options().setCreateIfMissing(true).setCreateMissingColumnFamilies(true);
    }

    /**
     * Prepares any {@link RocksDBCounter}'s that the storage wants to use
     * <p>
     * A {@link RocksDBCounter} is a convenience abstraction for implementing an atomic counter backed by persistent
     * state in the underlying RocksDB database
     * </p>
     * <p>
     * Derived storage implementations should override this if they wish to use counters, they may call the
     * {@link #createCounter(byte[], String)} method to create one/more counters and place those into the returned map.
     * Counter instances can be later retrieved via the {@link #getCounter(byte[])} method.
     * </p>
     *
     * @return Map of names to counters
     * @throws RocksDBException Thrown if counters cannot be prepared successfully
     */
    protected Map<String, RocksDBCounter> prepareCounters() throws RocksDBException {
        return Collections.emptyMap();
    }

    /**
     * Prepares the column family descriptors that will be used to obtain {@link ColumnFamilyHandle}'s for use in
     * interacting with the database
     *
     * @param cfOptions Default column family options as supplied by {@link #defaultColumnFamilyOptions()}
     * @return List of column family descriptors
     */
    protected abstract List<ColumnFamilyDescriptor> prepareColumnFamilyDescriptors(ColumnFamilyOptions cfOptions);

    /**
     * Creates the default column family options used when accessing column families
     *
     * @return Default column family options
     */
    protected ColumnFamilyOptions defaultColumnFamilyOptions() {
        return new ColumnFamilyOptions();
    }

    /**
     * Creates the default {@link WriteOptions} used when {@link #begin()} is used to begin a transaction
     *
     * @return Default write options
     */
    protected WriteOptions defaultWriteOptions() {
        return new WriteOptions();
    }

    /**
     * Creates the default {@link ReadOptions} used when {@link #begin()} is used to begin a transaction
     *
     * @return Default read options
     */
    protected ReadOptions defaultReadOptions() {
        return new ReadOptions();
    }

    @Override
    protected final void closeInternal() {
        try {
            for (final ColumnFamilyHandle cfHandle : columnFamilyHandles.values()) {
                cfHandle.close();
            }
            db.close();
            options.close();
            transactionOptions.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to close RocksDB resources", e);
        }
    }

    /**
     * Converts a long into a byte sequence for storage in RocksDB
     *
     * @param x Long
     * @return Bytes
     */
    protected static byte[] longToBytes(long x) {
        return ByteBuffer.allocate(Long.BYTES).putLong(x).array();
    }

    /**
     * Converts a byte sequence into a long after retrieval from RocksDB
     *
     * @param bytes Bytes
     * @return Long
     */
    protected static long bytesToLong(byte[] bytes) {
        if (bytes == null || bytes.length != Long.BYTES) {
            throw new IllegalArgumentException("Byte array must be " + Long.BYTES + " bytes long to represent a long.");
        }
        return ByteBuffer.wrap(bytes).getLong();
    }

    /**
     * Gets a column family that was previously defined via {@link #prepareColumnFamilyDescriptors(ColumnFamilyOptions)}
     * and registered during object construction
     *
     * @param cfNameBytes Column family name bytes
     * @return Column Family Handle, or {@code null} if no such handle registered
     */
    protected final ColumnFamilyHandle getHandle(byte[] cfNameBytes) {
        String cfName = new String(cfNameBytes, StandardCharsets.UTF_8);
        return columnFamilyHandles.get(cfName);
    }

    /**
     * Gets a counter that was previously created and registered by {@link #prepareCounters()}
     *
     * @param counterNameBytes Counter name bytes
     * @return Counter, or {@code null} if no such counter registered
     */
    protected final RocksDBCounter getCounter(byte[] counterNameBytes) {
        return getCounter(new String(counterNameBytes, StandardCharsets.UTF_8));
    }

    /**
     * Gets a counter that was previously created and registered by {@link #prepareCounters()}
     *
     * @param counterName Counter name
     * @return Counter, or {@code null} if no such counter registered
     */
    protected final RocksDBCounter getCounter(String counterName) {
        return counters.get(counterName);
    }

    /**
     * Begins a new transaction with default read and write options
     *
     * @return New transaction
     */
    protected final TransactionContext begin() {
        return this.begin(defaultReadOptions(), defaultWriteOptions());
    }

    /**
     * Begins a new transaction with the given read and write options
     *
     * @param readOptions  Read options
     * @param writeOptions Write options
     * @return New transaction
     */
    protected final TransactionContext begin(ReadOptions readOptions, WriteOptions writeOptions) {
        return new ShortLivedTransactionContext(this.db, readOptions, writeOptions);
    }

    /**
     * Create a counter
     *
     * @param countersColumnFamilyName Name of the column family in which the counter should be stored
     * @param counterKey               Key for the counter within the column family, using different keys allows for
     *                                 many counters to be stored in a single column family if needed
     * @return Counter
     * @throws RocksDBException Thrown if the counter cannot be synchronised with the underlying database
     */
    protected final RocksDBCounter createCounter(byte[] countersColumnFamilyName, String counterKey) throws RocksDBException {
        return new RocksDBCounter(this.db, this.getHandle(countersColumnFamilyName), counterKey);
    }
}
