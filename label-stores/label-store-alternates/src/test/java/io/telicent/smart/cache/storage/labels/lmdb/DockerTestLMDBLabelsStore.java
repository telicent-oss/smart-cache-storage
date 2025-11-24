/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.lmdb;

import io.telicent.smart.cache.storage.labels.AbstractDictionaryLabelStoreTests;
import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DockerTestLMDBLabelsStore extends AbstractDictionaryLabelStoreTests {

    File lmdbDir;

    @Override
    protected DictionaryLabelsStore newStore() {
        try {
            return new LMDBLabelsStore(lmdbDir.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeClass
    public void setup() throws IOException {
        lmdbDir = Files.createTempDirectory("lmdb").toFile();
    }

    @AfterClass
    public void cleanUp() {
        lmdbDir.delete();
    }
}
