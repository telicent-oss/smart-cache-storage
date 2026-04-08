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
import org.rocksdb.BackupInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An example implementation of the BackupRestoreCapable and CompactCapable interfaces.
 */
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
        descriptors.add(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOptions));
        return descriptors;
    }

    @Override
    public BackupStatus backup(BackupConfig config) throws BackupException {
        ensureNotClosed();
        if (config.getBackupDir() == null) {
            throw new BackupException("Backup directory must be specified for RocksDB backups");
        }
        Instant startTime = Instant.now();
        try {
            File backupDir = config.getBackupDir();
            Files.createDirectories(backupDir.toPath());

            try (BackupEngineOptions backupOptions = new BackupEngineOptions(backupDir.getAbsolutePath());
                 BackupEngine backupEngine = BackupEngine.open(Env.getDefault(), backupOptions)) {

                backupEngine.createNewBackup(getDb(), true);

                List<BackupInfo> backupInfos = backupEngine.getBackupInfo();
                BackupInfo latestBackup = backupInfos.getLast();

                Instant endTime = Instant.now();

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
                BackupInfo restoredBackup =config.getBackupId() != null
                                           ? backupInfos.stream()
                                                        .filter(b -> String.valueOf(b.backupId()).equals(config.getBackupId()))
                                                        .findFirst()
                                                        .orElseThrow(() -> new RestoreException("Backup not found: " + config.getBackupId()))
                                           : backupInfos.getLast();

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
            Instant startTime = Instant.now();
            long sizeBefore = estimateSize();
            try (FlushOptions flushOptions = new FlushOptions().setWaitForFlush(true)) {
                getTransactionDB().flush(flushOptions);
            }
            getTransactionDB().compactRange(getDefaultHandle());
            Instant endTime = Instant.now();
            long sizeAfter = estimateSize();

            return new CompactStatus(sizeBefore, sizeAfter, sizeBefore - sizeAfter, startTime, endTime);
        } catch (RocksDBException e) {
            throw new CompactException("Failed to compact database: " + e.getMessage(), e);
        }
    }

    @Override
    public List<BackupDetails> listBackups(File backupDir) throws BackupException {
        try (BackupEngineOptions backupOptions = new BackupEngineOptions(backupDir.getAbsolutePath());
             BackupEngine backupEngine = BackupEngine.open(Env.getDefault(), backupOptions)) {

            return backupEngine.getBackupInfo().stream()
                               .map(rocksBackup -> new io.telicent.smart.cache.storage.BackupDetails(
                                       Optional.empty(),
                                       String.valueOf(rocksBackup.backupId()),
                                       Optional.empty(),
                                       Optional.ofNullable(Instant.ofEpochSecond(rocksBackup.timestamp())),
                                       rocksBackup.size()
                               ))
                               .collect(Collectors.toList());

        } catch (RocksDBException e) {
            throw new BackupException("Failed to list backups: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteBackup(File backupDir, String backupId) throws BackupException {
        try (BackupEngineOptions backupOptions = new BackupEngineOptions(backupDir.getAbsolutePath());
             BackupEngine backupEngine = BackupEngine.open(Env.getDefault(), backupOptions)) {

            int id = Integer.parseInt(backupId);
            backupEngine.deleteBackup(id);

        } catch (RocksDBException | NumberFormatException e) {
            throw new BackupException("Failed to delete backup " + backupId + ": " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to estimate database size
     */
private long estimateSize() throws CompactException {
      try (Stream<Path> paths = Files.walk(dbDir.toPath())) {
          return paths
                  .filter(Files::isRegularFile)
                  .mapToLong(path -> {
                      try {
                          return Files.size(path);
                      } catch (IOException e) {
                          // Skip files we fail to stat rather than failing the whole estimate
                          return 0L;
                      }
                  })
                  .sum();
      } catch (IOException e) {
          throw new CompactException("Failed to estimate database size: " + e.getMessage(), e);
      }
  }

    /**
     * Accessor for the underlying RocksDB instance (for backup/restore operations)
     */
    private TransactionDB getDb() {
        return this.getTransactionDB();
    }

    public void put(byte[] key, byte[] value) throws RocksDBException {
        try (TransactionContext context = begin()) {
            context.put(getDefaultHandle(), key, value);
            context.commit();
        }
    }

    public byte[] get(byte[] key) throws RocksDBException {
        try (TransactionContext context = begin()) {
            byte[] value = context.get(getDefaultHandle(), key);
            return (value != null && value.length == 0) ? null : value;
        }
    }

    public void delete(byte[] key) throws RocksDBException {
        try (TransactionContext context = begin()) {
            getTransactionDB().delete(getDefaultHandle(), key);
        }
    }

    public long count() {
        try (TransactionContext context = begin()) {
            return context.count(getDefaultHandle());
        }
    }

    public void flush() throws RocksDBException {
        try (FlushOptions flushOptions = new FlushOptions().setWaitForFlush(true)) {
            getTransactionDB().flush(flushOptions);
        }
    }
}
