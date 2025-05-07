/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.mongodb;

import io.telicent.smart.cache.storage.mongodb.cluster.BasicMongoTestCluster;
import io.telicent.smart.cache.storage.mongodb.cluster.ClusterUtils;
import io.telicent.smart.cache.storage.mongodb.cluster.MongoTestCluster;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class AbstractMongoDBTests {
    /**
     * Protected so tests can override this to use a different test cluster e.g. SecureMongoTestCluster
     */
    protected MongoTestCluster mongo = new BasicMongoTestCluster();

    private long started;

    @BeforeClass
    public void setup() {
        this.started = ClusterUtils.logStart("Starting tests " + this.getClass().getCanonicalName());
        this.mongo.setup();
    }

    @AfterClass
    public void teardown() {
        this.mongo.teardown();
        ClusterUtils.logFinished("Finished tests " + this.getClass().getCanonicalName(), this.started);
    }
}
