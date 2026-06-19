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

import org.rocksdb.TickerType;

import java.util.Locale;

/**
 * Holds constants relating to RocksDB metric names
 */
public final class MetricNames {

    /**
     * An array of RocksDB statistics tickers that we consider interesting enough to expose via OpenTelemetry metrics
     */
    static final TickerType[] INTERESTING_ROCKS_TICKERS = {
            TickerType.BLOCK_CACHE_HIT,
            TickerType.BLOCK_CACHE_MISS,
            TickerType.BLOCK_CACHE_BYTES_READ,
            TickerType.BLOOM_FILTER_USEFUL,
            TickerType.PERSISTENT_CACHE_HIT,
            TickerType.PERSISTENT_CACHE_MISS,
            TickerType.MEMTABLE_HIT,
            TickerType.MEMTABLE_MISS,
            TickerType.NUMBER_KEYS_READ,
            TickerType.NUMBER_KEYS_WRITTEN,
            TickerType.BYTES_READ,
            TickerType.BYTES_WRITTEN,
            TickerType.ITER_BYTES_READ,
            TickerType.NO_ITERATOR_CREATED,
            TickerType.NO_FILE_OPENS,
            TickerType.NO_FILE_ERRORS,
            TickerType.COMPACT_READ_BYTES,
            TickerType.COMPACT_WRITE_BYTES,
            TickerType.FLUSH_WRITE_BYTES,
            TickerType.ROW_CACHE_HIT,
            TickerType.ROW_CACHE_MISS,
            TickerType.READ_AMP_ESTIMATE_USEFUL_BYTES,
            TickerType.READ_AMP_TOTAL_READ_BYTES,
            TickerType.BACKUP_READ_BYTES,
            TickerType.BACKUP_WRITE_BYTES
    };

    /**
     * Private constructor prevents instantiation
     */
    private MetricNames() {

    }

    public static final String TRANSACTIONS = "rocksdb.transactions";
    public static final String TRANSACTIONS_DESCRIPTION =
            "Number of RocksDB transactions used over the lifetime of the storage";

    public static final String ACTIVE_TRANSACTIONS = "rocksdb.transactions.active";
    public static final String ACTIVE_TRANSACTIONS_DESCRIPTION = "Number of currently active RocksDB transactions";

    public static final String READONLY_TRANSACTIONS = "rocksdb.transactions.readonly";
    public static final String READONLY_TRANSACTIONS_DESCRIPTION =
            "Number of readonly RocksDB transactions i.e. transactions that did not commit() and thus did not write to the database";

    public static final String WRITE_TRANSACTIONS = "rocksdb.transactions.write";

    public static final String BLOCK_CACHE_MEMORY_USAGE = "rocksdb.block-cache.memory.usage";
    public static final String BLOCK_CACHE_PINNED_MEMORY_USAGE = "rocksdb.block-cache.pinned.memory.usage";
    public static final String TABLE_READERS_MEMORY_USAGE = "rocksdb.table-readers.memory.usage";
    public static final String MEMTABLES_MEMORY_USAGE = "rocksdb.memtables.memory.usage";

    /**
     * Gets the names of all Telicent implemented RocksDB metrics
     * @see #names()
     */
    public static final String[] TELICENT_METRICS = {
            TRANSACTIONS,
            ACTIVE_TRANSACTIONS,
            READONLY_TRANSACTIONS,
            WRITE_TRANSACTIONS,
            BLOCK_CACHE_MEMORY_USAGE,
            BLOCK_CACHE_PINNED_MEMORY_USAGE,
            TABLE_READERS_MEMORY_USAGE,
            MEMTABLES_MEMORY_USAGE
    };

    /**
     * Gets the names of all RocksDB metrics that we track and expose via OpenTelemetry
     * <p>
     * This is the combination of the names from {@link #TELICENT_METRICS} and the names derived from the
     * {@link #INTERESTING_ROCKS_TICKERS}.
     * </p>
     *
     * @return All names
     */
    public static String[] names() {
        String[] names = new String[TELICENT_METRICS.length + INTERESTING_ROCKS_TICKERS.length];
        System.arraycopy(TELICENT_METRICS, 0, names, 0, TELICENT_METRICS.length);
        for (int i = 0; i < INTERESTING_ROCKS_TICKERS.length; i++) {
            names[i + TELICENT_METRICS.length] = asMetricName(INTERESTING_ROCKS_TICKERS[i]);
        }
        return names;
    }

    /**
     * Given a RocksDB TickerType enum value convert it into a metric name
     * <p>
     * More specifically we use the prefix {@code rocksdb.stats.} and then take the enum constant name and apply the
     * following transformations:
     * </p>
     * <ol>
     *     <li>Remove {@code BYTES} from the name if present</li>
     *     <li>Remove any resulting double underscores</li>
     *     <li>Convert underscores to periods</li>
     *     <li>Convert to lowercase</li>
     * </ol>
     *
     * @param ticker Ticker Type
     * @return Metric Name
     */
    public static String asMetricName(TickerType ticker) {
        return String.format("rocksdb.stats.%s",
                             ticker.name()
                                   .replace("BYTES", "")
                                   .replace("__", "_")
                                   .replace("_", ".")
                                   .toLowerCase(Locale.ROOT));
    }
}
