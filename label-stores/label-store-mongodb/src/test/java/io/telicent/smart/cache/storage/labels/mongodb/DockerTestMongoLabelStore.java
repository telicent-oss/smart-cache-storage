/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.mongodb;

import com.mongodb.client.MongoClient;
import io.telicent.smart.cache.storage.labels.AbstractLabelStoreTests;
import io.telicent.smart.cache.storage.labels.LabelsStore;
import io.telicent.smart.cache.storage.mongodb.cluster.BasicMongoTestCluster;
import io.telicent.smart.cache.storage.mongodb.cluster.ClusterUtils;
import io.telicent.smart.cache.storage.mongodb.cluster.MongoTestCluster;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class DockerTestMongoLabelStore extends AbstractLabelStoreTests {
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

    @BeforeMethod
    public void resetCollection() {
        if (this.mongo != null) {
            if (this.mongo.isRunning()) {
                try (MongoClient client = this.mongo.createMongoClient()) {
                    MongoTestCluster.resetCollection(client, MongoDBLabelsStore.ENCODED_LABELS_COLLECTION);
                    MongoTestCluster.resetCollection(client, MongoDBLabelsStore.ASSIGNED_LABELS_COLLECTION);
                }
            }
        }
    }


    @Override
    protected LabelsStore newStore() {
        return new MongoDBLabelsStore(this.mongo.createMongoClient(), MongoTestCluster.DEFAULT_TEST_DB);
    }
}
