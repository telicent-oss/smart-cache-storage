/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.mapdb;

import io.telicent.smart.cache.storage.labels.AbstractDictionaryLabelStoreTests;
import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestMapDBLabelsStore extends AbstractDictionaryLabelStoreTests {

    Path tempDir;
    Path dbFile;

    @Override
    protected DictionaryLabelsStore newDictionaryStore() {
        try {
            return new MapDbLabelsStore(dbFile.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeMethod
    public void setup() throws IOException {
        tempDir = Files.createTempDirectory("mapdb");
        dbFile = tempDir.resolve("labels.db");
    }

    @AfterMethod
    public void cleanUp() throws IOException {
        FileUtils.deleteDirectory(tempDir.toFile());
    }
}
