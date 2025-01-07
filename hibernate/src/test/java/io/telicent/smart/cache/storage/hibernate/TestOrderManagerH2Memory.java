/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.h2.H2Configuration;
import io.telicent.smart.cache.storage.hibernate.model.OrderManager;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import static io.telicent.smart.cache.storage.hibernate.configuration.JpaConfiguration.JAKARTA_PERSISTENCE_SCHEMA_GENERATION_ACTION;

/**
 * Tests against H2 In-memory database
 */
public class TestOrderManagerH2Memory extends AbstractOrderManagerTests {
    private static final AtomicInteger counter = new AtomicInteger();

    @Override
    protected OrderManager createOrderManager() {
        String dbName = "test-" + counter.incrementAndGet();
        Properties props = H2Configuration.prepareInMemoryConnectionProperties(
                DatabaseConfiguration.builder().hostname("localhost").database(dbName).build());
        props.put(JAKARTA_PERSISTENCE_SCHEMA_GENERATION_ACTION, "create");
        return new OrderManager(props);
    }
}
