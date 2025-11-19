/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks.stores;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.rocksdb.RocksDbLabelsStore;

import java.io.File;
import java.nio.file.Files;

public class RocksDB implements StoreImplementation {

    File baseDir;

    @Override
    public DictionaryLabelsStore newStore() {
        try {
            return new RocksDbLabelsStore(baseDir.getPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setup() {
        try {
            baseDir = Files.createTempDirectory("rocks").toFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void teardown() {
        baseDir.delete();
    }
}
