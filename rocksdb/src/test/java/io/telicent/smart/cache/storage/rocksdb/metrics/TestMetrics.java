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
package io.telicent.smart.cache.storage.rocksdb.metrics;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.semconv.DbAttributes;
import io.telicent.smart.cache.observability.metrics.MetricTestUtils;
import io.telicent.smart.cache.storage.BackupConfig;
import io.telicent.smart.cache.storage.BackupStatus;
import io.telicent.smart.cache.storage.RestoreConfig;
import io.telicent.smart.cache.storage.RestoreStatus;
import io.telicent.smart.cache.storage.rocksdb.AbstractRocksDBTests;
import io.telicent.smart.cache.storage.rocksdb.External;
import io.telicent.smart.cache.storage.rocksdb.TransactionContext;
import org.apache.commons.lang3.RandomUtils;
import org.rocksdb.RocksDBException;
import org.rocksdb.TickerType;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class TestMetrics extends AbstractRocksDBTests {

    /**
     * Test bytes
     */
    protected static final byte[] TEST_BYTES = "test".getBytes(
            StandardCharsets.UTF_8);

    @BeforeMethod
    public void setupMetrics() {
        MetricTestUtils.enableMetricsCapture();
    }

    @AfterMethod
    public void teardownMetrics() {
        MetricTestUtils.disableMetricsCapture();
    }

    @Test
    public void givenNoRocksStorage_whenCollectingMetrics_thenNoRocksMetricsExposed() {
        // Given, When and Then
        MetricTestUtils.verifyNotReported(MetricNames.names());
    }

    @Test
    public void givenRocksStorage_whenPerformingTransactions_thenMetricsAreRecorded() throws RocksDBException,
            IOException {
        // Given
        Attributes attributes = expectedAttributes();
        long numTransactions = RandomUtils.insecure().randomLong(10, 1_000);
        long readOnly = 0, write = 0;
        try (External storage = new External(this.dbDir)) {
            // When
            for (int i = 1; i <= numTransactions; i++) {
                try (TransactionContext transaction = storage.start()) {
                    if (i % 2 == 0) {
                        transaction.commit();
                        write++;
                    } else {
                        readOnly++;
                    }
                }
            }

            // Then
            Assert.assertEquals(
                    MetricTestUtils.getReportedMetric(MetricNames.TRANSACTIONS, attributes).longValue(),
                    numTransactions);
            Assert.assertEquals(
                    MetricTestUtils.getReportedMetric(MetricNames.READONLY_TRANSACTIONS, attributes).longValue(),
                    readOnly);
            Assert.assertEquals(
                    MetricTestUtils.getReportedMetric(MetricNames.WRITE_TRANSACTIONS, attributes).longValue(),
                    write);
            Assert.assertNotNull(MetricTestUtils.getReportedMetric(MetricNames.BLOCK_CACHE_MEMORY_USAGE, attributes));
            Assert.assertNotNull(
                    MetricTestUtils.getReportedMetric(MetricNames.BLOCK_CACHE_PINNED_MEMORY_USAGE, attributes));
            Assert.assertNotNull(MetricTestUtils.getReportedMetric(MetricNames.TABLE_READERS_MEMORY_USAGE, attributes));
            Assert.assertNotNull(MetricTestUtils.getReportedMetric(MetricNames.MEMTABLES_MEMORY_USAGE, attributes));
        }
    }

    @Test
    public void givenRocksStorage_whenCollectingMetrics_thenInterestingRocksTickersExposed() throws RocksDBException,
            IOException {
        // Given
        try (External external = new External(this.dbDir)) {
            // When and Then
            String[] names = new String[MetricNames.INTERESTING_ROCKS_TICKERS.length];
            for (int i = 0; i < MetricNames.INTERESTING_ROCKS_TICKERS.length; i++) {
                names[i] = MetricNames.asMetricName(MetricNames.INTERESTING_ROCKS_TICKERS[i]);
            }
            MetricTestUtils.verifyReported(names);
        }
    }

    private Attributes expectedAttributes() {
        return Attributes.builder()
                         .put(DbAttributes.DB_SYSTEM_NAME, "rocksdb")
                         .put(DbAttributes.DB_NAMESPACE, "file://" + this.dbDir.getAbsolutePath())
                         .build();
    }

    @Test
    public void givenRocksStorage_whenBackupAndRestore_thenMetricsContinueToBeTracked() throws IOException,
            RocksDBException {
        // Given
        Attributes attributes = expectedAttributes();
        File backupDir = Files.createTempDirectory("backup").toFile();

        try (External storage = new External(this.dbDir)) {
            // When
            try (TransactionContext transaction = storage.start()) {
                transaction.put(storage.getColumnFamilyHandles().get("default"), TEST_BYTES,
                                TEST_BYTES);
                transaction.commit();
            }
            Assert.assertEquals(MetricTestUtils.getReportedMetric(MetricNames.TRANSACTIONS, attributes), 1L);
            Assert.assertEquals(MetricTestUtils.getReportedMetric(MetricNames.WRITE_TRANSACTIONS, attributes), 1L);
            MetricTestUtils.verifyNotReported(MetricNames.READONLY_TRANSACTIONS);
            long bytesWritten =
                    MetricTestUtils.getReportedMetric(MetricNames.asMetricName(TickerType.BYTES_WRITTEN), attributes)
                                   .longValue();
            BackupStatus backupStatus =
                    storage.backup(BackupConfig.builder().backupLocation(backupDir.getAbsolutePath()).build());
            Assert.assertTrue(backupStatus.isSuccess());
            RestoreStatus restoreStatus = storage.restore(RestoreConfig.builder()
                                                                       .backupLocation(backupDir.getAbsolutePath())
                                                                       .backupId(backupStatus.getBackupId())
                                                                       .build());
            Assert.assertTrue(restoreStatus.isSuccess());

            // Then
            try (TransactionContext transaction = storage.start()) {
                Assert.assertEquals(transaction.get(storage.getColumnFamilyHandles().get("default"), TEST_BYTES),
                                    TEST_BYTES);
                transaction.put(storage.getColumnFamilyHandles().get("default"), TEST_BYTES,
                                "other".getBytes(StandardCharsets.UTF_8));
                transaction.commit();
            }
            Assert.assertEquals(MetricTestUtils.getReportedMetric(MetricNames.TRANSACTIONS, attributes), 2L);
            Assert.assertEquals(MetricTestUtils.getReportedMetric(MetricNames.WRITE_TRANSACTIONS, attributes), 2L);
            MetricTestUtils.verifyNotReported(MetricNames.READONLY_TRANSACTIONS);
            long currentBytesWritten =
                    MetricTestUtils.getReportedMetric(MetricNames.asMetricName(TickerType.BYTES_WRITTEN), attributes)
                                   .longValue();
            Assert.assertNotEquals(currentBytesWritten, bytesWritten);
            Assert.assertTrue(currentBytesWritten > bytesWritten);
        }

    }
}
