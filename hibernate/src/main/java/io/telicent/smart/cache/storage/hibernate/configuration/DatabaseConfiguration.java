/**
 * Copyright (C) Telicent Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.telicent.smart.cache.storage.hibernate.configuration;

import io.telicent.smart.cache.configuration.Configurator;
import io.telicent.smart.cache.storage.hibernate.configuration.postgres.PostgresConfiguration;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * A representation of basic database configuration, methods like
 * {@link PostgresConfiguration#prepareConnectionProperties(DatabaseConfiguration)} use this to construct concrete JPA
 * properties that can be used to establish a connection to the database
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class DatabaseConfiguration {

    /**
     * A configuration variable used to supply a full JDBC URL, if this is specified then any {@value #HOSTNAME},
     * {@value #PORT} and {@value #DB_NAME} values are ignored in favour of this URL.
     * <p>
     * Note that for security reasons it is strongly recommended to not specify the credentials in the URL and the
     * {@value #USER}/{@value #USERNAME} and {@value #PASSWORD} variables will still be honoured.
     * </p>
     */
    public static final String JDBC_URL = "DATABASE_JDBC_URL";
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

    private final String jdbcUrl;
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
        return fromConfigurator(null, null, null, null, null);
    }

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
     * <p>
     * The default values supplied are used if no configuration is available from the configuration API.
     * </p>
     * <p>
     * Since {@code 0.7.0} this may return incomplete configuration as there are now multiple ways to supply
     * configuration and this method does not know what is considered sufficient for a given database.  The
     * {@link #isValid()} method will give a general indication of whether enough configuration is present but depending
     * on the underlying database backend being used this may be insufficient/incompatible with that database.
     * </p>
     *
     * @param defaultHostname Default database hostname
     * @param defaultPort     Default database port
     * @param defaultDatabase Default database name
     * @param defaultUsername Default username
     * @param defaultPassword Default password
     * @return Database configuration
     */
    public static DatabaseConfiguration fromConfigurator(String defaultHostname, Integer defaultPort,
                                                         String defaultDatabase, String defaultUsername,
                                                         String defaultPassword) {
        return DatabaseConfiguration.builder()
                                    .jdbcUrl(Configurator.get(JDBC_URL))
                                    .hostname(Configurator.get(new String[] { HOSTNAME }, defaultHostname))
                                    .port(Configurator.get(PORT, Integer::parseInt, defaultPort))
                                    .database(Configurator.get(new String[] { DB_NAME }, defaultDatabase))
                                    .username(Configurator.get(new String[] { USERNAME, USER }, defaultUsername))
                                    .password(Configurator.get(new String[] { PASSWORD }, defaultPassword))
                                    .build();
    }

    /**
     * Determines whether the given database configuration is valid i.e. does it contain either a JDBC URL or both a
     * Hostname and Database name.  Either combination is considered the minimum permitted valid configuration.
     *
     * @return True if valid, false otherwise
     */
    public boolean isValid() {
        return StringUtils.isNotBlank(this.jdbcUrl) || StringUtils.isNoneBlank(this.hostname, this.database);
    }
}
