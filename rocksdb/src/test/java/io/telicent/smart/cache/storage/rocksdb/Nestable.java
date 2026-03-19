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

import org.rocksdb.ColumnFamilyDescriptor;
import org.rocksdb.ColumnFamilyOptions;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * A simple testbed for transaction nesting
 */
public final class Nestable extends AbstractRocksDBStorage {
    public static final byte[] KEY = "test".getBytes(StandardCharsets.UTF_8);
    public static final byte[] OTHER_KEY = "other".getBytes(StandardCharsets.UTF_8);

    public Nestable(File dbDir) throws RocksDBException, IOException {
        super(dbDir);
    }

    @Override
    protected List<ColumnFamilyDescriptor> prepareColumnFamilyDescriptors(ColumnFamilyOptions cfOptions) {
        return List.of(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOptions));
    }

    public void basic(boolean commit) throws RocksDBException {
        try (TransactionContext context = this.begin()) {
            context.put(this.getDefaultHandle(), KEY,
                        "basic".getBytes(StandardCharsets.UTF_8));
            if (commit) {
                context.commit();
            }
        }
    }

    public void nested(boolean commit) throws RocksDBException {
        try (TransactionContext context = this.beginNested()) {
            Assert.assertTrue(context.isActive());
            basic(true);
            context.put(this.getDefaultHandle(), KEY,
                        "nested".getBytes(StandardCharsets.UTF_8));
            if (commit) {
                context.commit();
            }
        }
    }

    public void deeplyNested(boolean commit) throws RocksDBException {
        try (TransactionContext context = this.beginNested()) {
            Assert.assertTrue(context.isActive());
            nested(true);
            nested(true);
            context.put(this.getDefaultHandle(), KEY,
                        "deeplyNested".getBytes(StandardCharsets.UTF_8));
            if (commit) {
                context.commit();
            }
        }
    }

    public void combined(boolean commit) throws RocksDBException {
        try (TransactionContext context = this.beginNested()) {
            this.nested(true);
            context.put(this.getDefaultHandle(), OTHER_KEY,
                        "combined".getBytes(
                                StandardCharsets.UTF_8));
            if (commit) {
                context.commit();
            }
        }
    }

    public String get() throws RocksDBException {
        return get(KEY);
    }

    public String get(byte[] key) throws RocksDBException {
        try (TransactionContext context = this.begin()) {
            verifyNotANestedTransaction(context);
            byte[] value = context.get(this.getDefaultHandle(), key);
            return value != null ? new String(value, StandardCharsets.UTF_8) : null;
        }
    }

    private static void verifyNotANestedTransaction(TransactionContext context) {
        Assert.assertFalse(context instanceof NestedTransactionContext,
                           "Nested transaction not properly closed or incorrectly reused");
    }

    public List<byte[]> multiGet(List<byte[]> keys) throws RocksDBException {
        try (TransactionContext context = this.begin()) {
            verifyNotANestedTransaction(context);
            return context.multiGetAsList(
                    keys.stream().map(k -> this.getDefaultHandle()).toList(), keys);
        }
    }
}
