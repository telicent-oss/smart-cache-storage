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
package io.telicent.smart.cache.storage.hibernate.configuration.h2;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

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
     * @throws IllegalArgumentException Thrown if the configuration is {@code null}, or invalid for use with an H2
     *                                  in-memory backend
     */
    public static Properties prepareInMemoryConnectionProperties(DatabaseConfiguration configuration) {
        validateConfiguration(configuration);

        Properties properties = new Properties();
        properties.put(JAKARTA_PERSISTENCE_JDBC_URL,
                       "jdbc:h2:mem:" + configuration.getDatabase() + ";DB_CLOSE_DELAY=-1");
        properties.put(JAKARTA_PERSISTENCE_JDBC_USER, "sa");
        properties.put(JAKARTA_PERSISTENCE_JDBC_PASSWORD, "");
        properties.put(HIBERNATE_DIALECT, "org.hibernate.dialect.H2Dialect");
        return properties;
    }

    /**
     * Validates that the provided database configuration is valid for forming H2 database connections
     *
     * @param configuration Database configuration
     * @throws IllegalArgumentException If the configuration is invalid in any way
     */
    private static void validateConfiguration(DatabaseConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("Insufficient configuration provided for H2 database connections");
        }
        if (StringUtils.isNotBlank(configuration.getJdbcUrl())) {
            throw new IllegalArgumentException(
                    "Explicit JDBC URL configuration not permitted for H2 database connections");
        }
        if (StringUtils.isBlank(configuration.getDatabase())) {
            throw new IllegalArgumentException("Must supply a database name for H2 database connections");
        }
    }

    /**
     * Prepares database connection properties for an H2 file database
     *
     * @param configuration Database configuration
     * @param dbBaseDir     Database base directory, when present then the database directory is resolved based on the
     *                      name provided in the {@link DatabaseConfiguration#getDatabase()} method relative to the base
     *                      directory.  If the base directory is {@code null} then it is ignored.
     * @return Connection properties
     * @throws IllegalArgumentException Thrown if the configuration is {@code null}, or invalid for use with an H2 file
     *                                  backend
     */
    public static Properties prepareFileConnectionProperties(DatabaseConfiguration configuration, File dbBaseDir) {
        validateConfiguration(configuration);

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
