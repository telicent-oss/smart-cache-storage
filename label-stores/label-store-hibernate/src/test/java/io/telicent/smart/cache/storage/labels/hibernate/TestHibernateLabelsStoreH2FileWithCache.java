/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.hibernate;

import io.telicent.smart.cache.storage.labels.CachingLabelsStore;
import io.telicent.smart.cache.storage.labels.LabelsStore;

public class TestHibernateLabelsStoreH2FileWithCache extends TestHibernateLabelsStoreH2File {

    @Override
    protected LabelsStore newStore() {
        return new CachingLabelsStore(super.newStore(), 1_000);
    }
}
