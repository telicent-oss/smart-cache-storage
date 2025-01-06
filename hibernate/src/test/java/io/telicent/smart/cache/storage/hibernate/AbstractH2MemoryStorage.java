/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import static io.telicent.smart.cache.storage.hibernate.HibernateConfiguration.*;

/**
 * Abstract H2 in-memory backed storage for testing
 */
public abstract class AbstractH2MemoryStorage extends AbstractHibernateStorage {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    public static Properties prepareProperties() {
        return prepareProperties("test-db" + COUNTER.incrementAndGet());
    }

    public static Properties prepareProperties(String dbName) {
        Properties properties = new Properties();
        properties.put(JAKARTA_PERSISTENCE_JDBC_URL,
                       "jdbc:h2:mem:" + dbName + ";DB_CLOSE_DELAY=-1");
        properties.put(JAKARTA_PERSISTENCE_JDBC_USER, "sa");
        properties.put(JAKARTA_PERSISTENCE_JDBC_PASSWORD, "");
        properties.put(HIBERNATE_DIALECT, "org.hibernate.dialect.H2Dialect");
        properties.put(JAKARTA_PERSISTENCE_SCHEMA_GENERATION_ACTION, "create");
        return properties;
    }

    /**
     * Creates a new H2 backed store
     *
     * @param dbName          In-memory database name, will reconnect to a pre-existing in-memory database of the same
     *                        name if one exists
     * @param persistenceUnit The name of the persistence unit to use, this should generally contain only the basic
     *                        configuration e.g. Entity Classes, JPA Provider, generic JPA configuration.
     */
    public AbstractH2MemoryStorage(String dbName, String persistenceUnit) {
        super(prepareProperties(dbName), persistenceUnit);
    }

    /**
     * Creates a new H2 backed store
     *
     * @param persistenceUnit The name of the persistence unit to use, this should generally contain only the basic
     *                        configuration e.g. Entity Classes, JPA Provider, generic JPA configuration.
     */
    public AbstractH2MemoryStorage(String persistenceUnit) {
        super(prepareProperties(), persistenceUnit);
    }
}
