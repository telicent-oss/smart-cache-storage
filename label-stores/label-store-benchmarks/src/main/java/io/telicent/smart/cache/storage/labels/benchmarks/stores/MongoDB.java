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
