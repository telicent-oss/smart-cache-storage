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
