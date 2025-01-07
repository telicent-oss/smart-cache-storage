/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate.configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Provides useful constants relating to Hibernate configuration
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HibernateConfiguration {

    /**
     * A configuration variable used to supply a database host
     */
    public static final String DATABASE_HOST = "DATABASE_HOST";
    /**
     * A configuration variable used to supply a database port
     */
    public static final String DATABASE_PORT = "DATABASE_PORT";
    /**
     * A configuration variable used to supply a database name
     */
    public static final String DATABASE_NAME = "DATABASE_NAME";
    /**
     * A configuration variable used to supply a database username, see also {@link #DATABASE_USERNAME}
     */
    public static final String DATABASE_USER = "DATABASE_USER";
    /**
     * A configuration variable used to supply a database username, see also {@link #DATABASE_USER}
     */
    public static final String DATABASE_USERNAME = "DATABASE_USERNAME";
    /**
     * A configuration variable used to supply a database password
     */
    public static final String DATABASE_PASSWORD = "DATABASE_PASSWORD";

    /**
     * JPA configuration key used to set the Hibernate SQL Dialect
     */
    public static final String HIBERNATE_DIALECT = "hibernate.dialect";

}
