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
package io.telicent.smart.cache.storage;

import org.testng.annotations.Test;
import java.io.File;
import java.time.Instant;
import static org.testng.Assert.*;

public class TestBackupCapabilities {

    @Test
    public void testBackupConfigBuilder() {
        File dir = new File("/tmp/backup");
        BackupConfig config = BackupConfig.builder()
                                          .name("test-backup")
                                          .backupDir(dir)
                                          .option("compress", true)
                                          .option("threads", 4)
                                          .build();

        assertEquals(config.getName(), "test-backup");
        assertEquals(config.getBackupDir(), dir);
        assertEquals(config.getOption("compress"), true);
        assertEquals(config.getOption("threads"), 4);
        assertEquals(config.getOptions().size(), 2);
    }

    @Test
    public void testBackupConfigMinimal() {
        BackupConfig config = BackupConfig.builder()
                                          .name("minimal")
                                          .build();

        assertEquals(config.getName(), "minimal");
        assertNull(config.getBackupDir());
        assertTrue(config.getOptions().isEmpty());
        assertNull(config.getOption("nonexistent"));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testBackupConfigRequiresName() {
        BackupConfig.builder().build();
    }

    @Test
    public void testRestoreConfigBuilder() {
        File dir = new File("/tmp/restore");
        RestoreConfig config = RestoreConfig.builder()
                                            .backupDir(dir)
                                            .option("verify", true)
                                            .build();

        assertEquals(config.getBackupDir(), dir);
        assertEquals(config.getOption("verify"), true);
    }

    @Test
    public void testBackupStatusSuccess() {
        BackupStatus status = BackupStatus.success("backup-123", 1048576L, Instant.now());

        assertTrue(status.isSuccess());
        assertEquals(status.getBackupId(), "backup-123");
        assertEquals(status.getBytesBackedUp(), 1048576L);
        assertNotNull(status.getStartTime());
        assertFalse(status.getErrorMessage().isPresent());
    }

    @Test
    public void testBackupStatusFailure() {
        BackupStatus status = BackupStatus.failure("Disk full");

        assertFalse(status.isSuccess());
        assertTrue(status.getErrorMessage().isPresent());
        assertEquals(status.getErrorMessage().get(), "Disk full");
    }

    @Test
    public void testBackupStatusBuilder() {
        Instant now = Instant.now();
        BackupStatus status = BackupStatus.builder()
                                          .success(true)
                                          .backupId("id-456")
                                          .bytesBackedUp(2097152L)
                                          .startTime(now)
                                          .build();

        assertTrue(status.isSuccess());
        assertEquals(status.getBackupId(), "id-456");
        assertEquals(status.getBytesBackedUp(), 2097152L);
        assertEquals(status.getStartTime(), now);
    }

    @Test
    public void testRestoreStatusSuccess() {
        RestoreStatus status = RestoreStatus.success("backup-789", 3145728L);

        assertTrue(status.isSuccess());
        assertEquals(status.getBackupId(), "backup-789");
        assertEquals(status.getBytesRestored(), 3145728L);
        assertNotNull(status.getTimestamp());
        assertFalse(status.getErrorMessage().isPresent());
    }

    @Test
    public void testRestoreStatusFailure() {
        RestoreStatus status = RestoreStatus.failure("Backup not found");

        assertFalse(status.isSuccess());
        assertTrue(status.getErrorMessage().isPresent());
        assertEquals(status.getErrorMessage().get(), "Backup not found");
    }

    @Test
    public void testRestoreStatusBuilder() {
        Instant now = Instant.now();
        RestoreStatus status = RestoreStatus.builder()
                                            .success(false)
                                            .errorMessage("Corrupted backup")
                                            .timestamp(now)
                                            .build();

        assertFalse(status.isSuccess());
        assertTrue(status.getErrorMessage().isPresent());
        assertEquals(status.getTimestamp(), now);
    }

    @Test
    public void testCompactStatusTwoArgs() {
        CompactStatus status = new CompactStatus(1000000L, 750000L);

        assertEquals(status.getSizeBefore(), 1000000L);
        assertEquals(status.getSizeAfter(), 750000L);
        assertEquals(status.getReclaimedBytes(), 250000L);
        assertNotNull(status.getTimestamp());
    }

    @Test
    public void testCompactStatusThreeArgs() {
        CompactStatus status = new CompactStatus(2000000L, 1500000L, 500000L);

        assertEquals(status.getSizeBefore(), 2000000L);
        assertEquals(status.getSizeAfter(), 1500000L);
        assertEquals(status.getReclaimedBytes(), 500000L);
    }

    @Test
    public void testCompactStatusWithTimestamp() {
        Instant now = Instant.now();
        CompactStatus status = new CompactStatus(1000L, 800L, 200L, now);

        assertEquals(status.getTimestamp(), now);
    }

    @Test
    public void testCompactStatusNoReclaim() {
        CompactStatus status = new CompactStatus(1000000L, 1000000L);
        assertEquals(status.getReclaimedBytes(), 0L);
    }

    @Test
    public void testBackupException() {
        BackupException ex1 = new BackupException("Backup failed");
        assertEquals(ex1.getMessage(), "Backup failed");
        assertNull(ex1.getCause());

        RuntimeException cause = new RuntimeException("IO error");
        BackupException ex2 = new BackupException("Backup failed", cause);
        assertEquals(ex2.getMessage(), "Backup failed");
        assertEquals(ex2.getCause(), cause);
    }

    @Test
    public void testRestoreException() {
        RestoreException ex1 = new RestoreException("Restore failed");
        assertEquals(ex1.getMessage(), "Restore failed");
        assertNull(ex1.getCause());

        RuntimeException cause = new RuntimeException("Corruption");
        RestoreException ex2 = new RestoreException("Restore failed", cause);
        assertEquals(ex2.getCause(), cause);
    }

    @Test
    public void testCompactionException() {
        CompactException ex1 = new CompactException("Compaction failed");
        assertEquals(ex1.getMessage(), "Compaction failed");

        RuntimeException cause = new RuntimeException("DB locked");
        CompactException ex2 = new CompactException("Compaction failed", cause);
        assertEquals(ex2.getCause(), cause);
    }

    @Test
    public void testConfigOptionsImmutability() {
        BackupConfig config = BackupConfig.builder()
                                          .name("test")
                                          .option("key1", "value1")
                                          .build();

        // Modifying returned map shouldn't affect config
        config.getOptions().put("key2", "value2");
        assertNull(config.getOption("key2"));
    }
}
