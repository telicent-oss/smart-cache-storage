/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.mongodb.configuration;

import com.mongodb.ConnectionString;
import io.telicent.smart.cache.configuration.Configurator;
import io.telicent.smart.cache.configuration.sources.NullSource;
import io.telicent.smart.cache.configuration.sources.PropertiesSource;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
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
    public void givenFullConfig_whenConfiguringMongo_thenSuccess() {
        // Given
        Properties props = new Properties();
        props.put(MongoConfiguration.MONGO_URL, "mongodb://localhost:27017");
        props.put(MongoConfiguration.MONGO_DATABASE, "test");
        props.put(MongoConfiguration.MONGO_USER, "user");
        props.put(MongoConfiguration.MONGO_PASSWORD, "password");
        props.put(MongoConfiguration.MONGO_AUTH_DATABASE, "users");
        Configurator.setSingleSource(new PropertiesSource(props));

        // When
        MongoConfiguration config = MongoConfiguration.fromConfigurator();

        // Then
        Assert.assertNotNull(config);
        Assert.assertEquals(config.getDatabase(), "test");
        config.getClient().close();
    }

    @Test
    public void givenFullConfigWithUrlAndProperties_whenConfiguringMongo_thenSuccess() {
        // Given
        Properties props = new Properties();
        props.put(MongoConfiguration.MONGO_URL, "mongodb://other:credential@localhost:27017?authSource=admin");
        props.put(MongoConfiguration.MONGO_DATABASE, "test");
        props.put(MongoConfiguration.MONGO_USER, "user");
        props.put(MongoConfiguration.MONGO_PASSWORD, "password");
        props.put(MongoConfiguration.MONGO_AUTH_DATABASE, "users");
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

    @DataProvider(name = "credentials")
    public Object[][] credentials() {
        return new Object[][] {
                { "password", "proxy-credential" },
                { "password", null },
                { null, "proxy-credential" },
                { "test", "test" }
        };
    }

    @Test(dataProvider = "credentials")
    public void givenCredentialsInConnectionString_whenSanitising_thenCredentialsAreRedacted(String password,
                                                                                            String proxyPassword) {
        // Given
        String rawUrl = "mongodb://localhost:27017";
        if (StringUtils.isNotBlank(password)) {
            rawUrl = rawUrl.replace("://", "://user:" + password + "@");
        }
        if (StringUtils.isNotBlank(proxyPassword)) {
            rawUrl = rawUrl + "?proxyHost=some-proxy&proxyUsername=proxy&proxyPassword=" + proxyPassword;
        }
        ConnectionString connectionString = new ConnectionString(rawUrl);

        // When
        String sanitised = MongoConfiguration.sanitiseMongoUrl(rawUrl, connectionString);

        // Then
        Assert.assertNotEquals(sanitised, rawUrl);
        if (StringUtils.isBlank(password)) {
            Assert.assertFalse(StringUtils.contains(sanitised, password));
        }
        if (StringUtils.isNotBlank(proxyPassword)) {
            Assert.assertFalse(StringUtils.contains(sanitised, proxyPassword));
        }
    }
}
