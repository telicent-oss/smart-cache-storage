/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.h2.H2Configuration;
import io.telicent.smart.cache.storage.hibernate.model.JsonStore;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class TestJsonStoreH2Memory extends AbstractJsonStorageTests {
    private static final AtomicInteger counter = new AtomicInteger();

    @Override
    protected JsonStore createJsonStore() {
        String dbName = "test-json-" + counter.incrementAndGet();
        Properties props = H2Configuration.prepareInMemoryConnectionProperties(
                DatabaseConfiguration.builder().hostname("localhost").database(dbName).build());
        return new JsonStore(props);
    }

}
