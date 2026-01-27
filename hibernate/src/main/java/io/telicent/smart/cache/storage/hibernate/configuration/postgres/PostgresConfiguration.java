/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate.configuration.postgres;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.HibernateConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.JpaConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostgresConfiguration {
    /**
     * The Hibernate Postgres SQL Dialect
     */
    public static final String HIBERNATE_DIALECT_POSTGRES = "org.hibernate.dialect.PostgreSQLDialect";

    /**
     * The default Postgres Port
     */
    public static final int DEFAULT_PORT = 5432;

    /**
     * Constructs a JDBC URL for Postgres based on general connection parameters
     *
     * @param configuration Database Configuration
     * @return Postgres JDBC URL
     */
    public static String getJdbcUrl(DatabaseConfiguration configuration) {
        return String.format("jdbc:postgresql://%s:%d/%s", configuration.getHostname(),
                             configuration.getPort() != null ?
                             configuration.getPort() :
                             DEFAULT_PORT, configuration.getDatabase());
    }

    /**
     * Prepares Postgres JPA connection properties based on general connection parameters
     *
     * @param configuration Database configuration
     * @return Connection properties
     * @throws IllegalArgumentException Thrown if the provided configuration is {@code null} or invalid
     */
    public static Properties prepareConnectionProperties(DatabaseConfiguration configuration) {
        if (configuration == null || !configuration.isValid()) {
            throw new IllegalArgumentException("Insufficient configuration to establish a Postgres connection");
        }

        Properties properties = new Properties();
        properties.put(HibernateConfiguration.HIBERNATE_DIALECT, HIBERNATE_DIALECT_POSTGRES);
        properties.put(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_URL,
                       StringUtils.isNotBlank(configuration.getJdbcUrl()) ? configuration.getJdbcUrl() :
                       getJdbcUrl(configuration));
        if (StringUtils.isNotBlank(configuration.getUsername())) {
            properties.put(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_USER, configuration.getUsername());
        }
        if (StringUtils.isNotBlank(configuration.getPassword())) {
            properties.put(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_PASSWORD, configuration.getPassword());
        }
        return properties;
    }
}
