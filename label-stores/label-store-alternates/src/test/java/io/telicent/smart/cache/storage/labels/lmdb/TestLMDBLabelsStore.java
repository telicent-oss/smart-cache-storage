/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.lmdb;

import io.telicent.smart.cache.storage.labels.AbstractDictionaryLabelStoreTests;
import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestLMDBLabelsStore extends AbstractDictionaryLabelStoreTests {

    private File lmdbDir;

    @Override
    protected DictionaryLabelsStore newDictionaryStore() {
        try {
            return new LMDBLabelsStore(lmdbDir.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeMethod
    public void setup() throws IOException {
        lmdbDir = Files.createTempDirectory("lmdb").toFile();
    }

    @AfterMethod
    public void cleanUp() {
        FileUtils.deleteQuietly(lmdbDir);
    }
}
