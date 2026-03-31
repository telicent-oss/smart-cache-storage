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
import org.rocksdb.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class RocksDBStorageForTest extends AbstractRocksDBStorage
        implements BackupRestoreCapable, CompactCapable {

    private final File dbDir;

    public RocksDBStorageForTest(File dbDir) throws IOException, RocksDBException {
        super(dbDir);
        this.dbDir = dbDir;
    }

    @Override
    protected List<ColumnFamilyDescriptor> prepareColumnFamilyDescriptors(ColumnFamilyOptions cfOptions) {
        List<ColumnFamilyDescriptor> descriptors = new ArrayList<>();
        // RocksDB requires the default column family
        descriptors.add(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOptions));
        return descriptors;
    }

    @Override
    public BackupStatus backup(BackupConfig config) throws BackupException {
        ensureNotClosed();

        if (config.getBackupDir() == null) {
            throw new BackupException("Backup directory must be specified for RocksDB backups");
        }

        try {
            File backupDir = config.getBackupDir();
            Files.createDirectories(backupDir.toPath());

            // Use RocksDB's native BackupEngine
            try (BackupEngineOptions backupOptions = new BackupEngineOptions(backupDir.getAbsolutePath());
                 BackupEngine backupEngine = BackupEngine.open(Env.getDefault(), backupOptions)) {

                // Create the backup
                backupEngine.createNewBackup(getDb(), true);

                // Get backup info
                List<BackupInfo> backupInfos = backupEngine.getBackupInfo();
                BackupInfo latestBackup = backupInfos.get(backupInfos.size() - 1);

                return BackupStatus.success(
                        String.valueOf(latestBackup.backupId()),
                        latestBackup.size()
                );
            }
        } catch (RocksDBException | IOException e) {
            throw new BackupException("Failed to create backup: " + e.getMessage(), e);
        }
    }

    @Override
    public RestoreStatus restore(RestoreConfig config) throws RestoreException {
        //ensureNotClosed();

        if (config.getBackupDir() == null) {
            throw new RestoreException("Backup directory must be specified for RocksDB restores");
        }
        if (!isClosed()) {
            throw new RestoreException("Database must be closed before restore operation");
        }

        try {
            File backupDir = config.getBackupDir();

            try (BackupEngineOptions backupOptions = new BackupEngineOptions(backupDir.getAbsolutePath());
                 BackupEngine backupEngine = BackupEngine.open(Env.getDefault(), backupOptions);
                 RestoreOptions restoreOptions = new RestoreOptions(false)) {

                // Restore from the latest backup
                backupEngine.restoreDbFromLatestBackup(
                        dbDir.getAbsolutePath(),
                        dbDir.getAbsolutePath(),
                        restoreOptions
                );

                // Get info about the restored backup
                List<BackupInfo> backupInfos = backupEngine.getBackupInfo();
                BackupInfo restoredBackup = backupInfos.get(backupInfos.size() - 1);

                return RestoreStatus.success(
                        String.valueOf(restoredBackup.backupId()),
                        restoredBackup.size()
                );
            }
        } catch (RocksDBException e) {
            throw new RestoreException("Failed to restore from backup: " + e.getMessage(), e);
        }
    }

    @Override
    public CompactStatus compact() throws CompactException {
        ensureNotClosed();

        try {
            // Get database size before compaction
            long sizeBefore = estimateSize();

            // Compact the entire database (all column families)
            getDb().compactRange();

            // Get database size after compaction
            long sizeAfter = estimateSize();

            return new CompactStatus(sizeBefore, sizeAfter, sizeBefore - sizeAfter);
        } catch (RocksDBException e) {
            throw new CompactException("Failed to compact database: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to estimate database size
     */
    private long estimateSize() throws RocksDBException {
        String sizeStr = getDb().getProperty("rocksdb.total-sst-files-size");
        return sizeStr != null ? Long.parseLong(sizeStr) : 0L;
    }

    /**
     * Accessor for the underlying RocksDB instance (for backup/restore operations)
     */
    private TransactionDB getDb() {
        // You'll need to expose this from AbstractRocksDBStorage or use reflection
        // For now, assuming you add a protected getter
        return this.getTransactionDB();
    }

    // Simple test methods to validate the storage works
    public void put(byte[] key, byte[] value) throws RocksDBException {
        try (TransactionContext context = begin()) {
            context.put(getDefaultHandle(), key, value);
            context.commit();
        }
    }

    public byte[] get(byte[] key) throws RocksDBException {
        try (TransactionContext context = begin()) {
            byte[] value = context.get(getDefaultHandle(), key);
            // Treat empty arrays as deleted/null (tombstone pattern)
            return (value != null && value.length == 0) ? null : value;
        }
    }

    public void delete(byte[] key) throws RocksDBException {
        try (TransactionContext context = begin()) {
            // Put null or empty to delete - depends on your implementation
            getTransactionDB().delete(getDefaultHandle(), key);
        }
    }

    public long count() throws RocksDBException {
        try (TransactionContext context = begin()) {
            return context.count(getDefaultHandle());
        }
    }

    private Transaction getTransaction(TransactionContext context) {
        // Option 1: If you add getTransaction() to TransactionContext interface
        // return context.getTransaction();

        // Option 2: Workaround using reflection
        try {
            java.lang.reflect.Field field = context.getClass().getDeclaredField("transaction");
            field.setAccessible(true);
            return (Transaction) field.get(context);
        } catch (Exception e) {
            throw new RuntimeException("Could not access transaction field. " +
                                               "Consider adding 'Transaction getTransaction()' to TransactionContext interface", e);
        }
    }
}
