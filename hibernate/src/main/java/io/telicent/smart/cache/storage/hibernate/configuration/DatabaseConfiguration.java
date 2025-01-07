/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate.configuration;

import io.telicent.smart.cache.configuration.Configurator;
import lombok.*;

import static io.telicent.smart.cache.storage.hibernate.configuration.HibernateConfiguration.*;

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

    @NonNull
    private final String hostname, database;
    private final Integer port;
    private final String username, password;

    /**
     * Gets the database configuration based upon using the {@link Configurator} API to retrieve the configuration based
     * upon the keys defined as constants on {@link HibernateConfiguration} i.e.
     * <ul>
     *     <li>{@value HibernateConfiguration#DATABASE_HOST} for database hostname</li>
     *     <li>{@value HibernateConfiguration#DATABASE_PORT} for database port</li>
     *     <li>{@value HibernateConfiguration#DATABASE_NAME} for database name</li>
     *     <li>{@value HibernateConfiguration#DATABASE_USERNAME}/{@value HibernateConfiguration#DATABASE_USER} for
     *     database username</li>
     *     <li>{@value HibernateConfiguration#DATABASE_PASSWORD} for database password</li>
     * </ul>
     *
     * @return Database configuration
     * @throws NullPointerException If any of the required configuration is missing
     */
    public static DatabaseConfiguration fromConfigurator() {
        return DatabaseConfiguration.builder()
                                    .hostname(Configurator.get(DATABASE_HOST))
                                    .port(Configurator.get(DATABASE_PORT, Integer::parseInt, null))
                                    .database(Configurator.get(DATABASE_NAME))
                                    .username(Configurator.get(new String[] { DATABASE_USERNAME, DATABASE_USER }))
                                    .password(Configurator.get(DATABASE_PASSWORD))
                                    .build();
    }
}
