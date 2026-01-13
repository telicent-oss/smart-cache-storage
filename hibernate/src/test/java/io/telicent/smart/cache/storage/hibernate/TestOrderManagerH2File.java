/**
 * Copyright (C) 2024-2025 Telicent Limited
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
