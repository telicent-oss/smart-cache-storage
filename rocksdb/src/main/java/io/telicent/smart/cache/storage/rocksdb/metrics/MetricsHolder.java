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
import org.rocksdb.RocksDBException;
import org.rocksdb.Statistics;
import org.rocksdb.TickerType;
import org.rocksdb.TransactionDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import static io.telicent.smart.cache.storage.rocksdb.metrics.MetricNames.*;
import static io.telicent.smart.cache.storage.rocksdb.metrics.MetricNames.ACTIVE_TRANSACTIONS_DESCRIPTION;

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
    private TransactionDB db;
    private Statistics stats;
    private boolean closed = false;
    private final LongCounter transactions, readOnlyTransactions, writeTransactions;
    private final AtomicLong active = new AtomicLong(0);
    @SuppressWarnings("unused")
    private final ObservableLongGauge activeTransactions;
    @SuppressWarnings("unused")
    private final ObservableLongGauge blockCacheUsage, blockCachePinnedUsage, tableReadersUsage, memtablesUsage;
    private final List<ObservableLongCounter> statsTickers = new ArrayList<>();

    /**
     * Creates a new metrics holder
     *
     * @param dbAttributes Database Attributes to attach to metrics
     * @param db           Database from which some metrics will be queried
     * @param stats        Database statistics tracker object
     */
    public MetricsHolder(Attributes dbAttributes, TransactionDB db, Statistics stats) {
        this.dbAttributes = Objects.requireNonNull(dbAttributes, "Database Metric Attributes cannot be null");
        this.db = Objects.requireNonNull(db, "Database cannot be null");
        this.stats = Objects.requireNonNull(stats, "Statistics cannot be null");

        Meter meter = TelicentMetrics.getMeter("rocksdb");

        this.transactions = meter.counterBuilder(TRANSACTIONS).setDescription(TRANSACTIONS_DESCRIPTION).build();
        this.activeTransactions = meter.gaugeBuilder(ACTIVE_TRANSACTIONS)
                                       .setDescription(ACTIVE_TRANSACTIONS_DESCRIPTION)
                                       .ofLongs()
                                       .buildWithCallback(m -> {
                                           if (!this.closed) {
                                               m.record(this.active.get(), this.dbAttributes);
                                           }
                                       });
        this.readOnlyTransactions = meter.counterBuilder(READONLY_TRANSACTIONS).

                setDescription(READONLY_TRANSACTIONS_DESCRIPTION).

                                                 build();
        this.writeTransactions = meter.counterBuilder(WRITE_TRANSACTIONS).

                build();

        this.blockCacheUsage = meter.gaugeBuilder(BLOCK_CACHE_MEMORY_USAGE).

                setUnit("bytes").

                                            ofLongs().

                                            buildWithCallback(m ->

                                                              {
                                                                  observeRocksDbProperty(m, BLOCK_CACHE_USAGE_PROPERTY);
                                                              });
        this.blockCachePinnedUsage = meter.gaugeBuilder(BLOCK_CACHE_PINNED_MEMORY_USAGE).

                setUnit("bytes").

                                                  ofLongs().

                                                  buildWithCallback(m ->

                                                                            observeRocksDbProperty(m,
                                                                                                   BLOCK_CACHE_PINNED_USAGE_PROPERTY));
        this.tableReadersUsage = meter.gaugeBuilder(TABLE_READERS_MEMORY_USAGE).

                setUnit("bytes").

                                              ofLongs().

                                              buildWithCallback(m ->

                                                                        observeRocksDbProperty(m,
                                                                                               ESTIMATE_TABLE_READERS_MEM_PROPERTY));
        this.memtablesUsage = meter.gaugeBuilder(MEMTABLES_MEMORY_USAGE).

                setUnit("bytes").

                                           ofLongs().

                                           buildWithCallback(m ->

                                                                     observeRocksDbProperty(m,
                                                                                            CURRENT_SIZE_ALL_MEM_TABLES_PROPERTY));


        // Create observables for each interesting RocksDB ticker that will expose those statistics out to OpenTelemetry
        for (TickerType ticker : INTERESTING_ROCKS_TICKERS) {
            ObservableLongCounter tickerCounter = buildForTicker(meter, ticker);
            this.statsTickers.add(tickerCounter);
        }
    }

    private ObservableLongCounter buildForTicker(Meter meter, TickerType ticker) {
        LongCounterBuilder builder = meter.counterBuilder(asMetricName(ticker));
        if (ticker.name().contains("BYTES")) {
            builder = builder.setUnit("bytes");
        }

        return builder.buildWithCallback(m -> {
            if (!this.closed) {
                m.record(this.stats.getTickerCount(ticker), this.dbAttributes);
            }
        });
    }

    private void observeRocksDbProperty(ObservableLongMeasurement m, String property) {
        if (!this.closed) {
            try {
                m.record(this.db.getLongProperty(property), this.dbAttributes);
            } catch (RocksDBException e) {
                LOGGER.warn("Failed to track RocksDB metric {}", property);
            }
        }
    }

    public void incrementTransactions() {
        if (!this.closed) {
            this.transactions.add(1, this.dbAttributes);
        }
    }

    public void incrementActiveTransactions() {
        if (!this.closed) {
            this.active.incrementAndGet();
        }
    }

    public void decrementActiveTransactions() {
        if (!this.closed) {
            this.active.decrementAndGet();
        }
    }

    public void incrementReadOnlyTransactions() {
        if (!this.closed) {
            this.readOnlyTransactions.add(1, this.dbAttributes);
        }
    }

    public void incrementWriteTransactions() {
        if (!this.closed) {
            this.writeTransactions.add(1, this.dbAttributes);
        }
    }

    @Override
    public void close() {
        boolean wasOpen = !this.closed;
        setClosedFlag();
        if (wasOpen) {
            // Close all our observables so they stop trying to invoke their callbacks and report metrics for closed
            // storage
            // Only need to do this the first time we are closed
            this.activeTransactions.close();
            this.tableReadersUsage.close();
            this.blockCacheUsage.close();
            this.blockCachePinnedUsage.close();
            this.memtablesUsage.close();
            for (ObservableLongCounter tickerCounter : this.statsTickers) {
                tickerCounter.close();
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
