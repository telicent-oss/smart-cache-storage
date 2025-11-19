/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.rocksdb;

import io.telicent.smart.cache.storage.labels.AbstractDictionaryLabelStoreTests;
import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DockerTestRocksDBLabelStore  extends AbstractDictionaryLabelStoreTests {

    File rocksDir;

    @Override
    protected DictionaryLabelsStore newStore() {
        try {
            return new RocksDbLabelsStore(rocksDir.getPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeClass
    public void setup() throws IOException {
        rocksDir = Files.createTempDirectory("rocks").toFile();
    }

    @AfterClass
    public void cleanUp() {
        rocksDir.delete();
    }
}
