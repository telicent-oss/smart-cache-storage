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

import org.awaitility.Awaitility;
import org.rocksdb.RocksDBException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.*;

public class TestTransactionIsolation extends AbstractRocksDBTests {

    public static final byte[] TEST_KEY = "test".getBytes(StandardCharsets.UTF_8);
    public static final byte[] OTHER_KEY = "other".getBytes(StandardCharsets.UTF_8);
    private ExecutorService executor;

    @BeforeMethod
    public void setupThreads() throws InterruptedException {
        if (executor != null) {
            executor.shutdownNow();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        executor = Executors.newFixedThreadPool(3);
    }

    @AfterMethod
    public void cleanupThreads() throws InterruptedException {
        if (executor != null) {
            executor.shutdownNow();
            executor.awaitTermination(5, TimeUnit.SECONDS);
            executor = null;
        }
    }

    private static Runnable createReader(External external, String name, Semaphore acquireBeforeRead, Semaphore releaseAfterRead) {
        return () -> {
            Thread.currentThread().setName(name);
            try (TransactionContext readTransaction = external.start()) {
                acquireBeforeRead.acquire();
                Assert.assertNull(readTransaction.get(external.getDefaultHandle(), TEST_KEY));
                releaseAfterRead.release();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static Runnable createWriter(External external, String name, Semaphore releaseAfterWrite, Semaphore acquireAfterWrite, byte[] key,
                                         byte[] value) {
        return () -> {
            Thread.currentThread().setName(name);
            try (TransactionContext writeTransaction = external.start()) {
                // Write the key and value
                writeTransaction.put(external.getDefaultHandle(), key, value);

                // Validate that it was written and we can read the written value
                Assert.assertEquals(writeTransaction.get(external.getDefaultHandle(), key), value);

                // Release a permit to signal the other thread we've performed our write
                releaseAfterWrite.release(1);

                // Acquire a permit so we know the other thread has completed its read/write operation
                acquireAfterWrite.acquire();

                // Further validate that the other thread hasn't affected the value that we wrote
                Assert.assertEquals(writeTransaction.get(external.getDefaultHandle(), key), value);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    private void validateFutures(Future<?>... futures) {
        for (Future<?> f : futures) {
            Assert.assertTrue(f.isDone());
            try {
                f.get();
                // If we can get a result then the future completed successfully
            } catch (InterruptedException e) {
                // Ignore, if we've passed the isDone() assertion then we know a result is available immediately
            } catch (ExecutionException e) {
                Assert.fail("Thread failed: " + e.getMessage());
            }
        }
    }

    private void waitForFutures(Future<?>... futures) {
        //@formatter:off
        Awaitility.await("Wait for Futures")
                  .pollDelay(Duration.ofMillis(50))
                  .pollInterval(Duration.ofMillis(100))
                  .atMost(Duration.ofSeconds(10))
                  .until(() -> Arrays.stream(futures).allMatch(Future::isDone));
        //@formatter:on
    }

    @Test
    public void givenDifferentThreads_whenWritingInOneThread_thenNotVisibleInOtherThread() throws RocksDBException,
            IOException {
        // Given
        Semaphore writeSignal = new Semaphore(0);
        Semaphore readSignal = new Semaphore(0);
        try (External external = new External(this.dbDir)) {
            Runnable writer = createWriter(external, "writer", writeSignal, readSignal, TEST_KEY, TEST_KEY);
            Runnable reader = createReader(external, "reader", writeSignal, readSignal);

            // When
            Future<?> readFuture = this.executor.submit(reader);
            Future<?> writeFuture = this.executor.submit(writer);
            waitForFutures(readFuture, writeFuture);

            // Then
            validateFutures(readFuture, writeFuture);
        }
    }

    @Test
    public void givenDifferentThreads_whenWritingDifferentKeysInBothThreads_thenVisibleOnlyInOwnThread() throws RocksDBException,
            IOException {
        // Given
        Semaphore aSignal = new Semaphore(0);
        Semaphore bSignal = new Semaphore(0);
        try (External external = new External(this.dbDir)) {
            Runnable writer1 = createWriter(external, "Writer 1", aSignal, bSignal, TEST_KEY, TEST_KEY);
            Runnable writer2 = createWriter(external, "Writer 2", bSignal, aSignal, OTHER_KEY, OTHER_KEY);

            // When
            Future<?> f1 = this.executor.submit(writer1);
            Future<?> f2 = this.executor.submit(writer2);
            waitForFutures(f1, f2);

            // Then
            validateFutures(f1, f2);
        }
    }

    @Test
    public void givenDifferentThreads_whenWritingSameKeysInBothThreadsWithoutCommit_thenBothSucceeds() throws RocksDBException,
            IOException {
        // Given
        Semaphore aSignal = new Semaphore(0);
        Semaphore bSignal = new Semaphore(0);
        Semaphore cSignal = new Semaphore(2);
        try (External external = new External(this.dbDir)) {
            Runnable writer1 = createWriter(external, "Writer 1", aSignal, cSignal, TEST_KEY, TEST_KEY);
            Runnable writer2 = createWriter(external, "Writer 2", bSignal, cSignal, TEST_KEY, TEST_KEY);

            // When
            Future<?> f1 = this.executor.submit(writer1);
            Future<?> f2 = this.executor.submit(writer2);
            waitForFutures(f1, f2);

            // Then
            validateFutures(f1, f2);
        }
    }
}
