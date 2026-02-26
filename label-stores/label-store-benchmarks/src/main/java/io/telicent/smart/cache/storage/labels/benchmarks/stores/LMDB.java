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
import io.telicent.smart.cache.storage.labels.lmdb.LMDBLabelsStore;

import java.io.File;
import java.nio.file.Files;

public class LMDB implements StoreImplementation {

    File lmdbDir;

    @Override
    public LabelsStore newStore() {
        try {
            return new PlaceholderLabelsStore(new LMDBLabelsStore(lmdbDir.getAbsolutePath()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setup() {
        try {
        lmdbDir = Files.createTempDirectory("lmdb").toFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void teardown() {
        lmdbDir.delete();
    }

}
