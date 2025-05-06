/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.mongodb;

import com.mongodb.MongoCommandException;
import com.mongodb.MongoSecurityException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import io.telicent.smart.cache.configuration.Configurator;
import io.telicent.smart.cache.configuration.sources.PropertiesSource;
import io.telicent.smart.cache.storage.mongodb.cluster.MongoTestCluster;
import io.telicent.smart.cache.storage.mongodb.cluster.SecureMongoTestCluster;
import io.telicent.smart.cache.storage.mongodb.configuration.MongoConfiguration;
import org.bson.Document;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Properties;

public class DockerTestSecureMongoDBStorage extends DockerTestMongoDBStorage {

    public static final String USERNAME = "admin";
    public static final String PASSWORD = "some-credential-123";

    public DockerTestSecureMongoDBStorage() {
        this.mongo = SecureMongoTestCluster.builder().username(USERNAME).password(PASSWORD).build();
    }

    @Test
    public void givenExplicitAuthDb_whenConnecting_thenSuccess() {
        // Given
        String connectionString =
                this.mongo.getConnectionString() + "?authSource=" + MongoConfiguration.DEFAULT_AUTH_DATABASE;

        // When and Then
        verifySuccessfulConnection(connectionString);
    }

    @Test(expectedExceptions = MongoCommandException.class)
    public void givenNoCredentials_whenConnecting_thenFails() {
        // Given
        String connectionString = this.mongo.getPlainConnectionString();

        // When and Then
        verifySuccessfulConnection(connectionString);
    }

    @Test(expectedExceptions = MongoSecurityException.class)
    public void givenWrongAuthDb_whenConnecting_thenFails() {
        // Given
        String connectionString = this.mongo.getConnectionString() + "?authSource=test";

        // When and Then
        verifyFailedConnection(connectionString);
    }

