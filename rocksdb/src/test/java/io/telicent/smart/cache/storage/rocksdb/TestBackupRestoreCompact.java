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
             .sorted(Comparator.reverseOrder()) // delete children first
             .forEach(path -> {
                 try {
                     Files.delete(path);
                 } catch (Exception e) {
                     System.out.println(e.getMessage());
                 }
             });
    }

    @Test
    void testStorageImplementsInterfaces() {
        assertTrue(storage instanceof BackupRestoreCapable);
        assertTrue(storage instanceof CompactCapable);
    }

    @Test
    void testBackupAndRestore() throws Exception {
        // Add some data
        storage.put("key1".getBytes(), "value1".getBytes());
        storage.put("key2".getBytes(), "value2".getBytes());
        assertEquals(2, storage.count());

        // Create backup
        BackupConfig backupConfig = BackupConfig.builder()
                                                .name("test-backup")
                                                .backupDir(backupDir)
                                                .build();

        BackupStatus backupStatus = storage.backup(backupConfig);
        assertTrue(backupStatus.isSuccess(), "Backup should succeed");
        assertNotNull(backupStatus.getBackupId(), "Backup ID should not be null");
        assertTrue(backupStatus.getBytesBackedUp() > 0, "Should have backed up some bytes");
        assertNotNull(backupStatus.getTimestamp(), "Backup should have timestamp");

        // Modify data
        storage.put("key3".getBytes(), "value3".getBytes());
        assertEquals(3, storage.count());
        assertEquals("value3".getBytes(), storage.get("key3".getBytes()));

        // Close storage before restore
        storage.close();

        // Restore from backup
        //storage = new RocksDBStorageForTest(dbDir);

        RestoreConfig restoreConfig = RestoreConfig.builder()
                                                   .name("test-backup")
                                                   .backupDir(backupDir)
                                                   .build();

        RestoreStatus restoreStatus = storage.restore(restoreConfig);
        assertTrue(restoreStatus.isSuccess(), "Restore should succeed");
        assertNotNull(restoreStatus.getBackupId(), "Restore should have backup ID");
        assertTrue(restoreStatus.getBytesRestored() > 0, "Should have restored some bytes");

        // Verify data is restored (key3 should be gone)
        // Note: Need to close and reopen after restore for changes to take effect
        //storage.close();
        storage = new RocksDBStorageForTest(dbDir);

        assertEquals("value1".getBytes(), storage.get("key1".getBytes()));
        assertEquals("value2".getBytes(), storage.get("key2".getBytes()));
        assertNull(storage.get("key3".getBytes()), "key3 should not exist after restore");
        assertEquals(2, storage.count(), "Should have 2 keys after restore");

        // Verify content of restored data
        assertEquals(new String(storage.get("key1".getBytes())), "value1");
        assertEquals(new String(storage.get("key2".getBytes())), "value2");
    }

    @Test
    void testCompaction() throws Exception {
        // Add lots of data
        for (int i = 0; i < 1000; i++) {
            storage.put(("key" + i).getBytes(), new byte[1024]);
        }

        long countBefore = storage.count();
        assertEquals(1000, countBefore);

        // Delete half of it to create waste
        for (int i = 0; i < 500; i++) {
            storage.delete(("key" + i).getBytes());
        }

        assertEquals(500, storage.count());

        // Compact
        CompactStatus status = storage.compact();
        assertNotNull(status, "Compact status should not be null");
        assertTrue(status.getSizeBefore() >= 0, "Size before should be non-negative");
        assertTrue(status.getSizeAfter() >= 0, "Size after should be non-negative");
        //TODO
        //  Size after compaction should be <= size before expected [true] but found [false]
        assertTrue(status.getSizeAfter() <= status.getSizeBefore(),
                   "Size after compaction should be <= size before");
        assertNotNull(status.getTimestamp(), "Compact status should have timestamp");
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
                                                   .name("invalid")
                                                   .build();

        storage.restore(invalidConfig);
    }

    @Test
    void testBackupStatusContainsCorrectMetadata() throws Exception {
        storage.put("test".getBytes(), "data".getBytes());

        BackupConfig config = BackupConfig.builder()
                                          .name("metadata-test")
                                          .backupDir(backupDir)
                                          .build();

        BackupStatus status = storage.backup(config);

        assertTrue(status.isSuccess());
        assertEquals("1", status.getBackupId()); // First backup should have ID 1
        assertFalse(status.getErrorMessage().isPresent());
    }

    @Test
    void testMultipleBackups() throws Exception {
        // First backup
        storage.put("key1".getBytes(), "value1".getBytes());
        BackupConfig config1 = BackupConfig.builder()
                                           .name("backup-1")
                                           .backupDir(backupDir)
                                           .build();
        BackupStatus status1 = storage.backup(config1);
        assertTrue(status1.isSuccess());

        // Second backup with more data
        storage.put("key2".getBytes(), "value2".getBytes());
        BackupConfig config2 = BackupConfig.builder()
                                           .name("backup-2")
                                           .backupDir(backupDir)
                                           .build();
        BackupStatus status2 = storage.backup(config2);
        assertTrue(status2.isSuccess());

        // Second backup should have different ID
        assertNotEquals(status1.getBackupId(), status2.getBackupId());
    }
}
