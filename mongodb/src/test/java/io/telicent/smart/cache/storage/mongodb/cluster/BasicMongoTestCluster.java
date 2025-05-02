/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.mongodb.cluster;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.testcontainers.containers.MongoDBContainer;

import java.io.PrintStream;

/**
 * A basic MongoDB test cluster with no authentication
 */
public class BasicMongoTestCluster implements MongoTestCluster {


    protected MongoDBContainer mongo;

    public void setup() {
        this.mongo = createContainer();
        long start = ClusterUtils.logStart("Starting MongoDB test cluster");
        this.mongo.start();
        ClusterUtils.logFinished("Started MongoDB test cluster", start);
    }

    /**
     * Creates the MongoDB container, potentially adding any extra customisation/configuration needed for the test
     * cluster
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
