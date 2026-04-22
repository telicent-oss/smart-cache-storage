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
package io.telicent.smart.cache.storage.labels.rocksdb;

import io.telicent.smart.cache.storage.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.testng.Assert.*;

public class TestRocksDbLabelsStoreBackupRestoreCompact {
    File tempDir;
    private RocksDbLabelsStore store;
    private File dbDir;
    private String backupDir;

    @BeforeMethod
    void setup() throws Exception {
        tempDir = Files.createTempDirectory("test-labels-store-").toFile();
        dbDir = new File(tempDir, "db");
        backupDir = new File(tempDir, "backups").getAbsolutePath();
        store = new RocksDbLabelsStore(dbDir);
    }

    @AfterMethod
    void teardown() throws IOException {
        if (store != null) {
            try {
                if (!store.isClosed()) {
                    store.close();
                }
            } catch (Exception e) {
                // ignore - may already be closed/crashed
            }
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
        addLabels(2);
        assertEquals(2, store.keyCount());
        assertEquals(2, store.labelCount());

        long label1Id = store.idForLabel("label0".getBytes());
        long label2Id = store.idForLabel("label1".getBytes());

        BackupConfig backupConfig = BackupConfig.builder()
                                                .name("test-backup")
                                                .backupLocation(backupDir)
                                                .build();
        // When
        BackupStatus backupStatus = store.backup(backupConfig);
        // Then
        assertTrue(backupStatus.isSuccess(), "Backup should succeed");
        assertNotNull(backupStatus.getBackupId(), "Backup ID should not be null");
        assertTrue(backupStatus.getBytesBackedUp() > 0, "Should have backed up some bytes");
        assertNotNull(backupStatus.getStartTime(), "Backup should have a start time");

        // Given
        long label3Id = store.idForLabel("label2".getBytes());
        store.setLabel("key2".getBytes(), label3Id);
        assertEquals(3, store.keyCount());

        RestoreConfig restoreConfig = RestoreConfig.builder()
                                                   .backupLocation(backupDir)
                                                   .build();

        // When
        RestoreStatus restoreStatus = store.restore(restoreConfig);

        // Then
        assertTrue(restoreStatus.isSuccess(), "Restore should succeed");
        assertNotNull(restoreStatus.getBackupId(), "Restore should have backup ID");
        assertTrue(restoreStatus.getBytesRestored() > 0, "Should have restored some bytes");

        assertEquals(2, store.keyCount(), "Should have 2 keys after restore");
        assertEquals(2, store.labelCount(), "Should have 2 labels after restore");
        assertEquals(label1Id, store.getLabel("key0".getBytes()));
        assertEquals(label2Id, store.getLabel("key1".getBytes()));
        assertNull(store.getLabel("key2".getBytes()), "key2 should not exist after restore");
    }

    @Test
    void testCompactionMoreDataForceFlush() throws Exception {
        // Given
        int numLabels = 100_000;
        int valueSize = 1024;

        for (int i = 0; i < numLabels; i++) {
            byte[] value = new byte[valueSize];
            Arrays.fill(value, (byte) (i % 256));
            long labelId = store.idForLabel(("label" + i).getBytes());
            store.setLabel(("key" + i).getBytes(), labelId);

            // force flush every 10k records to create multiple SST files
            if (i > 0 && i % 10000 == 0) {
                store.flush();
            }
        }
        store.flush();

        assertEquals(numLabels, store.keyCount());
        assertEquals(numLabels, store.labelCount());

        // When
        int deleteCount = numLabels / 2;
        for (int i = 0; i < deleteCount; i++) {
            store.removeLabel(("key" + i).getBytes());
        }
        assertEquals(numLabels/2, store.keyCount());

        store.flush();

        CompactStatus status = store.compact();

        // Then
        assertNotNull(status);
        assertTrue(status.getSizeBefore() > 0,
                   "Size before should be positive (SST files should exist)");
        assertTrue(status.getSizeAfter() > 0,
                   "Size after should be positive");
        assertTrue(status.getSizeAfter() < status.getSizeBefore(),
                   String.format("Size after (%d) should be < size before (%d)",
                                 status.getSizeAfter(), status.getSizeBefore()));
        assertTrue(status.getReclaimedBytes() > 0,
                   "Should have reclaimed some space or stayed the same");
    }

    @Test(expectedExceptions = BackupException.class,
            expectedExceptionsMessageRegExp = ".*Backup directory must be specified.*")
    public void testBackupFailsWhenDirectoryNotSpecified() {
        store.backup(BackupConfig.builder().name("invalid").build());
    }

    @Test(expectedExceptions = RestoreException.class,
            expectedExceptionsMessageRegExp = ".*Backup directory must be specified.*")
    public void testRestoreFailsWhenDirectoryNotSpecified() {
        store.restore(RestoreConfig.builder().build());
    }

    @Test
    void testBackupStatusContainsCorrectMetadata() {
        // Given
        long labelId = store.idForLabel("test-label".getBytes());
        store.setLabel("test-key".getBytes(), labelId);

        BackupConfig config = BackupConfig.builder()
                                          .name("metadata-test")
                                          .backupLocation(backupDir)
                                          .build();
        // When
        BackupStatus status = store.backup(config);
        // Then
        assertTrue(status.isSuccess());
        assertEquals("1", status.getBackupId());
        assertFalse(status.getErrorMessage().isPresent());
    }

    @Test
    void testMultipleBackups() {
        // Given
        long id1 = store.idForLabel("label1".getBytes());
        store.setLabel("key1".getBytes(), id1);
        BackupStatus status1 = store.backup(BackupConfig.builder()
                                                        .name("backup-1")
                                                        .backupLocation(backupDir)
                                                        .build());
        assertTrue(status1.isSuccess());

        long id2 = store.idForLabel("label2".getBytes());
        store.setLabel("key2".getBytes(), id2);
        BackupStatus status2 = store.backup(BackupConfig.builder()
                                                        .name("backup-2")
                                                        .backupLocation(backupDir)
                                                        .build());

        assertTrue(status2.isSuccess());
        assertNotEquals(status1.getBackupId(), status2.getBackupId());
    }

    @Test
    public void testListBackups() {
        // Given
        store.setLabel("key1".getBytes(), store.idForLabel("label1".getBytes()));
        store.backup(BackupConfig.builder().name("backup-1").backupLocation(backupDir).build());

        store.setLabel("key2".getBytes(), store.idForLabel("label2".getBytes()));
        store.backup(BackupConfig.builder().name("backup-2").backupLocation(backupDir).build());

        // When
        List<BackupDetails> backups = store.listBackups(backupDir);

        // Then
        assertEquals(backups.size(), 2);
        assertEquals(backups.get(0).getBackupId(), "1");
        assertEquals(backups.get(1).getBackupId(), "2");
        assertTrue(backups.get(0).getSizeBytes() > 0);
    }

    @Test
    public void testRestoreSpecificBackup() throws Exception {
        // Given
        store.setLabel("key1".getBytes(), store.idForLabel("label1".getBytes()));
        BackupStatus backup1 = store.backup(BackupConfig.builder()
                                                        .name("backup-1")
                                                        .backupLocation(backupDir)
                                                        .build());

        store.setLabel("key2".getBytes(), store.idForLabel("label2".getBytes()));
        store.backup(BackupConfig.builder().name("backup-2").backupLocation(backupDir).build());

        // When
        store.restore(RestoreConfig.builder()
                                   .backupId(backup1.getBackupId())
                                   .backupLocation(backupDir)
                                   .build());

        // Then
        assertNotNull(store.getLabel("key1".getBytes()));
        assertNull(store.getLabel("key2".getBytes()));
    }

    @Test
    void testStoreIsUsableAfterRestore() throws Exception {
        // Given
        addLabels(2);
        assertEquals(2, store.keyCount());
        assertEquals(2, store.labelCount());

        long label1Id = store.idForLabel("label0".getBytes());
        long label2Id = store.idForLabel("label1".getBytes());

        BackupConfig backupConfig = BackupConfig.builder()
                                                .name("post-restore-usability")
                                                .backupLocation(backupDir)
                                                .build();
        BackupStatus backupStatus = store.backup(backupConfig);
        assertTrue(backupStatus.isSuccess());

        long label3Id = store.idForLabel("label2".getBytes());
        store.setLabel("key2".getBytes(), label3Id);
        assertEquals(3, store.keyCount());

        // When
        RestoreStatus restoreStatus = store.restore(RestoreConfig.builder()
                                                                 .backupLocation(backupDir)
                                                                 .build());
        assertTrue(restoreStatus.isSuccess());

        // Then
        assertEquals(2, store.keyCount(), "Should have 2 keys after restore");
        assertEquals(2, store.labelCount(), "Should have 2 labels after restore");
        assertEquals(label1Id, store.getLabel("key0".getBytes()));
        assertEquals(label2Id, store.getLabel("key1".getBytes()));
        assertNull(store.getLabel("key2".getBytes()), "key2 should not exist after restore");

        //verifies store is fully usable after restore
        long newLabelId = store.idForLabel("newLabel".getBytes());
        store.setLabel("newKey".getBytes(), newLabelId);
        assertEquals(3, store.keyCount(), "Should be able to add new keys after restore");
        assertEquals(newLabelId, store.getLabel("newKey".getBytes()),
                     "Should be able to read newly written key after restore");

        store.removeLabel("newKey".getBytes());
        assertEquals(2, store.keyCount(), "Should be able to delete keys after restore");
        assertNull(store.getLabel("newKey".getBytes()), "Deleted key should not exist after restore");

        // second backup/restore cycle
        BackupStatus secondBackup = store.backup(BackupConfig.builder()
                                                             .name("post-restore-backup")
                                                             .backupLocation(backupDir)
                                                             .build());
        assertTrue(secondBackup.isSuccess(), "Should be able to backup again after restore");
    }

    @Test
    public void testDeleteBackup() {
        // Given
        store.setLabel("key1".getBytes(), store.idForLabel("label1".getBytes()));
        BackupStatus backup = store.backup(BackupConfig.builder()
                                                       .name("to-delete")
                                                       .backupLocation(backupDir)
                                                       .build());
        assertEquals(store.listBackups(backupDir).size(), 1);
        // When
        store.deleteBackup(backupDir, backup.getBackupId());
        // Then
        assertEquals(store.listBackups(backupDir).size(), 0);
    }

    @Test(expectedExceptions = BackupException.class,
            expectedExceptionsMessageRegExp = ".*Failed to delete backup.*")
    public void testDeleteBackupFailed() {
        store.setLabel("key1".getBytes(), store.idForLabel("label1".getBytes()));
        BackupStatus backup = store.backup(BackupConfig.builder()
                                                       .name("to-delete")
                                                       .backupLocation(backupDir)
                                                       .build());
        assertEquals(store.listBackups(backupDir).size(), 1);
        store.deleteBackup(backupDir, backup.getBackupId());
        assertEquals(store.listBackups(backupDir).size(), 0);
        store.deleteBackup(backupDir, backup.getBackupId());
    }

    @Test
    void testStoreThrowsIllegalStateWhenUsedDuringRestore() throws Exception {
        // Given
        addLabels(2);
        BackupStatus backupStatus = store.backup(BackupConfig.builder()
                                                             .name("lock-test")
                                                             .backupLocation(backupDir)
                                                             .build());
        assertTrue(backupStatus.isSuccess());

        // simulates the store being mid-restore
        Field restoringField = AbstractStorage.class.getDeclaredField("restoring");
        restoringField.setAccessible(true);
        restoringField.set(store, true);

        try {
            // Then
            assertThrows(RestoreException.class, () -> store.setLabel("key".getBytes(), 1L));
            assertThrows(RestoreException.class, () -> store.getLabel("key0".getBytes()));
            assertThrows(RestoreException.class, () -> store.removeLabel("key0".getBytes()));
            assertThrows(RestoreException.class, () -> store.keyCount());
            assertThrows(RestoreException.class, () -> store.labelCount());
            assertThrows(RestoreException.class, () -> store.idForLabel("label0".getBytes()));
        } finally {
            restoringField.set(store, false);
        }
    }

    @Test
    void testStoreIsUsableAfterRestoreCompletes() throws Exception {
        // Given
        addLabels(2);
        store.backup(BackupConfig.builder()
                                 .name("lock-release-test")
                                 .backupLocation(backupDir)
                                 .build());

        // When
        store.restore(RestoreConfig.builder()
                                   .backupLocation(backupDir)
                                   .build());

        // Then
        Field restoringField = AbstractStorage.class.getDeclaredField("restoring");
        restoringField.setAccessible(true);
        assertFalse((Boolean) restoringField.get(store), "restoring flag should be cleared after restore");

        // if these throw anything the test fails
        store.setLabel("newKey".getBytes(), store.idForLabel("newLabel".getBytes()));
        store.getLabel("newKey".getBytes());
    }

    @Test
    void testRestoringFlagClearedEvenOnFailure() throws Exception {
        // Given
        addLabels(2);

        // When
        assertThrows(RestoreException.class, () -> store.restore(
                RestoreConfig.builder()
                             .backupLocation("/nonexistent/path/to/backups")
                             .build()
        ));

        // Then
        Field restoringField = AbstractStorage.class.getDeclaredField("restoring");
        restoringField.setAccessible(true);
        assertFalse((Boolean) restoringField.get(store), "restoring flag should be cleared even after failed restore");

        // store should still be usable and not throw
        store.keyCount();
    }

    private void addLabels(int count) {
        for (int i = 0; i < count; i++) {
            byte[] key = ("key" + i).getBytes();
            long labelId = store.idForLabel(("label" + i).getBytes());
            store.setLabel(key, labelId);
        }
    }
}
