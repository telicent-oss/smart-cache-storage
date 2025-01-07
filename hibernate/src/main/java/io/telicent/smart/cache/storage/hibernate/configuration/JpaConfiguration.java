/**
 * Copyright (C) 2024-2025 Telicent Limited
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
     * JPA configuration key used to set the Schema Generation action
     */
    public static final String JAKARTA_PERSISTENCE_SCHEMA_GENERATION_ACTION =
            "jakarta.persistence.schema-generation.database.action";
}
