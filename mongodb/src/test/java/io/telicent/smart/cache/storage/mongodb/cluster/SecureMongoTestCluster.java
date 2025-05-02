package io.telicent.smart.cache.storage.mongodb.cluster;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.io.PrintStream;

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
