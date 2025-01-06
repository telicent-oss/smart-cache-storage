/**
 * Copyright (C) 2022 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate;

import java.util.Properties;

/**
 * Provides useful constants relating to Hibernate configuration
 */
public final class HibernateConfiguration {

    public static final String DATABASE_HOST = "DATABASE_HOST";
    public static final String DATABASE_PORT = "DATABASE_PORT";
    public static final String DATABASE_NAME = "DATABASE_NAME";
    public static final String DATABASE_USER = "DATABASE_USER";
    public static final String DATABASE_USERNAME = "DATABASE_USERNAME";
    public static final String DATABASE_PASSWORD = "DATABASE_PASSWORD";
    public static final String HIBERNATE_DIALECT = "hibernate.dialect";
    public static final String JAKARTA_PERSISTENCE_JDBC_URL = "jakarta.persistence.jdbc.url";
    public static final String JAKARTA_PERSISTENCE_JDBC_USER = "jakarta.persistence.jdbc.user";
    public static final String JAKARTA_PERSISTENCE_JDBC_PASSWORD = "jakarta.persistence.jdbc.password";
    public static final String JAKARTA_PERSISTENCE_SCHEMA_GENERATION_ACTION =
            "jakarta.persistence.schema-generation.database.action";
    public static final String HIBERNATE_DIALECT_POSTGRES = "org.hibernate.dialect.PostgreSQLDialect";
    public static final String DEFAULT_PERSISTENCE_UNIT = "telicent-notifications";

    /**
     * The default Postgres Port
     */
    public static final int DEFAULT_POSTGRES_PORT = 5432;

    /**
     * Private constructor to prevent instantiation
     */
    private HibernateConfiguration() {

    }

    /**
     * Constructs a JDBC URL for Postgres based on general connection parameters
     *
     * @param dbHost Database Host
     * @param dbPort Database Port
     * @param dbName Database Name
     * @return Postgres JDBC URL
     */
    public static String getPostgresJdbcUrl(String dbHost, Integer dbPort, String dbName) {
        return "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
    }

    /**
     * Prepares Postgres JPA connection properties
     *
     * @param dbHost     Database Host
     * @param dbPort     Database Port
     * @param dbName     Database Name
     * @param dbUsername Database Username
     * @param dbPassword Database Password
     * @return Connection properties
     */
    public static Properties preparePostgresConnectionProperties(String dbHost, Integer dbPort, String dbName,
                                                                 String dbUsername, String dbPassword) {
        Properties properties = new Properties();
        properties.put(HIBERNATE_DIALECT, HIBERNATE_DIALECT_POSTGRES);
        properties.put(JAKARTA_PERSISTENCE_JDBC_URL,
                       getPostgresJdbcUrl(dbHost, dbPort, dbName));
        if (dbUsername != null) {
            properties.put(JAKARTA_PERSISTENCE_JDBC_USER, dbUsername);
        }
        if (dbPassword != null) {
            properties.put(JAKARTA_PERSISTENCE_JDBC_PASSWORD, dbPassword);
        }
        return properties;
    }
}
