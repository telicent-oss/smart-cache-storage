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

import io.telicent.smart.cache.storage.*;
import org.rocksdb.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An example implementation of the BackupRestoreCapable and CompactCapable interfaces.
 */
public class RocksDBStorageForTest extends AbstractRocksDBStorage
        implements BackupRestoreCapable, CompactCapable {

    public RocksDBStorageForTest(File dbDir) throws IOException, RocksDBException {
        super(dbDir);
    }

    @Override
    protected List<ColumnFamilyDescriptor> prepareColumnFamilyDescriptors(ColumnFamilyOptions cfOptions) {
        List<ColumnFamilyDescriptor> descriptors = new ArrayList<>();
        descriptors.add(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOptions));
        return descriptors;
    }

    public void put(byte[] key, byte[] value) throws RocksDBException {
        try (TransactionContext context = begin()) {
            context.put(getDefaultHandle(), key, value);
            context.commit();
        }
    }

    public byte[] get(byte[] key) throws RocksDBException {
        try (TransactionContext context = begin()) {
            byte[] value = context.get(getDefaultHandle(), key);
            return (value != null && value.length == 0) ? null : value;
        }
    }

    public void delete(byte[] key) throws RocksDBException {
        try (TransactionContext context = begin()) {
            getTransactionDB().delete(getDefaultHandle(), key);
        }
    }

    public long count() {
        try (TransactionContext context = begin()) {
            return context.count(getDefaultHandle());
        }
    }

    public void flush() throws RocksDBException {
        try (FlushOptions flushOptions = new FlushOptions().setWaitForFlush(true)) {
            getTransactionDB().flush(flushOptions);
        }
    }
}
