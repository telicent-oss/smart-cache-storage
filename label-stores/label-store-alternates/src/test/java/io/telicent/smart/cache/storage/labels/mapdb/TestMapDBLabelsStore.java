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
