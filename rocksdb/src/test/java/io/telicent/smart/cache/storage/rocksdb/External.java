/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rocksdb;

import org.rocksdb.ColumnFamilyDescriptor;
import org.rocksdb.ColumnFamilyOptions;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class External extends AbstractRocksDBStorage {

    public External(File dbDir) throws IOException, RocksDBException {
        super(dbDir);
    }

    @Override
    protected List<ColumnFamilyDescriptor> prepareColumnFamilyDescriptors(ColumnFamilyOptions cfOptions) {
        return List.of(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOptions));
    }

    /**
     * Starts and returns a transaction
     *
     * @return Nested transaction context
     */
    public TransactionContext start() {
        return this.beginNested();
    }
}
