/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rocksdb.counters;

import io.telicent.smart.cache.storage.rocksdb.AbstractCounterTester;
import io.telicent.smart.cache.storage.rocksdb.RocksDBCounter;
import org.rocksdb.RocksDBException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class NoCounters extends AbstractCounterTester {

    public NoCounters(File dbDir) throws IOException, RocksDBException {
        super(dbDir);
    }

    @Override
    protected Map<String, RocksDBCounter> prepareCounters() throws RocksDBException {
        return Map.of();
    }
}
