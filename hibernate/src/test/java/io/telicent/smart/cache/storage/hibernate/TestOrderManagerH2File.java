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
package io.telicent.smart.cache.storage.hibernate;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.h2.H2Configuration;
import io.telicent.smart.cache.storage.hibernate.model.OrderManager;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tests against H2 file database
 */
public class TestOrderManagerH2File extends AbstractOrderManagerTests {

    private File tempDir;
    private final AtomicInteger counter = new AtomicInteger(0);

    @BeforeClass
    public void setup() throws IOException {
        this.tempDir = Files.createTempDirectory("h2db").toFile();
    }

    @AfterClass
    public void teardown() {
        FileUtils.deleteQuietly(this.tempDir);
    }

    @Override
    protected OrderManager createOrderManager() {
        String dbName = "test-" + counter.incrementAndGet();
        Properties props = H2Configuration.prepareFileConnectionProperties(
                DatabaseConfiguration.builder().hostname("localhost").database(dbName).build(), this.tempDir);
        return new OrderManager(props);
    }
}
