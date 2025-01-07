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
     * JPA configuration key used to set the Hibernate SQL Dialect
     */
    public static final String HIBERNATE_DIALECT = "hibernate.dialect";

}
