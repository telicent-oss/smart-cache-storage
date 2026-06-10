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

import java.time.Instant;
import static org.testng.Assert.*;


public class TestBackupCapabilities {

    @Test
    public void testBackupConfigBuilder() {
        // Given
        String dir = "/tmp/backup";
        BackupConfig config = BackupConfig.builder()
                                          .name("test-backup")
                                          .backupLocation(dir)
                                          .option("compress", true)
                                          .option("threads", 4)
                                          .build();
        // When & Then
        assertEquals(config.getName(), "test-backup");
        assertEquals(config.getBackupLocation(), dir);
        assertEquals(config.getOption("compress"), true);
        assertEquals(config.getOption("threads"), 4);
        assertEquals(config.getOptions().size(), 2);
    }

    @Test
    public void testBackupConfigMinimal() {
        // Given
        BackupConfig config = BackupConfig.builder()
                                          .name("minimal")
                                          .build();
        // When & Then
        assertEquals(config.getName(), "minimal");
        assertNull(config.getBackupLocation());
        assertTrue(config.getOptions().isEmpty());
        assertNull(config.getOption("nonexistent"));
    }

    @Test
    public void testRestoreConfigBuilder() {
        // Given
        String dir = "/tmp/restore";
        RestoreConfig config = RestoreConfig.builder()
                                            .backupLocation(dir)
                                            .option("verify", true)
                                            .build();
        // When & Then
        assertEquals(config.getBackupLocation(), dir);
        assertEquals(config.getOption("verify"), true);
    }

    @Test
    public void testBackupStatusSuccess() {
        // Given
        BackupStatus status = BackupStatus.success("backup-123", 1048576L, Instant.now(), Instant.now().plusSeconds(10));

        // When & Then
        assertTrue(status.isSuccess());
        assertEquals(status.getBackupId(), "backup-123");
        assertEquals(status.getBytesBackedUp(), 1048576L);
        assertNotNull(status.getStartTime());
        assertNotNull(status.getEndTime());
        assertFalse(status.getErrorMessage().isPresent());
    }

    @Test
    public void testBackupStatusFailure() {
        // Given
        BackupStatus status = BackupStatus.failure("Disk full");

        // When & Then
        assertFalse(status.isSuccess());
        assertTrue(status.getErrorMessage().isPresent());
        assertEquals(status.getErrorMessage().get(), "Disk full");
    }

    @Test
    public void testBackupStatusBuilder() {
        // Given
        Instant now = Instant.now();
        BackupStatus status = BackupStatus.builder()
                                          .success(true)
                                          .backupId("id-456")
                                          .bytesBackedUp(2097152L)
                                          .startTime(now)
                                          .build();
        // When & Then
        assertTrue(status.isSuccess());
        assertEquals(status.getBackupId(), "id-456");
        assertEquals(status.getBytesBackedUp(), 2097152L);
        assertEquals(status.getStartTime(), now);
    }

    @Test
    public void testRestoreStatusSuccess() {
        // Given
        RestoreStatus status = RestoreStatus.success("backup-789", 3145728L);

        // When & Then
        assertTrue(status.isSuccess());
        assertEquals(status.getBackupId(), "backup-789");
        assertEquals(status.getBytesRestored(), 3145728L);
        assertFalse(status.getErrorMessage().isPresent());
    }

    @Test
    public void testRestoreStatusFailure() {
        // Given
        RestoreStatus status = RestoreStatus.failure("Backup not found");

        // When & Then
        assertFalse(status.isSuccess());
        assertTrue(status.getErrorMessage().isPresent());
        assertEquals(status.getErrorMessage().get(), "Backup not found");
    }

    @Test
    public void testRestoreStatusBuilder() {
        //Given
        RestoreStatus status = RestoreStatus.builder()
                                            .success(false)
                                            .errorMessage("Corrupted backup")
                                            .build();
        // When & Then
        assertFalse(status.isSuccess());
        assertTrue(status.getErrorMessage().isPresent());
    }

    @Test
    public void testCompactStatusFourArgs() {
        // Given
        CompactStatus status = new CompactStatus(1000000L, 750000L, Instant.now(), Instant.now().plusSeconds(10));

        // When & Then
        assertEquals(status.getSizeBefore(), 1000000L);
        assertEquals(status.getSizeAfter(), 750000L);
        assertEquals(status.getReclaimedBytes(), 250000L);
        assertNotNull(status.getStartTime());
        assertNotNull(status.getEndTime());
    }

    @Test
    public void testCompactStatusFiveArgs() {
        // Given
        CompactStatus status = new CompactStatus(2000000L, 1500000L, 500000L, Instant.now(), Instant.now().plusSeconds(10));

        // When & Then
        assertEquals(status.getSizeBefore(), 2000000L);
        assertEquals(status.getSizeAfter(), 1500000L);
        assertEquals(status.getReclaimedBytes(), 500000L);
        assertNotNull(status.getStartTime());
        assertNotNull(status.getEndTime());
    }

    @Test
    public void testCompactStatusNoReclaim() {
        // Given
        CompactStatus status = new CompactStatus(1000000L, 1000000L, Instant.now(), Instant.now().plusSeconds(10));
        // When & Then
        assertEquals(status.getReclaimedBytes(), 0L);
    }

    @Test
    public void testBackupException() {
        // Given
        BackupException ex1 = new BackupException("Backup failed");
        RuntimeException cause = new RuntimeException("IO error");
        BackupException ex2 = new BackupException("Backup failed", cause);

        // When & Then
        assertEquals(ex1.getMessage(), "Backup failed");
        assertNull(ex1.getCause());
        assertEquals(ex2.getMessage(), "Backup failed");
        assertEquals(ex2.getCause(), cause);
    }

    @Test
    public void testRestoreException() {
        // Given
        RestoreException ex1 = new RestoreException("Restore failed");
        RuntimeException cause = new RuntimeException("Corruption");
        RestoreException ex2 = new RestoreException("Restore failed", cause);

        //  When & Then
        assertEquals(ex1.getMessage(), "Restore failed");
        assertNull(ex1.getCause());
        assertEquals(ex2.getCause(), cause);
    }

    @Test
    public void testCompactionException() {
        // Given
        CompactException ex1 = new CompactException("Compaction failed");
        RuntimeException cause = new RuntimeException("DB locked");
        CompactException ex2 = new CompactException("Compaction failed", cause);

        // When & Then
        assertEquals(ex1.getMessage(), "Compaction failed");
        assertEquals(ex2.getCause(), cause);
    }

    @Test
    public void testConfigOptionsImmutability() {
        // Given
        BackupConfig config = BackupConfig.builder()
                                          .name("test")
                                          .option("key1", "value1")
                                          .build();
        // When & Then
        config.getOptions().put("key2", "value2");
        assertNull(config.getOption("key2"));
    }
}
