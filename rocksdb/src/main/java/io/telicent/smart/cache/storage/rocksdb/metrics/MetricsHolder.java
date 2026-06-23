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
import io.opentelemetry.api.metrics.*;
import io.telicent.smart.cache.observability.TelicentMetrics;
import io.telicent.smart.cache.storage.rocksdb.AbstractRocksDBStorage;
import org.apache.commons.io.FileUtils;
import org.rocksdb.Statistics;
import org.rocksdb.TickerType;
import org.rocksdb.TransactionDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import static io.telicent.smart.cache.storage.rocksdb.metrics.MetricNames.*;

/**
 * A helper class that holds all the OpenTelemetry metrics associated with an {@link AbstractRocksDBStorage} instance
 */
public final class MetricsHolder implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsHolder.class);

    private static final String BLOCK_CACHE_USAGE_PROPERTY = "rocksdb.block-cache-usage";
    private static final String BLOCK_CACHE_PINNED_USAGE_PROPERTY = "rocksdb.block-cache-pinned-usage";
    private static final String ESTIMATE_TABLE_READERS_MEM_PROPERTY = "rocksdb.estimate-table-readers-mem";
    private static final String CURRENT_SIZE_ALL_MEM_TABLES_PROPERTY = "rocksdb.cur-size-all-mem-tables";

    private final Attributes dbAttributes;
    private final File dbDir;
    private TransactionDB db;
    private Statistics stats;
    private boolean closed = false;
    private final LongCounter transactions, readOnlyTransactions, writeTransactions;
    private final AtomicLong active = new AtomicLong(0);

    // These two lists track our observable metrics, i.e., those populated by callbacks, these are all close()'d in our
    // close() method to ensure OpenTelemetry doesn't keep trying to record these metrics after a RocksDB store has been
    // closed
    private final List<ObservableLongGauge> gauges = new ArrayList<>();
    private final List<ObservableLongCounter> counters = new ArrayList<>();

    /**
     * Creates a new metrics holder
     *
     * @param dbAttributes Database Attributes to attach to metrics
     * @param dbDir        Database directory
     * @param db           Database from which some metrics will be queried
     * @param stats        Database statistics tracker object
     */
    public MetricsHolder(Attributes dbAttributes, File dbDir, TransactionDB db, Statistics stats) {
        this.dbAttributes = Objects.requireNonNull(dbAttributes, "Database Metric Attributes cannot be null");
        this.dbDir = Objects.requireNonNull(dbDir, "Database directory cannot be null");
        this.db = Objects.requireNonNull(db, "Database cannot be null");
        this.stats = Objects.requireNonNull(stats, "Statistics cannot be null");

        Meter meter = TelicentMetrics.getMeter("rocksdb");

        //@formatter:off
        this.transactions = meter.counterBuilder(TRANSACTIONS)
                                 .setDescription(TRANSACTIONS_DESCRIPTION)
                                 .build();
        ObservableLongGauge activeTransactions
                = meter.gaugeBuilder(ACTIVE_TRANSACTIONS)
                       .setDescription(ACTIVE_TRANSACTIONS_DESCRIPTION)
                       .ofLongs()
                       .buildWithCallback(m -> {
                          if (!this.closed) {
                              m.record(this.active.get(), this.dbAttributes);
                          }
                       });
        this.gauges.add(activeTransactions);
        this.readOnlyTransactions
                = meter.counterBuilder(READONLY_TRANSACTIONS)
                       .setDescription(READONLY_TRANSACTIONS_DESCRIPTION)
                       .build();
        this.writeTransactions
                = meter.counterBuilder(WRITE_TRANSACTIONS)
                       .setDescription(WRITE_TRANSACTIONS_DESCRIPTION)
                       .build();
        ObservableLongGauge blockCacheUsage
                = meter.gaugeBuilder(BLOCK_CACHE_MEMORY_USAGE)
                       .setUnit("bytes")
                       .setDescription(BLOCK_CACHE_MEMORY_USAGE_DESCRIPTION)
                       .ofLongs()
                       .buildWithCallback(
                               m -> observeRocksDbProperty(m, BLOCK_CACHE_USAGE_PROPERTY));
        this.gauges.add(blockCacheUsage);
        ObservableLongGauge blockCachePinnedUsage
                = meter.gaugeBuilder(BLOCK_CACHE_PINNED_MEMORY_USAGE)
                       .setUnit("bytes")
                       .setDescription(BLOCK_CACHE_PINNED_MEMORY_USAGE_DESCRIPTION)
                       .ofLongs()
                       .buildWithCallback(m ->
                            observeRocksDbProperty(m, BLOCK_CACHE_PINNED_USAGE_PROPERTY));
        this.gauges.add(blockCachePinnedUsage);
        ObservableLongGauge tableReadersUsage
                = meter.gaugeBuilder(TABLE_READERS_MEMORY_USAGE)
                       .setUnit("bytes")
                       .setDescription(TABLE_READERS_MEMORY_USAGE_DESCRIPTION)
                       .ofLongs()
                       .buildWithCallback(m ->
                            observeRocksDbProperty(m, ESTIMATE_TABLE_READERS_MEM_PROPERTY));
        this.gauges.add(tableReadersUsage);
        ObservableLongGauge memtablesUsage
                = meter.gaugeBuilder(MEMTABLES_MEMORY_USAGE)
                       .setUnit("bytes")
                       .setDescription(MEMTABLES_MEMORY_USAGE_DESCRIPTION)
                       .ofLongs()
                       .buildWithCallback(m ->
                           observeRocksDbProperty(m, CURRENT_SIZE_ALL_MEM_TABLES_PROPERTY));
        this.gauges.add(memtablesUsage);
        ObservableLongGauge diskUsage
                = meter.gaugeBuilder(DISK_USAGE)
                       .setUnit("bytes")
                       .setDescription(DISK_USAGE_DESCRIPTION)
                       .ofLongs()
                       .buildWithCallback(m -> {
                           if (!this.closed) {
                               m.record(FileUtils.sizeOfDirectory(this.dbDir), this.dbAttributes);
                           }
                       });
        this.gauges.add(diskUsage);
        //@formatter:on

        // Create observables for each interesting RocksDB ticker that will expose those statistics out to OpenTelemetry
        for (TickerType ticker : INTERESTING_ROCKS_TICKERS) {
            ObservableLongCounter tickerCounter = buildForTicker(meter, ticker);
            this.counters.add(tickerCounter);
        }
    }

    /**
     * Creates an observable that records a metric from a RocksDB {@link Statistics} ticker counter
     * <p>
     * The resulting observable does not record a metric if the database is closed, the statistics object is invalid, or
     * RocksDB fails to report the requested ticker counter value successfully.
     * </p>
     *
     * @param meter  Meter to use in building the counter
     * @param ticker RocksDB ticker to record
     * @return Observable gauge
     */
    private ObservableLongCounter buildForTicker(Meter meter, TickerType ticker) {
        LongCounterBuilder builder = meter.counterBuilder(asMetricName(ticker));
        if (ticker.name().contains("BYTES")) {
            builder = builder.setUnit("bytes");
        }

        return builder.buildWithCallback(m -> {
            if (this.closed) {
                return;
            }
            Statistics currentStats = this.stats;
            if (currentStats == null) {
                return;
            }
            try {
                m.record(currentStats.getTickerCount(ticker), this.dbAttributes);
            } catch (Throwable e) {
                LOGGER.warn("Failed to record RocksDB ticker {}", ticker);
            }
        });
    }

    /**
     * Reports a metric by obtaining the current value of a RocksDB property via the
     * {@link TransactionDB#getProperty(String)} method.
     * <p>
     * If the database is closed, or fails to cleanly report the property value then no metric will be recorded.
     * </p>
     *
     * @param m        Measurement
     * @param property Property to record
     */
    private void observeRocksDbProperty(ObservableLongMeasurement m, String property) {
        if (this.closed) {
            return;
        }
        // Snapshot the reference: close() may null this.db (and close the native DB) concurrently with this callback
        // running on the OpenTelemetry collection thread. Reading it once and null-checking avoids an NPE, and
        // catching Throwable (not just RocksDBException) keeps a shutdown-race error off the collection thread.
        TransactionDB currentDb = this.db;
        if (currentDb == null) {
            return;
        }
        try {
            m.record(currentDb.getLongProperty(property), this.dbAttributes);
        } catch (Throwable e) {
            LOGGER.warn("Failed to track RocksDB metric {}", property);
        }
    }

    /**
     * Increments the transactions counter
     */
    public void incrementTransactions() {
        if (!this.closed) {
            this.transactions.add(1, this.dbAttributes);
        }
    }

    /**
     * Increments the active transactions gauge
     */
    public void incrementActiveTransactions() {
        if (!this.closed) {
            this.active.incrementAndGet();
        }
    }

    /**
     * Decrements the active transactions gauge
     */
    public void decrementActiveTransactions() {
        if (!this.closed) {
            this.active.decrementAndGet();
        }
    }

    /**
     * Increments the read-only transactions counter
     */
    public void incrementReadOnlyTransactions() {
        if (!this.closed) {
            this.readOnlyTransactions.add(1, this.dbAttributes);
        }
    }

    /**
     * Increments the write-only transactions counter
     */
    public void incrementWriteTransactions() {
        if (!this.closed) {
            this.writeTransactions.add(1, this.dbAttributes);
        }
    }

    /**
     * Closes the metrics holder ensuring that any observable metrics are also {@code close()}'d so that they no longer
     * report metrics for closed storage
     */
    @Override
    public void close() {
        boolean wasOpen = !this.closed;
        setClosedFlag();
        if (wasOpen) {
            // Close all our observables so they stop trying to invoke their callbacks and report metrics for closed
            // storage
            // Only need to do this the first time we are closed
            for (ObservableLongGauge gauge : this.gauges) {
                gauge.close();
            }
            for (ObservableLongCounter counter : this.counters) {
                counter.close();
            }

            // Also set our references to null
            // Note we don't close() these because its the AbstractRocksStorage that owns these and not the
            // MetricsHolder.  However, the fact we've been close()'d implies these references are likely to no longer
            // be valid so we should avoid holding them any longer
            this.db = null;
            this.stats = null;
        }
    }

    /**
     * Sets the closed flag to {@code true}
     * <p>
     * Package private visibility only for unit testing that once the holder is closed any observables stop collecting
     * stats.
     * </p>
     */
    void setClosedFlag() {
        this.closed = true;
    }
}
