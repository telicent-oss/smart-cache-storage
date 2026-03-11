/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rocksdb.counters;

import io.telicent.smart.cache.storage.rocksdb.AbstractCounterTester;
import io.telicent.smart.cache.storage.rocksdb.RocksDBCounter;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AlphabetCounters extends AbstractCounterTester {

    private static List<String> createNames() {
        List<String> names = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            names.add(String.valueOf(c));
        }
        return names;
    }

    public static final List<String> NAMES = createNames();

    public AlphabetCounters(File dbDir) throws IOException, RocksDBException {
        super(dbDir);
    }

    @Override
    protected Map<String, RocksDBCounter> prepareCounters() throws RocksDBException {
        Map<String, RocksDBCounter> counters = new LinkedHashMap<>();
        for (String name : NAMES) {
            counters.put(name, createCounter(RocksDB.DEFAULT_COLUMN_FAMILY, name));
        }
        return counters;
    }
}