    private static void verifyFailedConnection(String connectionString) {
        // When and Then
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            verifyFailedConnection(mongoClient);
        }
    }

    private static void verifyFailedConnection(MongoClient mongoClient) {
        // When
        mongoClient.getDatabase(MongoTestCluster.DEFAULT_TEST_DB).createCollection("test");

        // Then
        Assert.fail("Should have failed to connect and thrown an exception");
    }

    @Test
    public void givenScramSha256Mechanism_whenConnecting_thenSuccess() {
        // Given
        String connectionString = this.mongo.getConnectionString() + "?authMechanism=SCRAM-SHA-256";

        // When and Then
        verifySuccessfulConnection(connectionString);
    }

    private static void verifySuccessfulConnection(String connectionString) {
        // When and Then
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            verifySuccessfulConnection(mongoClient);
        }
    }

    private static void verifySuccessfulConnection(MongoClient mongoClient) {
        // When
        mongoClient.getDatabase(MongoTestCluster.DEFAULT_TEST_DB).createCollection("test");

        // Then
        MongoCollection<Document> collection =
                mongoClient.getDatabase(MongoTestCluster.DEFAULT_TEST_DB).getCollection("test");
        Assert.assertNotNull(collection);
    }

    @Test
    public void givenScramSha1Mechanism_whenConnecting_thenSuccess() {
        // Given
        String connectionString = this.mongo.getConnectionString() + "?authMechanism=SCRAM-SHA-1";

        // When and Then
        verifySuccessfulConnection(connectionString);
    }

    @Test
    public void givenPlainConnectionStringAndCredentialsInConfig_whenConfiguring_thenConnectsSuccessfully() {
        // Given
        String connectionString = this.mongo.getPlainConnectionString();
        Properties properties = createMongoConnectionProperties(connectionString);
        try {
            Configurator.setSingleSource(new PropertiesSource(properties));

            // When
            MongoConfiguration config = MongoConfiguration.fromConfigurator();

            // Then
            Assert.assertNotNull(config);
            try (MongoClient client = config.getClient()) {
                verifySuccessfulConnection(client);
            }

        } finally {
            Configurator.reset();
        }
    }

    private static Properties createMongoConnectionProperties(String connectionString) {
        Properties properties = new Properties();
        properties.put(MongoConfiguration.MONGO_URL, connectionString);
        properties.put(MongoConfiguration.MONGO_DATABASE, MongoTestCluster.DEFAULT_TEST_DB);
        properties.put(MongoConfiguration.MONGO_USER, USERNAME);
        properties.put(MongoConfiguration.MONGO_PASSWORD, PASSWORD);
        return properties;
    }

    @Test
    public void givenConnectionStringWithWrongCredentialsAndCredentialsInConfig_whenConfiguring_thenConfigCredentialsAllowConnectSuccessfully() {
        // NB - This test verifies that if the MONGO_USER and MONGO_PASSWORD settings are given then those take
        //      precedence over any credentials given in the connection string itself

        // Given
        String connectionString = this.mongo.getPlainConnectionString();
        connectionString = connectionString.replace("://", "://bad-user:bad-password@");
        Properties properties = createMongoConnectionProperties(connectionString);
        try {
            Configurator.setSingleSource(new PropertiesSource(properties));

            // When
            MongoConfiguration config = MongoConfiguration.fromConfigurator();

            // Then
            Assert.assertNotNull(config);
            try (MongoClient client = config.getClient()) {
                verifySuccessfulConnection(client);
            }

        } finally {
            Configurator.reset();
        }
    }

    @Test
    public void givenConnectionStringWithCredentials_whenConfiguring_thenConnectsSuccessfully() {
        // NB - This test verifies that if the credentials are given in the connection string they are used

        // Given
        String connectionString = this.mongo.getPlainConnectionString();
        connectionString = connectionString.replace("://", "://" + USERNAME + ":" + PASSWORD + "@");
        Properties properties = new Properties();
        properties.put(MongoConfiguration.MONGO_URL, connectionString);
        properties.put(MongoConfiguration.MONGO_DATABASE, MongoTestCluster.DEFAULT_TEST_DB);
        try {
            Configurator.setSingleSource(new PropertiesSource(properties));

            // When
            MongoConfiguration config = MongoConfiguration.fromConfigurator();

            // Then
            Assert.assertNotNull(config);
            try (MongoClient client = config.getClient()) {
                verifySuccessfulConnection(client);
            }

        } finally {
            Configurator.reset();
        }
    }

    @Test
    public void givenConnectionStringWithWrongAuthDBAndCorrectInConfig_whenConfiguring_thenConfigCredentialsAllowConnectSuccessfully() {
        // NB - This test verifies that if the MONGO_AUTH_DATABASE config is given then that takes
        //      precedence over any authSource given in the connection string itself

        // Given
        String connectionString = this.mongo.getPlainConnectionString();
        connectionString = connectionString + "?authSource=test";
        Properties properties = createMongoConnectionProperties(connectionString);
        properties.put(MongoConfiguration.MONGO_AUTH_DATABASE, MongoConfiguration.DEFAULT_AUTH_DATABASE);
        try {
            Configurator.setSingleSource(new PropertiesSource(properties));

            // When
            MongoConfiguration config = MongoConfiguration.fromConfigurator();

            // Then
            Assert.assertNotNull(config);
            try (MongoClient client = config.getClient()) {
                verifySuccessfulConnection(client);
            }

        } finally {
            Configurator.reset();
        }
    }

    @Test(expectedExceptions = MongoSecurityException.class)
    public void givenConnectionStringWithWrongAuthDB_whenConfiguring_thenConnectionFails() {
        // NB - This test verifies that an authSource in the connection string is honoured when configuring, even if
        //      it is the wrong authSource

        // Given
        String connectionString = this.mongo.getConnectionString();
        connectionString = connectionString + "?authSource=test";
        Properties properties = new Properties();
        properties.put(MongoConfiguration.MONGO_URL, connectionString);
        properties.put(MongoConfiguration.MONGO_DATABASE, MongoTestCluster.DEFAULT_TEST_DB);
        try {
            Configurator.setSingleSource(new PropertiesSource(properties));

            // When
            MongoConfiguration config = MongoConfiguration.fromConfigurator();

            // Then
            Assert.assertNotNull(config);
            try (MongoClient client = config.getClient()) {
                verifyFailedConnection(client);
            }

        } finally {
            Configurator.reset();
        }
    }

    @Test(expectedExceptions = MongoSecurityException.class)
    public void givenConnectionStringWithWrongAuthDBAndCredentialsInConfig_whenConfiguring_thenConnectionFails() {
        // NB - This test verifies that an authSource in the connection string is honoured when configuring, even if
        //      it is the wrong authSource, and the credentials are supplied

        // Given
        String connectionString = this.mongo.getConnectionString();
        connectionString = connectionString + "?authSource=test";
        Properties properties = createMongoConnectionProperties(connectionString);
        try {
            Configurator.setSingleSource(new PropertiesSource(properties));

            // When
            MongoConfiguration config = MongoConfiguration.fromConfigurator();

            // Then
            Assert.assertNotNull(config);
            try (MongoClient client = config.getClient()) {
                verifyFailedConnection(client);
            }

        } finally {
            Configurator.reset();
        }
    }
}
