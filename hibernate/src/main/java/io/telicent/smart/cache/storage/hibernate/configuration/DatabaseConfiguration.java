/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate.configuration;

import io.telicent.smart.cache.configuration.Configurator;
import lombok.*;

/**
 * A representation of basic database configuration, methods like
 * {@link
 * io.telicent.smart.cache.storage.hibernate.configuration.postgres.PostgresConfiguration#prepareConnectionProperties(DatabaseConfiguration)}
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class DatabaseConfiguration {

    /**
     * A configuration variable used to supply a database host
     */
    public static final String HOSTNAME = "DATABASE_HOST";
    /**
     * A configuration variable used to supply a database port
     */
    public static final String PORT = "DATABASE_PORT";
    /**
     * A configuration variable used to supply a database name
     */
    public static final String DB_NAME = "DATABASE_NAME";
    /**
     * A configuration variable used to supply a database username, see also {@link #USERNAME}
     */
    public static final String USER = "DATABASE_USER";
    /**
     * A configuration variable used to supply a database username, see also {@link #USER}
     */
    public static final String USERNAME = "DATABASE_USERNAME";
    /**
     * A configuration variable used to supply a database password
     */
    public static final String PASSWORD = "DATABASE_PASSWORD";

    @NonNull
    private final String hostname, database;
    private final Integer port;
    private final String username, password;

    /**
     * Gets the database configuration based upon using the {@link Configurator} API to retrieve the configuration based
     * upon the keys defined as constants on this class i.e.
     * <ul>
     *     <li>{@value #HOSTNAME} for database hostname</li>
     *     <li>{@value #PORT} for database port</li>
     *     <li>{@value #DB_NAME} for database name</li>
     *     <li>{@value #USERNAME}/{@value #USER} for
     *     database username</li>
     *     <li>{@value #PASSWORD} for database password</li>
     * </ul>
     *
     * @return Database configuration
     * @throws NullPointerException If any of the required configuration is missing
     */
    public static DatabaseConfiguration fromConfigurator() {
        return DatabaseConfiguration.builder()
                                    .hostname(Configurator.get(HOSTNAME))
                                    .port(Configurator.get(PORT, Integer::parseInt, null))
                                    .database(Configurator.get(DB_NAME))
                                    .username(Configurator.get(new String[] { USERNAME, USER }))
                                    .password(Configurator.get(PASSWORD))
                                    .build();
    }
}
