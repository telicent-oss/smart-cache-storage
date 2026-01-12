/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.rocksdb;

import io.telicent.smart.cache.storage.labels.AbstractLabelStoreTests;
import io.telicent.smart.cache.storage.labels.LabelsStore;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestRocksDBLabelStore extends AbstractLabelStoreTests {

    private File rocksDir;

    @Override
    protected LabelsStore newStore() {
        try {
            return new RocksDbLabelsStore(rocksDir.getAbsoluteFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeMethod
    public void setup() throws IOException {
        rocksDir = Files.createTempDirectory("rocks").toFile();
    }

    @AfterMethod
    public void cleanUp() throws IOException {
        // Walk and delete directory tree properly
        FileUtils.deleteDirectory(rocksDir);
    }
}
