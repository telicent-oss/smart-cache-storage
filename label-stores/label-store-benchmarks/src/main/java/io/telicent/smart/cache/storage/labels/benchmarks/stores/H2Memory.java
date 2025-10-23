/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks.stores;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.h2.H2Configuration;
import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.hibernate.HibernateLabelsStore;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

public class H2Memory implements StoreImplementation{
    private final AtomicLong counter = new AtomicLong();

    @Override
    public DictionaryLabelsStore newStore() {
        String dbName = "benchmark-" + counter.incrementAndGet();
        Properties props = H2Configuration.prepareInMemoryConnectionProperties(
                DatabaseConfiguration.builder().hostname("localhost").database(dbName).build());
        return new HibernateLabelsStore(props);
    }

    @Override
    public void setup() {

    }

    @Override
    public void teardown() {

    }
}
