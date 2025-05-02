package io.telicent.smart.cache.storage.mongodb.cluster;

import lombok.Builder;
import lombok.NonNull;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.util.Objects;

public class SecureMongoContainer extends GenericContainer<SecureMongoContainer> {
    private final String username, password;

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

    public String getConnectionString() {
        return String.format("mongodb://%s:%s@%s:%d", this.username, this.password, this.getHost(),
                             this.getMappedPort(27017));
    }

    public String getPlainConnectionString() {
        return String.format("mongodb://%s:%d", this.getHost(), this.getMappedPort(27017));
    }
}
