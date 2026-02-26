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

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.h2.H2Configuration;
import io.telicent.smart.cache.storage.labels.LabelsStore;
import io.telicent.smart.cache.storage.labels.hibernate.HibernateLabelsStore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tests the {@link HibernateLabelsStore} with an H2 file backend
 */
public class H2File implements StoreImplementation{
    private static final AtomicInteger counter = new AtomicInteger();

    private File tempDir;

    @Override
    public LabelsStore newStore() {
        String dbName = "benchmark-" + counter.incrementAndGet();
        Properties props = H2Configuration.prepareFileConnectionProperties(
                DatabaseConfiguration.builder().hostname("localhost").database(dbName).build(), this.tempDir);
        return new HibernateLabelsStore(props);
    }

    @Override
    public void setup() {
        if (tempDir == null) {
            try {
                this.tempDir = Files.createTempDirectory("h2db").toFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void teardown() {

    }
}
