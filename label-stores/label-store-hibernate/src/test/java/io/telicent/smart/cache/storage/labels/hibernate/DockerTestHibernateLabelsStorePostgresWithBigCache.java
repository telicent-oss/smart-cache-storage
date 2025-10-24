/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.hibernate;

import io.telicent.smart.cache.storage.labels.CachingLabelsStore;
import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;

public class DockerTestHibernateLabelsStorePostgresWithBigCache extends DockerTestHibernateLabelsStorePostgres{
    @Override
    protected DictionaryLabelsStore newStore() {
        return new CachingLabelsStore(super.newStore(), 20_000);
    }
}
