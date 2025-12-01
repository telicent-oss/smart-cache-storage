/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.mongodb;

import io.telicent.smart.cache.storage.labels.CachingLabelsStore;
import io.telicent.smart.cache.storage.labels.LabelsStore;

public class DockerTestMongoLabelStoreWithCache extends DockerTestMongoLabelStore {
    @Override
    protected LabelsStore newStore() {
        return new CachingLabelsStore(super.newStore(), 1_000);
    }
}
