/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks.stores;

import com.mongodb.client.MongoClient;
import io.telicent.smart.cache.storage.labels.LabelsStore;
import io.telicent.smart.cache.storage.labels.mongodb.MongoDBLabelsStore;
import io.telicent.smart.cache.storage.mongodb.cluster.BasicMongoTestCluster;
import io.telicent.smart.cache.storage.mongodb.cluster.MongoTestCluster;

/**
 * Tests the {@link MongoDBLabelsStore} which uses a MongoDB backend
 */
public class MongoDB implements StoreImplementation {

    private MongoTestCluster mongo;

    @Override
    public LabelsStore newStore() {
        return new MongoDBLabelsStore(this.mongo.createMongoClient(), MongoTestCluster.DEFAULT_TEST_DB);
    }

    @Override
    public void setup() {
        if (this.mongo == null) {
            this.mongo = new BasicMongoTestCluster();
            this.mongo.setup();
        }
    }

    @Override
    public void teardown() {
        if (this.mongo != null) {
            if (this.mongo.isRunning()) {
                try (MongoClient client = this.mongo.createMongoClient()) {
                    MongoTestCluster.resetCollection(client, MongoDBLabelsStore.ENCODED_LABELS_COLLECTION);
                }
            }
        }
    }
}
