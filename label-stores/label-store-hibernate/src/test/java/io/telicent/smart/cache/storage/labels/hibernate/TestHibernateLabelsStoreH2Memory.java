/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.hibernate;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.h2.H2Configuration;
import io.telicent.smart.cache.storage.labels.AbstractLabelStoreTests;
import io.telicent.smart.cache.storage.labels.LabelsStore;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class TestHibernateLabelsStoreH2Memory extends AbstractLabelStoreTests {
    private static final AtomicInteger counter = new AtomicInteger();

    @Override
    protected LabelsStore newStore() {
        String dbName = "test-" + counter.incrementAndGet();
        Properties props = H2Configuration.prepareInMemoryConnectionProperties(
                DatabaseConfiguration.builder().hostname("localhost").database(dbName).build());
        return new HibernateLabelsStore(props);
    }
}
