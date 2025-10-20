/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.hibernate;

import io.telicent.smart.cache.storage.CachingLabelsStore;
import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;

public class TestHibernateLabelsStoreH2MemoryWithCache extends TestHibernateLabelsStoreH2Memory {

    @Override
    protected DictionaryLabelsStore newStore() {
        return new CachingLabelsStore(super.newStore(), 1_000);
    }
}
