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
import io.telicent.smart.cache.observability.metrics.MetricTestUtils;
import org.rocksdb.RocksDBException;
import org.rocksdb.Statistics;
import org.rocksdb.TransactionDB;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestMetricsHolder {

    @BeforeMethod
    public void setupMetrics() {
        MetricTestUtils.enableMetricsCapture();
    }

    @AfterMethod
    public void teardownMetrics() {
        MetricTestUtils.disableMetricsCapture();
    }

    @Test
    public void givenBadDb_whenCollectingMetrics_thenNothingCollected() throws RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        when(db.getLongProperty(any())).thenThrow(new RocksDBException("Bad property"));
        Statistics stats = mock(Statistics.class);

        // When and Then
        MetricTestUtils.verifyNotReported(MetricNames.names());
    }

    @Test
    public void givenClosedHolder_whenUpdatingMetrics_thenNothingReported() throws RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        when(db.getLongProperty(any())).thenReturn(0L);
        Statistics stats = mock(Statistics.class);
        MetricsHolder metrics = new MetricsHolder(Attributes.empty(), db, stats);
        metrics.close();

        // When
        metrics.incrementTransactions();
        metrics.incrementWriteTransactions();
        metrics.incrementReadOnlyTransactions();
        metrics.incrementActiveTransactions();
        metrics.decrementActiveTransactions();

        // Then
        MetricTestUtils.verifyNotReported(MetricNames.TELICENT_METRICS);
    }

    @Test
    public void givenHolder_whenClosingTwice_thenOk() {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Statistics stats = mock(Statistics.class);
        MetricsHolder holder = new MetricsHolder(Attributes.empty(), db, stats);

        // When and Then
        holder.close();
        holder.close();
    }

    @Test
    public void givenClosedHolder_whenCollectingMetrics_thenNothingReported() throws RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        when(db.getLongProperty(any())).thenReturn(0L);
        Statistics stats = mock(Statistics.class);
        when(stats.getTickerCount(any())).thenReturn(0L);
        MetricsHolder holder = new MetricsHolder(Attributes.empty(), db, stats);
        // NB - A normal close() would set the closed flag and immediately close() all the observables which stops them
        //      invoking their callbacks but if we set the closed flag without that we want to test the edge case of the
        //      observables callbacks triggering during a close() operation and ensure they don't try and report metrics
        //      from what are now bad references
        holder.setClosedFlag();

        // When and Then
        MetricTestUtils.verifyNotReported(MetricNames.names());
    }
}
