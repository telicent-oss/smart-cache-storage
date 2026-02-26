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
package io.telicent.smart.cache.storage.mongodb.cluster;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.testcontainers.containers.MongoDBContainer;

/**
 * A basic MongoDB test cluster with no authentication using the standard Test Containers {@link MongoDBContainer}
 * <p>
 * To test with MongoDB authentication use the {@link SecureMongoTestCluster} instead.
 * </p>
 */
public class BasicMongoTestCluster implements MongoTestCluster {

    /**
     * The MongoDB container, populated by calling the {@link #createContainer()} method when {@link #setup()} is
     * called
     */
    protected MongoDBContainer mongo;

    @Override
    public void setup() {
        this.mongo = createContainer();
        long start = ClusterUtils.logStart("Starting MongoDB test cluster");
        this.mongo.start();
        ClusterUtils.logFinished("Started MongoDB test cluster", start);
    }

    /**
     * Creates the MongoDB container, potentially adding any extra customisation/configuration needed for the test
     * cluster
     * <p>
     * By default, this is a plain {@link MongoDBContainer}
     * </p>
     *
     * @return MongoDB Container
     */
    protected MongoDBContainer createContainer() {
        return new MongoDBContainer("mongo");
    }

    @Override
    public void teardown() {
        long start = ClusterUtils.logStart("Stopping MongoDB test cluster");
        this.mongo.stop();
        this.mongo.close();
        ClusterUtils.logFinished("Stopped MongoDB test cluster", start);
    }

    @Override
    public boolean isRunning() {
        return this.mongo.isRunning();
    }

    @Override
    public String getPlainConnectionString() {
        return this.mongo.getConnectionString();
    }

    @Override
    public String getConnectionString() {
        return this.mongo.getConnectionString();
    }

    @Override
    public MongoClient createMongoClient() {
        return MongoClients.create(this.mongo.getConnectionString());
    }
}
