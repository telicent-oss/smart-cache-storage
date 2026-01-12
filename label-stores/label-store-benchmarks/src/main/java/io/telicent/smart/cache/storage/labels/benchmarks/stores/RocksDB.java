/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks.stores;

import io.telicent.smart.cache.storage.labels.LabelsStore;
import io.telicent.smart.cache.storage.labels.rocksdb.RocksDbLabelsStore;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class RocksDB implements StoreImplementation {

    File baseDir;

    @Override
    public LabelsStore newStore() {
        try {
            return new RocksDbLabelsStore(baseDir.getAbsoluteFile());
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
        try {
            FileUtils.deleteDirectory(baseDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
