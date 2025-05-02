/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.mongodb.cluster;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.util.Objects;

/**
 * A test container that runs MongoDB with authentication enabled
 * <p>
 * Due to Test Containers bug <a href="https://github.com/testcontainers/testcontainers-java/issues/4695">#4695</a> the
 * official {@link org.testcontainers.containers.MongoDBContainer} used by {@link BasicMongoTestCluster} does not work
 * with authentication as it sets up a Replica Set for which enabling authentication is more involved that what this
 * container does here.  This test container is based upon various hints/suggestions/workaround from the GitHub issue
 * discussion and as seen in {@link io.telicent.smart.cache.storage.mongodb.DockerTestSecureMongoDBStorage} is
 * sufficient for testing a variety of authentication success and failure scenarios.
 * </p>
 */
public class SecureMongoContainer extends GenericContainer<SecureMongoContainer> {
    private final String username, password;

    /**
     * Creates a new secure MongoDB container
     *
     * @param username Admin username
     * @param password Admin password
     */
    public SecureMongoContainer(String username, String password) {
        super(DockerImageName.parse("mongo"));
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNull(password);
        addEnv("MONGO_INITDB_ROOT_USERNAME", this.username);
        addEnv("MONGO_INITDB_ROOT_PASSWORD", this.password);
        addEnv("MONGO_INITDB_DATABASE", "mongodb");
        addExposedPorts(27017);
        setWaitStrategy(Wait.forLogMessage("(?i).*Waiting for connections*.*", 1));
    }

    /**
     * Gets the connection string including credentials
     *
     * @return Connection string
     */
    public String getConnectionString() {
        return String.format("mongodb://%s:%s@%s:%d", this.username, this.password, this.getHost(),
                             this.getMappedPort(27017));
    }

    /**
     * Gets the connection string excluding credentials
     *
     * @return Connection string
     */
    public String getPlainConnectionString() {
        return String.format("mongodb://%s:%d", this.getHost(), this.getMappedPort(27017));
    }
}
