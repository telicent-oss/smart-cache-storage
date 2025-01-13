/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.mongodb.configuration;

import io.telicent.smart.cache.configuration.Configurator;
import io.telicent.smart.cache.configuration.sources.NullSource;
import io.telicent.smart.cache.configuration.sources.PropertiesSource;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.Properties;

public class TestMongoConfiguration {

    @AfterMethod
    public void cleanup() {
        Configurator.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Mongo URL not configured")
    public void givenNoConfiguration_whenConfiguringMongo_thenFails() {
        // Given
        Configurator.setSingleSource(NullSource.INSTANCE);

        // When and Then
        MongoConfiguration.fromConfigurator();
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void givenNoDatabase_whenConfiguringMongo_thenFails() {
        // Given
        Properties props = new Properties();
        props.put(MongoConfiguration.MONGO_URL, "mongodb://localhost:27017");
        Configurator.setSingleSource(new PropertiesSource(props));

        // When and Then
        MongoConfiguration.fromConfigurator();
    }

    @Test
    public void givenMinimalConfig_whenConfiguringMongo_thenSuccess() {
        // Given
        Properties props = new Properties();
        props.put(MongoConfiguration.MONGO_URL, "mongodb://localhost:27017");
        props.put(MongoConfiguration.MONGO_DATABASE, "test");
        Configurator.setSingleSource(new PropertiesSource(props));

        // When
        MongoConfiguration config = MongoConfiguration.fromConfigurator();

        // Then
        Assert.assertNotNull(config);
        Assert.assertEquals(config.getDatabase(), "test");
        config.getClient().close();
    }

    @Test
    public void givenNoConfigAndDefaults_whenConfiguringMongo_thenDefaultsAreUsed() {
        // Given
        Configurator.setSingleSource(NullSource.INSTANCE);

        // When
        MongoConfiguration config = MongoConfiguration.fromConfigurator("mongodb://localhost:27017",
                                                                        "test",
                                                                        MongoConfiguration.DEFAULT_AUTH_DATABASE,
                                                                        "user", "password");

        // Then
        Assert.assertNotNull(config.getClient());
        Assert.assertEquals(config.getDatabase(), "test");
    }
}
