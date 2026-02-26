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
import org.bson.Document;

/**
 * Interface for MongoDB test clusters to abstract spinning clusters up and down in unit/integration tests
 */
public interface MongoTestCluster {

    /**
     * Default Test Database
     */
    String DEFAULT_TEST_DB = "test";

    /**
     * Resets the given collection, i.e. deletes all documents from it, in the default test database
     *
     * @param client     Client
     * @param collection Collection to reset
     */
    static void resetCollection(MongoClient client, String collection) {
        resetCollection(client, DEFAULT_TEST_DB, collection);
    }

    /**
     * Resets a collection, i.e. deletes all documents form it, in the given database
     *
     * @param client     Client
     * @param db         Database
     * @param collection Collection
     */
    static void resetCollection(MongoClient client, String db, String collection) {
        long start = ClusterUtils.logStart(String.format("Resetting collection %s in database %s", collection, db));
        client.getDatabase(db).getCollection(collection).deleteMany(new Document());
        ClusterUtils.logFinished(String.format("Reset collection %s in database %s", collection, db), start);
    }

    /**
     * Sets up, i.e. creates and starts, the MongoDB cluster
     */
    void setup();

    /**
     * Tears down, i.e. stops and destroys, the MongoDB cluster
     */
    void teardown();

    /**
     * Gets a connection string for the cluster containing <strong>ONLY</strong> the host and port, depending on the
     * cluster implementation this may be insufficient to connect to the cluster and additional parameters, e.g. auth
     * credentials, may need to be injected in other ways
     *
     * @return Plain connection string
     */
    String getPlainConnectionString();

    /**
     * Gets a connection string for connecting to the cluster, this connection string <strong>MUST</strong> contain all
     * parameters necessary to connect
     *
     * @return Connection string
     */
    String getConnectionString();

    /**
     * Gets a {@link MongoClient} for interacting with the cluster
     * <p>
     * Implementations <strong>MUST</strong> guarantee that the returned client can connect to the cluster so the client
     * <strong>MUST</strong> be configured with all necessary parameters to do so.
     * </p>
     *
     * @return Mongo Client
     */
    MongoClient createMongoClient();

    /**
     * Gets whether the cluster is running
     *
     * @return True if running, false otherwise
     */
    boolean isRunning();
}
