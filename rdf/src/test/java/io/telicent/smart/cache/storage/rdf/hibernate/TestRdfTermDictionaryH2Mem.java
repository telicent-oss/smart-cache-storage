/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rdf.hibernate;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.h2.H2Configuration;
import io.telicent.smart.cache.storage.rdf.AbstractRdfTermDictionaryTests;
import io.telicent.smart.cache.storage.rdf.RdfTermDictionary;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import static io.telicent.smart.cache.storage.hibernate.configuration.JpaConfiguration.JAKARTA_PERSISTENCE_SCHEMA_GENERATION_ACTION;

public class TestRdfTermDictionaryH2Mem extends AbstractRdfTermDictionaryTests {
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    protected RdfTermDictionary create() {
        String dbName = "test-" + counter.incrementAndGet();
        Properties props = H2Configuration.prepareInMemoryConnectionProperties(
                DatabaseConfiguration.builder().hostname("localhost").database(dbName).build());
        props.put(JAKARTA_PERSISTENCE_SCHEMA_GENERATION_ACTION, "create");
        return new HibernateRdfTermDictionary(props);
    }
}
