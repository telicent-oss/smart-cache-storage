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
        return String.format("jdbc:postgresql://%s:%d/%s?tcpKeepAlive=true", configuration.getHostname(),
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
