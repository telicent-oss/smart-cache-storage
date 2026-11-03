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

import org.rocksdb.Cache;
import org.rocksdb.ColumnFamilyDescriptor;
import org.rocksdb.ColumnFamilyOptions;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * A testbed for restores attempted while transactions remain open, see {@link TestRestoreWithOpenTransactions}.
 * <p>
 * As well as exposing the {@code begin*()} methods it offers a probe that runs partway through a restore, at the point
 * where the old database has been closed and the new one is about to be opened, which is the window in which a
 * transaction must not be obtainable.
 * </p>
 */
public final class RestoreHazardStorage extends AbstractRocksDBStorage {

    public static final byte[] KEY = "key".getBytes(StandardCharsets.UTF_8);

    private boolean probeDuringRestore = false;
    private Boolean restoringDuringReopen;

    public RestoreHazardStorage(File dbDir) throws RocksDBException, IOException {
        super(dbDir);
    }

    @Override
    protected List<ColumnFamilyDescriptor> prepareColumnFamilyDescriptors(ColumnFamilyOptions cfOptions) {
        return List.of(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOptions));
    }

    @Override
    protected Cache createDefaultBlockCache() {
        // NB - This is the first thing openInternal() does, so when it runs as part of a restore the old database has
        //      already been closed and the new one does not exist yet, i.e. we are inside the restore window
        // NB - Deliberately only records the flag rather than calling begin(), because if the flag were ever false
        //      here then begin() would hand out a transaction against the closed database and abort the JVM, taking
        //      the rest of the test suite with it.  The flag being set is what guarantees ensureNotClosed() refuses.
        if (this.probeDuringRestore) {
            this.restoringDuringReopen = this.restoring;
        }
        return super.createDefaultBlockCache();
    }

    /**
     * Enables the mid-restore probe, see {@link #getRestoringDuringReopen()} for what it records
     */
    public void probeDuringRestore() {
        this.probeDuringRestore = true;
    }

    /**
     * @return Whether the restoring flag was set when the database was being reopened, {@code null} if the probe never
     * ran
     */
    public Boolean getRestoringDuringReopen() {
        return this.restoringDuringReopen;
    }

    public TransactionContext transaction() {
        return this.begin();
    }

    public TransactionContext readOnlyTransaction() {
        return this.beginReadOnly();
    }

    public TransactionContext nestedTransaction() {
        return this.beginNested();
    }

    public void put(byte[] key, byte[] value) throws RocksDBException {
        try (TransactionContext context = this.begin()) {
            context.put(this.getDefaultHandle(), key, value);
            context.commit();
        }
    }

    public byte[] get(byte[] key) throws RocksDBException {
        try (TransactionContext context = this.begin()) {
            return context.get(this.getDefaultHandle(), key);
        }
    }

    public long count() {
        try (TransactionContext context = this.begin()) {
            return context.count(this.getDefaultHandle());
        }
    }

}
