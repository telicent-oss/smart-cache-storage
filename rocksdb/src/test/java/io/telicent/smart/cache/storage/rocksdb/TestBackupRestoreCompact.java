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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;

import static org.testng.Assert.*;


public class TestBackupRestoreCompact {

    File tempDir;

    private RocksDBStorageForTest storage;
    private File dbDir;
    private File backupDir;

    @BeforeMethod
    void setup() throws Exception {
        tempDir = Files.createTempDirectory("test-").toFile();
        dbDir = new File(tempDir, "db");
        backupDir = new File(tempDir, "backups");
        storage = new RocksDBStorageForTest(dbDir);
    }

    @AfterMethod
    void teardown() throws IOException {
        if (storage != null && !storage.isClosed()) {
            storage.close();
        }
        Files.walk(tempDir.toPath())
             .sorted(Comparator.reverseOrder())
             .forEach(path -> {
                 try {
                     Files.delete(path);
                 } catch (Exception e) {
                     // ignore
                 }
             });
    }

    @Test
    void testBackupAndRestore() throws Exception {
        // Given
        storage.put("key1".getBytes(), "value1".getBytes());
        storage.put("key2".getBytes(), "value2".getBytes());
        assertEquals(2, storage.count());

        BackupConfig backupConfig = BackupConfig.builder()
                                                .name("test-backup")
                                                .backupDir(backupDir)
                                                .build();
        // When
        BackupStatus backupStatus = storage.backup(backupConfig);
        // Then
        assertTrue(backupStatus.isSuccess(), "Backup should succeed");
        assertNotNull(backupStatus.getBackupId(), "Backup ID should not be null");
        assertTrue(backupStatus.getBytesBackedUp() > 0, "Should have backed up some bytes");
        assertNotNull(backupStatus.getStartTime(), "Backup should have a start time");

        // Given
        storage.put("key3".getBytes(), "value3".getBytes());
        assertEquals(3, storage.count());
        assertEquals("value3".getBytes(), storage.get("key3".getBytes()));

        storage.close();

        RestoreConfig restoreConfig = RestoreConfig.builder()
                                                   .backupDir(backupDir)
                                                   .build();
        // When
        RestoreStatus restoreStatus = storage.restore(restoreConfig);
        // Then
        assertTrue(restoreStatus.isSuccess(), "Restore should succeed");
        assertNotNull(restoreStatus.getBackupId(), "Restore should have backup ID");
        assertTrue(restoreStatus.getBytesRestored() > 0, "Should have restored some bytes");

        storage = new RocksDBStorageForTest(dbDir);

        assertEquals("value1".getBytes(), storage.get("key1".getBytes()));
        assertEquals("value2".getBytes(), storage.get("key2".getBytes()));
        assertNull(storage.get("key3".getBytes()), "key3 should not exist after restore");
        assertEquals(2, storage.count(), "Should have 2 keys after restore");

        assertEquals(new String(storage.get("key1".getBytes())), "value1");
        assertEquals(new String(storage.get("key2".getBytes())), "value2");
    }

    @Test
    void testCompaction() throws Exception {
        // Given
        for (int i = 0; i < 1000; i++) {
            storage.put(("key" + i).getBytes(), new byte[1024]);
        }
        long countBefore = storage.count();
        assertEquals(1000, countBefore);

        for (int i = 0; i < 500; i++) {
            storage.delete(("key" + i).getBytes());
        }
        assertEquals(500, storage.count());

        // When
        CompactStatus status = storage.compact();
        // Then
        assertNotNull(status, "Compact status should not be null");
        assertTrue(status.getSizeBefore() >= 0, "Size before should be non-negative");
        assertTrue(status.getSizeAfter() >= 0, "Size after should be non-negative");
        assertTrue(status.getSizeAfter() <= status.getSizeBefore(),
                   "Size after compaction should be <= size before");
        assertNotNull(status.getStartTime(), "Compact status should have a start time");
        assertNotNull(status.getEndTime(), "Compact status should have an end time");
    }

    @Test
    void testCompactionMoreData() throws Exception {
        // Given
        int numKeys = 10_000;
        int valueSize = 10_240;
        for (int i = 0; i < numKeys; i++) {
            storage.put(("key" + i).getBytes(), new byte[valueSize]);
        }

        // force a flush to ensure data is in SST files, not just memtable
        storage.flush();
        long countBefore = storage.count();
        assertEquals(numKeys, countBefore);

        // delete half the data to create waste
        int deleteCount = numKeys / 2;
        for (int i = 0; i < deleteCount; i++) {
            storage.delete(("key" + i).getBytes());
        }
        assertEquals(numKeys - deleteCount, storage.count());

        // force another flush to write tombstones to SST files
        storage.flush();

        // When
        CompactStatus status = storage.compact();

        // Then
        assertNotNull(status, "Compact status should not be null");
        assertTrue(status.getSizeBefore() > 0, "Size before should be positive");
        assertTrue(status.getSizeAfter() > 0, "Size after should be positive");
        assertTrue(status.getSizeAfter() <= status.getSizeBefore(),
                   String.format("Size after compaction (%d) should be <= size before (%d)",
                                 status.getSizeAfter(), status.getSizeBefore()));

        // verify space was reclaimed (at least 10% of the deleted data)
        long expectedReclaimed = (deleteCount * valueSize) / 10;
        assertTrue(status.getReclaimedBytes() >= expectedReclaimed || status.getSizeAfter() < status.getSizeBefore(),
                   String.format("Should have reclaimed some space. Before: %d, After: %d, Reclaimed: %d",
                                 status.getSizeBefore(), status.getSizeAfter(), status.getReclaimedBytes()));
        assertNotNull(status.getStartTime(), "Compact status should have a start time");
        assertNotNull(status.getEndTime(), "Compact status should have an end time");
    }

