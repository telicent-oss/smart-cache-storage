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

import io.telicent.smart.cache.storage.BackupConfig;
import io.telicent.smart.cache.storage.RestoreConfig;
import io.telicent.smart.cache.storage.RestoreStatus;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.testng.Assert.*;

/**
 * Tests that a restore refuses to close the database while transactions remain open on it
 * <p>
 * Closing a RocksDB {@code TransactionDB} frees the native transactions begun against it, along with the shared
 * read/write options and column family handles that those transactions point at.  Any subsequent use of such a
 * transaction - including merely rolling it back when the caller closes it - dereferences freed memory and aborts the
 * JVM, typically inside {@code rocksdb::PessimisticTransactionDB::UnLock()}.  That abort cannot be caught, so none of
 * the tests here are allowed to actually provoke it; they assert that the storage refuses the restore instead.
 * </p>
 */
public class TestRestoreWithOpenTransactions {

    private static final byte[] VALUE = "value".getBytes(StandardCharsets.UTF_8);

    private File tempDir;
    private String backupDir;
    private RestoreHazardStorage storage;

    @BeforeMethod
    public void setup() throws Exception {
        this.tempDir = Files.createTempDirectory("restore-hazard-").toFile();
        this.backupDir = new File(this.tempDir, "backups").getAbsolutePath();
        this.storage = new RestoreHazardStorage(new File(this.tempDir, "db"));
        this.storage.put(RestoreHazardStorage.KEY, VALUE);
        this.storage.backup(BackupConfig.builder().name("hazard").backupLocation(this.backupDir).build());
    }

    @AfterMethod
    public void teardown() throws IOException {
        if (this.storage != null && !this.storage.isClosed()) {
            this.storage.close();
        }
        FileUtils.deleteDirectory(this.tempDir);
    }

    private RestoreConfig restoreConfig() {
        return RestoreConfig.builder().backupLocation(this.backupDir).build();
    }

    @Test
    public void givenNestedTransactionOnCallingThread_whenRestoring_thenRefused() throws Exception {
        // Given
        // NB - This is the shape of the real world failure, a caller wrapped the whole restore in a longer lived
        //      transaction on the store
        try (final TransactionContext context = this.storage.nestedTransaction()) {
            // When
            final ActiveTransactionsException e =
                    expectThrows(ActiveTransactionsException.class, () -> this.storage.restore(restoreConfig()));

            // Then
            assertTrue(e.getMessage().contains("1 of which are held by the calling thread"), e.getMessage());
            assertTrue(context.isActive(), "Transaction should have been left alone by the refused restore");
            // The database must still be open, i.e. the restore refused before closing anything
            assertEquals(context.get(this.storage.getDefaultHandle(), RestoreHazardStorage.KEY), VALUE);
        }
    }

    @Test
    public void givenShortLivedTransactionOnCallingThread_whenRestoring_thenRefused() {
        // Given
        try (final TransactionContext ignored = this.storage.transaction()) {
            // When and Then
            assertThrows(ActiveTransactionsException.class, () -> this.storage.restore(restoreConfig()));
        }
    }

    @Test
    public void givenReadOnlyTransactionOnCallingThread_whenRestoring_thenRefused() {
        // Given
        // NB - A read-only context holds no native transaction but does hold the database and the shared read options,
        //      which a restore would free just the same
        try (final TransactionContext ignored = this.storage.readOnlyTransaction()) {
            // When and Then
            assertThrows(ActiveTransactionsException.class, () -> this.storage.restore(restoreConfig()));
        }
    }

    @Test
    public void givenTransactionOnAnotherThread_whenRestoring_thenRefused() throws Exception {
        // Given
        final CountDownLatch opened = new CountDownLatch(1);
        final CountDownLatch release = new CountDownLatch(1);
        final AtomicReference<Exception> failure = new AtomicReference<>();
        final Thread other = new Thread(() -> {
            try (final TransactionContext ignored = this.storage.transaction()) {
                opened.countDown();
                assertTrue(release.await(10, TimeUnit.SECONDS), "Transaction was never released");
            } catch (Exception e) {
                failure.set(e);
            }
        }, "other-transaction");
        other.start();
        assertTrue(opened.await(10, TimeUnit.SECONDS), "Transaction was never opened");

        try {
            // When
            final ActiveTransactionsException e =
                    expectThrows(ActiveTransactionsException.class, () -> this.storage.restore(restoreConfig()));

            // Then
            assertTrue(e.getMessage().contains("0 of which are held by the calling thread"), e.getMessage());
        } finally {
            release.countDown();
            other.join(TimeUnit.SECONDS.toMillis(10));
        }
        assertNull(failure.get(), "Other thread failed: " + failure.get());
    }

    @Test
    public void givenRefusedRestore_whenTransactionClosedAndRestoreRetried_thenSucceeds() throws Exception {
        // Given
        this.storage.put("extra".getBytes(StandardCharsets.UTF_8), VALUE);
        assertEquals(this.storage.count(), 2);
        try (final TransactionContext ignored = this.storage.nestedTransaction()) {
            assertThrows(ActiveTransactionsException.class, () -> this.storage.restore(restoreConfig()));
        }

        // When
        // NB - A refused restore must leave the storage's bookkeeping intact so that a subsequent legitimate restore
        //      still works
        final RestoreStatus status = this.storage.restore(restoreConfig());

        // Then
        assertTrue(status.isSuccess());
        assertEquals(this.storage.count(), 1);
        assertEquals(this.storage.get(RestoreHazardStorage.KEY), VALUE);
    }

    @Test
    public void givenCommittedTransactions_whenRestoring_thenSucceeds() throws Exception {
        // Given
        try (final TransactionContext context = this.storage.nestedTransaction()) {
            context.put(this.storage.getDefaultHandle(), "extra".getBytes(StandardCharsets.UTF_8), VALUE);
            context.commit();
        }
        try (final TransactionContext ignored = this.storage.readOnlyTransaction()) {
            // Deliberately empty, just needs opening and closing again
        }

        // When
        final RestoreStatus status = this.storage.restore(restoreConfig());

        // Then
        assertTrue(status.isSuccess(), "Closed transactions must not block a restore");
        assertEquals(this.storage.count(), 1);
    }

    @Test
    public void givenProbedRestore_whenReopening_thenStorageIsStillMarkedAsRestoring() {
        // Given
        this.storage.probeDuringRestore();

        // When
        this.storage.restore(restoreConfig());

        // Then
        // NB - The restoring flag must already be set by the time the database is reopened, otherwise there is a
        //      window in which neither closed nor restoring is true and begin() would hand out a transaction against
        //      a database that has been closed
        assertEquals(this.storage.getRestoringDuringReopen(), Boolean.TRUE,
                     "Storage should have been marked as restoring while the database was closed");
    }

    @Test
    public void givenClosedStorage_whenRestoring_thenRefused() {
        // Given
        this.storage.close();

        // When and Then
        assertThrows(IllegalStateException.class, () -> this.storage.restore(restoreConfig()));
    }

}
