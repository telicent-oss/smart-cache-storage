/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks.stores;

import io.telicent.smart.cache.storage.labels.LabelsStore;
import io.telicent.smart.cache.storage.labels.lmdb.LMDBLabelsStore;

import java.io.File;
import java.nio.file.Files;

public class LMDB implements StoreImplementation {

    File lmdbDir;

    @Override
    public LabelsStore newStore() {
        try {
            return new PlaceholderLabelsStore(new LMDBLabelsStore(lmdbDir.getAbsolutePath()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setup() {
        try {
        lmdbDir = Files.createTempDirectory("lmdb").toFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void teardown() {
        lmdbDir.delete();
    }

}
