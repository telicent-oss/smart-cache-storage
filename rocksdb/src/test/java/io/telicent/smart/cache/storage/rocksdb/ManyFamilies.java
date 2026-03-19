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

import org.rocksdb.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ManyFamilies extends AbstractRocksDBStorage {

    public static final List<String> NAMES = generateNames();

    private static List<String> generateNames() {
        List<String> names = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            names.add(String.valueOf(c));
        }
        return names;
    }

    public ManyFamilies(File dbDir) throws IOException, RocksDBException {
        super(dbDir);
    }

    @Override
    protected List<ColumnFamilyDescriptor> prepareColumnFamilyDescriptors(ColumnFamilyOptions cfOptions) {
        ArrayList<ColumnFamilyDescriptor> descriptors = new ArrayList<>();
        descriptors.add(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOptions));
        for (String name : NAMES) {
            descriptors.add(new ColumnFamilyDescriptor(name.getBytes(StandardCharsets.UTF_8), cfOptions));
        }
        return descriptors;
    }

    public void put(String family, byte[] key, byte[] value) throws RocksDBException {
        ColumnFamilyHandle handle = handle(family);
        try (TransactionContext context = this.begin()) {
            context.put(handle, key, value);
            context.commit();
        }
    }

    private ColumnFamilyHandle handle(String family) {
        ColumnFamilyHandle handle = this.getHandle(family.getBytes(StandardCharsets.UTF_8));
        if (handle == null) {
            throw new IllegalArgumentException("Invalid column family");
        }
        return handle;
    }

    public byte[] get(String family, byte[] key) throws RocksDBException {
        ColumnFamilyHandle handle = handle(family);
        try (TransactionContext context = this.begin()) {
            return context.get(handle, key);
        }
    }

    public void drop(String family) throws RocksDBException {
        this.dropColumnFamily(handle(family));
    }
}
