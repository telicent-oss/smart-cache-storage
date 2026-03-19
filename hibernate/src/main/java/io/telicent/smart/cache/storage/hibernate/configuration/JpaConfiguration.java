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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Provides constants related to JPA Configuration
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JpaConfiguration {

    /**
     * JPA configuration key used to set the JDBC URL
     */
    public static final String JAKARTA_PERSISTENCE_JDBC_URL = "jakarta.persistence.jdbc.url";
    /**
     * JPA configuration key used to set the JDBC Username
     */
    public static final String JAKARTA_PERSISTENCE_JDBC_USER = "jakarta.persistence.jdbc.user";
    /**
     * JPA configuration key used to set the JDBC Password
     */
    public static final String JAKARTA_PERSISTENCE_JDBC_PASSWORD = "jakarta.persistence.jdbc.password";
    /**
     * JPA configuration key used to set the Schema Generation action, note that if a storage backend derived from
     * {@link io.telicent.smart.cache.storage.hibernate.AbstractHibernateStorage} is using the optional Flyway
     * integration then there should be no need to set this property.
     */
    @SuppressWarnings("unused")
    public static final String JAKARTA_PERSISTENCE_SCHEMA_GENERATION_ACTION =
            "jakarta.persistence.schema-generation.database.action";
}
