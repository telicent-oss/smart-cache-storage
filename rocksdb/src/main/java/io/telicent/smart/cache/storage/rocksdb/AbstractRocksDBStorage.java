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

import io.telicent.smart.cache.storage.*;
import org.apache.commons.lang3.StringUtils;
import org.rocksdb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract implementation of RocksDB backed storage, see {@link #AbstractRocksDBStorage(File)} for customisation
 * options.
 * <p>
 * The primary usage pattern for this in derived implementations is as follows:
 * </p>
 * <pre>
 *   // Start a fresh transaction
 *   try (TransactionContext context = this.begin()) {
 *     // Get a column family handle
 *     ColumnFamilyHandle cfHandle = this.getHandle(NAME_OF_COLUMN_FAMILY);
 *
 *     // Perform some operations
 *     context.put(cfHandle, key, value);
 *
 *     // Commit the transaction
 *     context.commit();
 *   }
 * </pre>
 */
public abstract class AbstractRocksDBStorage extends AbstractStorage implements BackupRestoreCapable,
        CompactCapable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRocksDBStorage.class);
    private TransactionDB db;
    private Options options;
    private TransactionDBOptions transactionOptions;
    private final Map<String, ColumnFamilyHandle> columnFamilyHandles;
    private final Map<String, RocksDBCounter> counters;
    private final ThreadLocal<NestedTransactionContext> nestedTransactions = ThreadLocal.withInitial(() -> null);

    protected final File dbDir;

    protected final TransactionDB getTransactionDB() {
        return this.db;
    }

    public final Map<String, ColumnFamilyHandle> getColumnFamilyHandles() {
        return this.columnFamilyHandles;
    }

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
        this.dbDir = dbDir;
        Objects.requireNonNull(dbDir, "Database Directory cannot be null");

        // Load the native library
        RocksDB.loadLibrary();

        // Prepare column family descriptors
        ColumnFamilyOptions cfOptions = defaultColumnFamilyOptions();
        List<ColumnFamilyDescriptor> cfDescriptors = prepareColumnFamilyDescriptors(cfOptions);
        LOGGER.info("Prepared {} column family descriptors", cfDescriptors.size());
        List<ColumnFamilyHandle> cfHandles = new ArrayList<>();

        // 2. Open RocksDB with Options
        this.options = createDefaultOptions();
        this.transactionOptions = createDefaultTransactionOptions();
        Files.createDirectories(dbDir.toPath());
        DBOptions dbOptions = new DBOptions(this.options);
        try {
            this.db = TransactionDB.open(dbOptions, this.transactionOptions, dbDir.getAbsolutePath(), cfDescriptors,
                                         cfHandles);
            LOGGER.info("Successfully opened RocksDB database at {}", dbDir.getAbsolutePath());
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
        if (!this.counters.isEmpty()) {
            LOGGER.info("Prepared {} counters ({})", this.counters.size(),
                        StringUtils.join(this.counters.keySet(), ", "));
        }
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
     * <p>
     * The default implementation uses the recommended settings from <a
     * href="https://github.com/facebook/rocksdb/wiki/Setup-Options-and-Basic-Tuning">Setup-Options-and-Basic-Tuning</a>
     * </p>
     *
     * @return Default options for creating/opening the database
     */
    protected Options createDefaultOptions() {
        // Always want to create the database and column families if missing, otherwise we can't open a new blank
        // location as a database
        Options options =
                new Options().setCreateIfMissing(true).setCreateMissingColumnFamilies(true);

        // Apply all the official RocksDB recommendations
        LOGGER.debug("Configuring RocksDB options from defaults to recommended:");
        LOGGER.debug("maxBackgroundJobs {} to {}", options.maxBackgroundJobs(), 6);
        options.setMaxBackgroundJobs(6);
        LOGGER.debug("bytesPerSync {} to {}", options.bytesPerSync(), 1048576);
        options.setBytesPerSync(1048576);
        LOGGER.debug("compactionPriority {} to {}", options.compactionPriority(),
                     CompactionPriority.MinOverlappingRatio);
        options.setCompactionPriority(CompactionPriority.MinOverlappingRatio);

        // Table level configuration
        var tableOptions = new BlockBasedTableConfig();
        LOGGER.debug("blockSize {} to {}", tableOptions.blockSize(), 16 * 1024);
        tableOptions.setBlockSize(16 * 1024);
        LOGGER.debug("cacheIndexAndFilterBlocks {} to {}", tableOptions.cacheIndexAndFilterBlocks(), true);
        tableOptions.setCacheIndexAndFilterBlocks(true);
        LOGGER.debug("pinL0FilterAndIndexBlocksInCache {} to {}", tableOptions.pinL0FilterAndIndexBlocksInCache(),
                     true);
        tableOptions.setPinL0FilterAndIndexBlocksInCache(true);
        var newFilterPolicy = new BloomFilter(10.0);
        LOGGER.debug("filterPolicy {} to {}", tableOptions.filterPolicy(), newFilterPolicy);
        tableOptions.setFilterPolicy(newFilterPolicy);
        LOGGER.debug("formatVersion {} to {}", tableOptions.formatVersion(), 5);
        tableOptions.setFormatVersion(5);
        options.setTableFormatConfig(tableOptions);

        return options;
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
     * <p>
     * <strong>NB: </strong> RocksDB requires that this list <strong>MUST</strong> include the default column family
     * whose name is given by the {@link RocksDB#DEFAULT_COLUMN_FAMILY} constant.  If this is not included in the
     * returned list RocksDB will fail to create/open the database.
     * </p>
     *
     * @param cfOptions Default column family options as supplied by {@link #defaultColumnFamilyOptions()}
     * @return List of column family descriptors
     */
    protected abstract List<ColumnFamilyDescriptor> prepareColumnFamilyDescriptors(ColumnFamilyOptions cfOptions);

    /**
     * Creates the default column family options used when accessing column families
     * <p>
     * The default implementation uses the recommended settings from <a
     * href="https://github.com/facebook/rocksdb/wiki/Setup-Options-and-Basic-Tuning">Setup-Options-and-Basic-Tuning</a>
     * </p>
     *
     * @return Default column family options
     */
    @SuppressWarnings("resource")
    protected ColumnFamilyOptions defaultColumnFamilyOptions() {
        return new ColumnFamilyOptions().setLevelCompactionDynamicLevelBytes(true)
                                        .setCompressionType(CompressionType.LZ4_COMPRESSION)
                                        .setBottommostCompressionType(CompressionType.ZLIB_COMPRESSION);
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
            // Ensure any updated counters are persisted at close time
            if (!this.counters.isEmpty()) {
                LOGGER.info("Persisting counters to ensure their values are up to date...");
                try (TransactionContext context = this.begin()) {
                    for (Map.Entry<String, RocksDBCounter> counter : this.counters.entrySet()) {
                        counter.getValue().update(context);
                        LOGGER.info("Counter {} persisted with value {}", counter.getKey(), counter.getValue().get());
                    }
                    context.commit();
                    LOGGER.info("Persisted all counters successfully");
                } catch (Throwable e) {
                    LOGGER.warn("Unexpected error persisting counters: ", e);
                }
            }

            for (final ColumnFamilyHandle cfHandle : columnFamilyHandles.values()) {
                cfHandle.close();
            }
            db.close();
            options.close();
            transactionOptions.close();
        } catch (Throwable e) {
            throw new RuntimeException("Failed to close RocksDB resources", e);
        }
    }

    protected final void openInternal() {
        try {
            this.options = createDefaultOptions();
            this.transactionOptions = createDefaultTransactionOptions();

            columnFamilyHandles.clear();

            List<ColumnFamilyHandle> cfHandles = new ArrayList<>();

            try (DBOptions dbOptions = new DBOptions(this.options)) {
                try (ColumnFamilyOptions cfOptions = defaultColumnFamilyOptions()) {
                    List<ColumnFamilyDescriptor> cfDescriptors = prepareColumnFamilyDescriptors(cfOptions);

                    this.db = TransactionDB.open(dbOptions, this.transactionOptions,
                                                 dbDir.getAbsolutePath(), cfDescriptors, cfHandles);

                    for (int i = 0; i < cfDescriptors.size(); i++) {
                        String cfName = new String(cfDescriptors.get(i).getName(), StandardCharsets.UTF_8);
                        columnFamilyHandles.put(cfName, cfHandles.get(i));
                    }
                }
            }
            LOGGER.info("RocksDB opened successfully at {}", dbDir.getAbsolutePath());
        } catch (RocksDBException e) {
            throw new RuntimeException("Failed to open RocksDB", e);
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
     * Gets a column family handler that was previously defined via
     * {@link #prepareColumnFamilyDescriptors(ColumnFamilyOptions)} and registered during object construction
     *
     * @param cfNameBytes Column family name bytes
     * @return Column Family Handle, or {@code null} if no such handle registered
     */
    protected final ColumnFamilyHandle getHandle(byte[] cfNameBytes) {
        String cfName = new String(cfNameBytes, StandardCharsets.UTF_8);
        return columnFamilyHandles.get(cfName);
    }

    /**
     * Gets the column family handle for the default column family
     *
     * @return Default column family handle, as RocksDB requires that we always register the default column family this
     * is guaranteed to never be {@code null}
     */
    protected ColumnFamilyHandle getDefaultHandle() {
        return this.getHandle(RocksDB.DEFAULT_COLUMN_FAMILY);
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
     * <p>
     * If this is called within the context of a pre-existing nested transaction (created by a call to
     * {@link #beginNested()}) then the returned transaction will be a nested transaction that shares the longer running
     * transaction context.
     * </p>
     *
     * @return New transaction
     */
    protected final TransactionContext begin() {
        return this.begin(defaultReadOptions(), defaultWriteOptions());
    }

    /**
     * Begins a new transaction with the given read and write options
     * <p>
     * If this is called within the context of a pre-existing nested transaction (created by a call to
     * {@link #beginNested()}) then the returned transaction will be a nested transaction that shares the longer running
     * transaction context.
     * </p>
     *
     * @param readOptions  Read options
     * @param writeOptions Write options
     * @return New transaction
     */
    protected final TransactionContext begin(ReadOptions readOptions, WriteOptions writeOptions) {
        ensureNotClosed();
        NestedTransactionContext context = this.nestedTransactions.get();
        if (context != null && context.isActive()) {
            return context.increment();
        } else {
            return new ShortLivedTransactionContext(this.db, readOptions, writeOptions);
        }
    }

    /**
     * Begins a new transaction that may be nested, or increments the nested of the pre-existing nested transaction
     *
     * @return Nested transaction
     */
    protected final TransactionContext beginNested() {
        return this.beginNested(defaultReadOptions(), defaultWriteOptions());
    }

    /**
     * Begins a new transaction that may be nested, or increments the nested of the existing nested transaction, the
     * read and write options are only honoured if this is the top level transaction
     *
     * @param readOptions  Read options
     * @param writeOptions Write options
     * @return Nested transaction
     */
    protected final TransactionContext beginNested(ReadOptions readOptions, WriteOptions writeOptions) {
        ensureNotClosed();
        NestedTransactionContext context = this.nestedTransactions.get();
        if (context == null || !context.isActive()) {
            // No prior nested transaction, or previous one has been closed, create a fresh one
            context = new NestedTransactionContext(this.db, readOptions, writeOptions);
            this.nestedTransactions.set(context);
            return context;
        } else {
            // Increment nesting on the existing active nested transaction
            return context.increment();
        }
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
    protected final RocksDBCounter createCounter(byte[] countersColumnFamilyName, String counterKey) throws
            RocksDBException {
        String cfName = new String(countersColumnFamilyName, StandardCharsets.UTF_8);
        return new RocksDBCounter(
                () -> this.db,
                () -> this.columnFamilyHandles.get(cfName),
                counterKey
        );
    }

    /**
     * Drops a column family
     *
     * @param handle Column family handle
     * @throws RocksDBException         Thrown if there is a problem dropping a column family
     * @throws NullPointerException     Thrown if no column family handle is provided
     * @throws IllegalArgumentException Thrown if there is an attempt to drop the default column family
     */
    @SuppressWarnings("resource")
    protected final void dropColumnFamily(ColumnFamilyHandle handle) throws RocksDBException {
        ensureNotClosed();
        Objects.requireNonNull(handle, "Must provide a valid column family handle to drop");
        if (Arrays.equals(handle.getName(), RocksDB.DEFAULT_COLUMN_FAMILY)) {
            throw new IllegalArgumentException("Cannot drop the default column family");
        }

        // Drop and then flush the column family (which actively deletes the data)
        String key = new String(handle.getName(), StandardCharsets.UTF_8);
        this.db.dropColumnFamily(handle);
        try (FlushOptions flushOptions = new FlushOptions().setWaitForFlush(true)) {
            this.db.flush(flushOptions);
        }

        // Close and remove it from our map of column families so we don't need to close it again later and we prevent
        // access to it via getHandle()
        handle.close();
        this.columnFamilyHandles.remove(key);
    }

    @Override
    public BackupStatus backup(BackupConfig config) throws BackupException {
        ensureNotClosed();
        if (config.getBackupLocation() == null) {
            throw new BackupException("Backup directory must be specified for RocksDB backups");
        }
        Instant startTime = Instant.now();
        LOGGER.info("Starting backup...");
        try {
            File backupDir = new File(config.getBackupLocation());
            Files.createDirectories(backupDir.toPath());

            try (BackupEngineOptions backupOptions = new BackupEngineOptions(backupDir.getAbsolutePath());
                 BackupEngine backupEngine = BackupEngine.open(Env.getDefault(), backupOptions)) {

                backupEngine.createNewBackup(getTransactionDB(), true);

                List<BackupInfo> backupInfos = backupEngine.getBackupInfo();
                BackupInfo latestBackup = backupInfos.getLast();

                Instant endTime = Instant.now();
                LOGGER.info("Backup {} created successfully", latestBackup.backupId());

                return BackupStatus.builder()
                                   .success(true)
                                   .backupId(String.valueOf(latestBackup.backupId()))
                                   .bytesBackedUp(latestBackup.size())
                                   .startTime(startTime)
                                   .endTime(endTime)
                                   .build();
            }
        } catch (RocksDBException | IOException e) {
            throw new BackupException("Failed to create backup: " + e.getMessage(), e);
        }
    }

    @Override
    public RestoreStatus restore(RestoreConfig config) throws RestoreException {
        if (config.getBackupLocation() == null) {
            throw new RestoreException("Backup directory must be specified for RocksDB restores");
        }

        try {
            // Close the storage first, this needs to happen before we set the restoring flag as otherwise logic in
            // closeInternal() will fail when it tries to persist the counters.  While this error is generally harmless
            // it does pollute the logs, and there is a failure scenario where the restore fails that leads to out of
            // date counters.
            // Since upon restore failure we try to reopen the storage as-is at which point the counter values would be
            // wrong if we didn't allow them to be persisted prior to attempting the restore
            closeInternal();

            this.restoring = true;
            LOGGER.info("Starting restore of {}", config.getBackupLocation());

            File backupDir = new File(config.getBackupLocation());
            if (!backupDir.exists()) {
                throw new RestoreException("Backup directory " + config.getBackupLocation() + " does not exist");
            }
            try (BackupEngineOptions backupOptions = new BackupEngineOptions(backupDir.getAbsolutePath());
                 BackupEngine backupEngine = BackupEngine.open(Env.getDefault(), backupOptions);
                 RestoreOptions restoreOptions = new RestoreOptions(false)) {

                if (config.getBackupId() != null) {
                    int backupId = Integer.parseInt(config.getBackupId());
                    backupEngine.restoreDbFromBackup(
                            backupId,
                            dbDir.getAbsolutePath(),
                            dbDir.getAbsolutePath(),
                            restoreOptions
                    );
                } else {
                    backupEngine.restoreDbFromLatestBackup(
                            dbDir.getAbsolutePath(),
                            dbDir.getAbsolutePath(),
                            restoreOptions
                    );
                }

                List<BackupInfo> backupInfos = backupEngine.getBackupInfo();
                BackupInfo restoredBackup = config.getBackupId() != null
                                            ? backupInfos.stream()
                                                         .filter(b -> String.valueOf(b.backupId())
                                                                            .equals(config.getBackupId()))
                                                         .findFirst()
                                                         .orElseThrow(() -> new RestoreException(
                                                                 "Backup not found: " + config.getBackupId()))
                                            : backupInfos.getLast();

                openInternal();

                // We have to resynchronise any counters after a restore operation as their in-memory values wouldn't be
                // in-sync with their backup values
                LOGGER.info("Resynchronising counters after restore...");
                for (Map.Entry<String, RocksDBCounter> counter : this.counters.entrySet()) {
                    long before = counter.getValue().get();
                    counter.getValue().sync();
                    LOGGER.info("Counter {} restored value from {} to {}", counter.getKey(), before,
                                counter.getValue().get());
                }

                LOGGER.info("Restored {} from backup {}", dbDir.getPath(), restoredBackup.backupId());
                return RestoreStatus.success(
                        String.valueOf(restoredBackup.backupId()),
                        restoredBackup.size()
                );
            }
        } catch (RocksDBException | RuntimeException e) {
            try {
                openInternal();
                this.closed = false;
            } catch (Throwable reopenError) {
                LOGGER.error("Failed to reopen store after failed restore", reopenError);
                this.closed = true;
            }
            throw new RestoreException("Failed to restore from backup: " + e.getMessage(), e);

        } finally {
            this.restoring = false;
        }
    }

    @Override
    public CompactStatus compact() throws CompactException {
        ensureNotClosed();
        try {
            Instant startTime = Instant.now();
            LOGGER.info("Starting compact operation...");
            try (FlushOptions flushOptions = new FlushOptions().setWaitForFlush(true)) {
                getTransactionDB().flush(flushOptions);
            }
            long sizeBefore = estimateSize();
            for (ColumnFamilyHandle handle : getAllColumnFamilyHandles()) {
                getTransactionDB().compactRange(handle);
                LOGGER.info("Compaction of ColumnFamily {} has been completed.", handle);
            }
            flush();
            Instant endTime = Instant.now();
            long sizeAfter = estimateSize();
            LOGGER.info("Compaction finished at {}, reclaimed {} bytes.", endTime, sizeBefore - sizeAfter);
            return new CompactStatus(sizeBefore, sizeAfter, sizeBefore - sizeAfter, startTime, endTime);
        } catch (RocksDBException e) {
            throw new CompactException("Failed to compact database: " + e.getMessage(), e);
        }
    }

    @Override
    public List<BackupDetails> listBackups(String backupDir) throws BackupException {
        try (BackupEngineOptions backupOptions = new BackupEngineOptions(backupDir);
             BackupEngine backupEngine = BackupEngine.open(Env.getDefault(), backupOptions)) {

            return backupEngine.getBackupInfo().stream()
                               .map(rocksBackup -> new BackupDetails(
                                       null,
                                       String.valueOf(rocksBackup.backupId()),
                                       null,
                                       Instant.ofEpochSecond(rocksBackup.timestamp()),
                                       rocksBackup.size()
                               ))
                               .collect(Collectors.toList());

        } catch (RocksDBException e) {
            throw new BackupException("Failed to list backups: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteBackup(String backupDir, String backupId) throws BackupException {
        try (BackupEngineOptions backupOptions = new BackupEngineOptions(backupDir);
             BackupEngine backupEngine = BackupEngine.open(Env.getDefault(), backupOptions)) {

            int id = Integer.parseInt(backupId);
            backupEngine.deleteBackup(id);
            LOGGER.info("Deleted backup {}", id);

        } catch (RocksDBException | NumberFormatException e) {
            throw new BackupException("Failed to delete backup " + backupId + ": " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to estimate database size
     */
    public long estimateSize() throws RocksDBException {
        long totalSize = 0;

        for (ColumnFamilyHandle handle : getAllColumnFamilyHandles()) {
            String sizeStr = getTransactionDB().getProperty(handle, "rocksdb.total-sst-files-size");
            if (sizeStr != null && !sizeStr.isEmpty()) {
                long size = Long.parseLong(sizeStr);
                totalSize += size;
                LOGGER.debug("ColumnFamily {} size: {}", new String(handle.getName()), size);
            }
        }
        return totalSize;
    }

    /**
     * Gets all the column family handles managed by this storage instance
     *
     * @return Collection of Column family handles
     */
    protected Collection<ColumnFamilyHandle> getAllColumnFamilyHandles() {
        return getColumnFamilyHandles().values();
    }

    /**
     * Flushes the column families to disk explicitly blocking until that has occurred
     *
     * @throws RocksDBException Thrown if the column families cannot be flushed for any reason
     */
    protected void flush() throws RocksDBException {
        try (FlushOptions flushOptions = new FlushOptions().setWaitForFlush(true)) {
            List<ColumnFamilyHandle> handles = new ArrayList<>(getAllColumnFamilyHandles());
            getTransactionDB().flush(flushOptions, handles);
        }
    }
}
