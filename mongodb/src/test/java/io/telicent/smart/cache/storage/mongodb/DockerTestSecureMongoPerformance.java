/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.mongodb;

import io.telicent.smart.cache.storage.mongodb.cluster.SecureMongoTestCluster;

public class DockerTestSecureMongoPerformance extends DockerTestMongoPerformance {

    public DockerTestSecureMongoPerformance() {
        this.mongo = SecureMongoTestCluster.builder()
                                           .username(DockerTestSecureMongoDBStorage.USERNAME)
                                           .password(DockerTestSecureMongoDBStorage.PASSWORD)
                                           .build();
    }
}
