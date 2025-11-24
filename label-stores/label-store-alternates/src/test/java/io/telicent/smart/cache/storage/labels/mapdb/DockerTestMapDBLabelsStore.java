/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.mapdb;

import io.telicent.smart.cache.storage.labels.AbstractDictionaryLabelStoreTests;
import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DockerTestMapDBLabelsStore extends AbstractDictionaryLabelStoreTests {

    Path tempDir;
    Path dbFile;

    @Override
    protected DictionaryLabelsStore newStore() {
        try {
            return new MapDbLabelsStore(dbFile.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeClass
    public void setup() throws IOException {
        tempDir = Files.createTempDirectory("mapdb");
        dbFile = tempDir.resolve("labels.db");
    }

    @AfterClass
    public void cleanUp() throws IOException {
            Files.delete(dbFile);
            Files.delete(tempDir);
    }
}