    @Test(expectedExceptions = BackupException.class,
            expectedExceptionsMessageRegExp = ".*Backup directory must be specified.*")
    public void testBackupFailsWhenDirectoryNotSpecified() {
        BackupConfig invalidConfig = BackupConfig.builder()
                                                 .name("invalid")
                                                 .build();

        storage.backup(invalidConfig);
    }

    @Test(expectedExceptions = RestoreException.class,
            expectedExceptionsMessageRegExp = ".*Backup directory must be specified.*")
    public void testRestoreFailsWhenDirectoryNotSpecified() {
        RestoreConfig invalidConfig = RestoreConfig.builder()
                                                   .build();

        storage.restore(invalidConfig);
    }

    @Test(expectedExceptions = RestoreException.class,
            expectedExceptionsMessageRegExp = ".*Database must be closed before restore operation.*")
    public void testRestoreFailsWhenDbNotClosed() {
        RestoreConfig config = RestoreConfig.builder()
                                            .backupDir(backupDir)
                                            .build();

        storage.restore(config);
    }

    @Test
    void testBackupStatusContainsCorrectMetadata() throws Exception {
        // Given
        storage.put("test".getBytes(), "data".getBytes());

        BackupConfig config = BackupConfig.builder()
                                          .name("metadata-test")
                                          .backupDir(backupDir)
                                          .build();
        // When
        BackupStatus status = storage.backup(config);
        // Then
        assertTrue(status.isSuccess());
        assertEquals("1", status.getBackupId());
        assertFalse(status.getErrorMessage().isPresent());
    }

    @Test
    void testMultipleBackups() throws Exception {
        // Given
        storage.put("key1".getBytes(), "value1".getBytes());
        BackupConfig config1 = BackupConfig.builder()
                                           .name("backup-1")
                                           .backupDir(backupDir)
                                           .build();
        // When
        BackupStatus status1 = storage.backup(config1);
        assertTrue(status1.isSuccess());

        storage.put("key2".getBytes(), "value2".getBytes());
        BackupConfig config2 = BackupConfig.builder()
                                           .name("backup-2")
                                           .backupDir(backupDir)
                                           .build();
        BackupStatus status2 = storage.backup(config2);

        // Then
        assertTrue(status2.isSuccess());
        assertNotEquals(status1.getBackupId(), status2.getBackupId());
    }

    @Test
    public void testListBackups() throws Exception {
        // Given
        storage.put("key1".getBytes(), "value1".getBytes());
        BackupConfig config1 = BackupConfig.builder()
                                           .name("backup-1")
                                           .backupDir(backupDir)
                                           .build();
        storage.backup(config1);

        storage.put("key2".getBytes(), "value2".getBytes());
        BackupConfig config2 = BackupConfig.builder()
                                           .name("backup-2")
                                           .backupDir(backupDir)
                                           .build();
        storage.backup(config2);

        // When
        List<BackupDetails> backups = storage.listBackups(backupDir);

        // Then
        assertEquals(backups.size(), 2);
        assertEquals(backups.get(0).getBackupId(), "1");
        assertEquals(backups.get(1).getBackupId(), "2");
        assertTrue(backups.get(0).getSizeBytes() > 0);
    }

    @Test
    public void testRestoreSpecificBackup() throws Exception {
        // Given
        storage.put("key1".getBytes(), "value1".getBytes());
        BackupStatus backup1 = storage.backup(BackupConfig.builder()
                                                          .name("backup-1")
                                                          .backupDir(backupDir)
                                                          .build());

        storage.put("key2".getBytes(), "value2".getBytes());
        BackupStatus backup2 = storage.backup(BackupConfig.builder()
                                                          .name("backup-2")
                                                          .backupDir(backupDir)
                                                          .build());
        storage.close();

        // When
        RestoreConfig restoreConfig = RestoreConfig.builder()
                                                   .backupId(backup1.getBackupId())
                                                   .backupDir(backupDir)
                                                   .build();

        storage.restore(restoreConfig);
        storage = new RocksDBStorageForTest(dbDir);

        // Then
        assertNotNull(storage.get("key1".getBytes()));
        assertNull(storage.get("key2".getBytes()));
    }

    @Test
    public void testDeleteBackup() {
        // given
        BackupStatus backup1 = storage.backup(BackupConfig.builder()
                                                          .name("to-delete")
                                                          .backupDir(backupDir)
                                                          .build());
        assertEquals(storage.listBackups(backupDir).size(), 1);

        //When
        storage.deleteBackup(backupDir, backup1.getBackupId());

        //Then
        assertEquals(storage.listBackups(backupDir).size(), 0);
    }

    @Test(expectedExceptions = BackupException.class,
            expectedExceptionsMessageRegExp = ".*Failed to delete backup.*")
    public void testDeleteBackupFailed() {
        BackupStatus backup1 = storage.backup(BackupConfig.builder()
                                                          .name("to-delete")
                                                          .backupDir(backupDir)
                                                          .build());

        assertEquals(storage.listBackups(backupDir).size(), 1);
        storage.deleteBackup(backupDir, backup1.getBackupId());
        assertEquals(storage.listBackups(backupDir).size(), 0);
        storage.deleteBackup(backupDir, backup1.getBackupId());
    }
}
