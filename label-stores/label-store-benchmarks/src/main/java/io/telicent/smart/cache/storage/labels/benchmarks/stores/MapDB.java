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
package io.telicent.smart.cache.storage.labels.benchmarks.stores;

import io.telicent.smart.cache.storage.labels.LabelsStore;
import io.telicent.smart.cache.storage.labels.mapdb.MapDbLabelsStore;

import java.nio.file.Files;
import java.nio.file.Path;

public class MapDB implements StoreImplementation {
    Path tempDir;
    Path dbFile;

    @Override
    public LabelsStore newStore() {
        try {
            return new PlaceholderLabelsStore(new MapDbLabelsStore(dbFile.toString()));
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
