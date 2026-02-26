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
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * A MongoDB test cluster that has authentication enabled with a single administrative user by default
 */
@Builder
public class SecureMongoTestCluster implements MongoTestCluster {
    @NonNull
    @Getter
    private String username, password;

    private SecureMongoContainer mongo;

    @Override
    public void setup() {
        this.mongo = new SecureMongoContainer(this.username, this.password);
        long start = ClusterUtils.logStart("Starting Secure MongoDB test cluster");
        this.mongo.start();
        ClusterUtils.logFinished("Started Secure MongoDB test cluster", start);
    }

    @Override
    public void teardown() {
        long start = ClusterUtils.logStart("Stopping Secure MongoDB test cluster");
        this.mongo.stop();
        this.mongo.close();
        ClusterUtils.logFinished("Stopped Secure MongoDB test cluster", start);
    }

    @Override
    public String getPlainConnectionString() {
        return this.mongo.getPlainConnectionString();
    }

    @Override
    public String getConnectionString() {
        return this.mongo.getConnectionString();
    }

    @Override
    public MongoClient createMongoClient() {
        return MongoClients.create(this.mongo.getConnectionString());
    }

    @Override
    public boolean isRunning() {
        return this.mongo.isRunning();
    }
}
