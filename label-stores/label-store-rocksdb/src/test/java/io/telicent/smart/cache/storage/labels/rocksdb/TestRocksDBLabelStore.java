/**
 * Copyright (C) Telicent Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.telicent.smart.cache.storage.labels.rocksdb;

import io.telicent.smart.cache.storage.labels.AbstractLabelStoreTests;
import io.telicent.smart.cache.storage.labels.LabelsStore;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class TestRocksDBLabelStore extends AbstractLabelStoreTests {

    private static final byte[] KEY = "rocks-key".getBytes(StandardCharsets.UTF_8);

    private File rocksDir;

    @Override
    protected LabelsStore newStore() {
        try {
            return new RocksDbLabelsStore(rocksDir.getAbsoluteFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void givenAssignedLabel_whenGettingLabelBytesViaCollapsedReadPath_thenLabelReturned() {
        // Given
        byte[] label = { 9, 8, 7 };
        try (LabelsStore store = newStore()) {
            long labelId = store.idForLabel(label);
            store.setLabel(KEY, labelId);

            // When (getLabelAsBytes resolves key -> id -> label in a single read-only transaction)
            byte[] retrieved = store.getLabelAsBytes(KEY);

            // Then
            Assert.assertEquals(retrieved, label);
        }
    }

    @Test
    public void givenNonExistentKey_whenGettingLabelBytes_thenNull() {
        // Given
        try (LabelsStore store = newStore()) {
            // When and Then
            Assert.assertNull(store.getLabelAsBytes(KEY));
        }
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void givenNullKey_whenGettingLabelBytes_thenNPE() {
        // Given
        try (LabelsStore store = newStore()) {
            // When and Then
            store.getLabelAsBytes(null);
        }
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void givenEmptyKey_whenGettingLabelBytes_thenNPE() {
        // Given
        try (LabelsStore store = newStore()) {
            // When and Then
            store.getLabelAsBytes(new byte[0]);
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
