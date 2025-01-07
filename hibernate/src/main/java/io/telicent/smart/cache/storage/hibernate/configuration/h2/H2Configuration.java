/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate.configuration.h2;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.Properties;

import static io.telicent.smart.cache.storage.hibernate.configuration.JpaConfiguration.*;
import static io.telicent.smart.cache.storage.hibernate.configuration.HibernateConfiguration.HIBERNATE_DIALECT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class H2Configuration {

    /**
     * Prepares database connection properties for an H2 in-memory database
     *
     * @param configuration Database configuration
     * @return Connection properties
     */
    public static Properties prepareInMemoryConnectionProperties(DatabaseConfiguration configuration) {
        Properties properties = new Properties();
        properties.put(JAKARTA_PERSISTENCE_JDBC_URL,
                       "jdbc:h2:mem:" + configuration.getDatabase() + ";DB_CLOSE_DELAY=-1");
        properties.put(JAKARTA_PERSISTENCE_JDBC_USER, "sa");
        properties.put(JAKARTA_PERSISTENCE_JDBC_PASSWORD, "");
        properties.put(HIBERNATE_DIALECT, "org.hibernate.dialect.H2Dialect");
        return properties;
    }

    /**
     * Prepares database connection properties for an H2 file database
     *
     * @param configuration Database configuration
     * @param dbBaseDir     Database base directory, when present then the database directory is resolved based on the
     *                      name provided in the {@link DatabaseConfiguration#getDatabase()} method relative to the base
     *                      directory.  If the base directory is {@code null} then it is ignored.
     * @return Connection properties
     */
    public static Properties prepareFileConnectionProperties(DatabaseConfiguration configuration, File dbBaseDir) {
        Properties properties = new Properties();
        properties.put(JAKARTA_PERSISTENCE_JDBC_URL,
                       "jdbc:h2:file:" + resolveDatabaseDirectory(configuration, dbBaseDir).getAbsolutePath());
        properties.put(JAKARTA_PERSISTENCE_JDBC_USER, "sa");
        properties.put(JAKARTA_PERSISTENCE_JDBC_PASSWORD, "");
        properties.put(HIBERNATE_DIALECT, "org.hibernate.dialect.H2Dialect");
                return properties;
    }

    /**
     * Resolves the database directory
     *
     * @param configuration Database configuration, the {@link DatabaseConfiguration#getDatabase()} is used to refer to
     *                      a specific directory
     * @param dbBaseDir     When not {@code null} the directory is resolved relative to this base
     * @return Resolved database directory
     */
    public static File resolveDatabaseDirectory(DatabaseConfiguration configuration, File dbBaseDir) {
        if (dbBaseDir != null) {
            return new File(dbBaseDir, configuration.getDatabase());
        } else {
            return new File(configuration.getDatabase());
        }
    }
}
