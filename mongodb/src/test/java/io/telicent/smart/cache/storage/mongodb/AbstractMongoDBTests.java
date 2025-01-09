/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.testcontainers.containers.MongoDBContainer;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class AbstractMongoDBTests {
    public static final String DEFAULT_TEST_DB = "test";
    protected MongoDBContainer mongo;

    protected static void resetCollection(MongoClient client, String collection) {
        client.getDatabase(DEFAULT_TEST_DB)
              .getCollection(collection)
              .deleteMany(new Document());
    }

    @BeforeSuite
    public void setup() {
        this.mongo = new MongoDBContainer("mongo");
        this.mongo.start();
    }

    @AfterSuite
    public void teardown() {
        this.mongo.stop();
    }

    @NotNull
    protected MongoClient createMongoClient() {
        return MongoClients.create(this.mongo.getConnectionString());
    }
}
