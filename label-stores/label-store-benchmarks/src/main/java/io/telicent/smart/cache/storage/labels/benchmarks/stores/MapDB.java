/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks.stores;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.mapdb.MapDbLabelsStore;

import java.nio.file.Files;
import java.nio.file.Path;

public class MapDB implements StoreImplementation {
    Path tempDir;
    Path dbFile;

    @Override
    public DictionaryLabelsStore newStore() {
        try {
            return new MapDbLabelsStore(dbFile.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setup() {
        try {
            tempDir = Files.createTempDirectory("mapdb");
            dbFile = tempDir.resolve("labels.db");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void teardown()  {
        try {
            Files.delete(dbFile);
            Files.delete(tempDir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
