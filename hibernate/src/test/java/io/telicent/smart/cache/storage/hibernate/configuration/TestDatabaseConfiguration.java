/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate.configuration;

import io.telicent.smart.cache.configuration.Configurator;
import io.telicent.smart.cache.configuration.sources.NullSource;
import io.telicent.smart.cache.configuration.sources.PropertiesSource;
import io.telicent.smart.cache.storage.hibernate.configuration.postgres.PostgresConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Properties;

public class TestDatabaseConfiguration {

    public static final String FAKE_JDBC_URL = "jdbc:postgres://hostname:1234/db";

    @AfterMethod
    public void cleanup() {
        Configurator.reset();
    }

    @Test
    public void givenNoConfiguration_whenConfiguringDatabase_thenInvalid() {
        // Given
        Configurator.setSingleSource(NullSource.INSTANCE);

        // When
        DatabaseConfiguration config = DatabaseConfiguration.fromConfigurator();

        // Then
        Assert.assertFalse(config.isValid());
    }

    @DataProvider(name = "incompleteConfigs")
    private Object[][] incompleteConfigurations() {
        return new Object[][] {
                { Map.of(DatabaseConfiguration.HOSTNAME, "localhost") },
                { Map.of(DatabaseConfiguration.DB_NAME, "test") },
                {
                        Map.of(DatabaseConfiguration.HOSTNAME, "localhost", DatabaseConfiguration.PORT,
                               PostgresConfiguration.DEFAULT_PORT)
                },
                { Map.of(DatabaseConfiguration.USER, "sa", DatabaseConfiguration.PASSWORD, "test") }
        };
    }

    @Test(dataProvider = "incompleteConfigs")
    public void givenIncompleteConfiguration_whenConfiguringDatabase_thenInvalid(Map<String, Object> map) {
        // Given
        Properties properties = new Properties();
        properties.putAll(map);
        Configurator.setSingleSource(new PropertiesSource(properties));

        // When
        DatabaseConfiguration config = DatabaseConfiguration.fromConfigurator();

        // Then
        Assert.assertFalse(config.isValid());
    }

    @Test
    public void givenMinimalConfig_whenConfiguringDatabase_thenSuccess() {
        // Given
        Properties properties = new Properties();
        properties.put(DatabaseConfiguration.HOSTNAME, "localhost");
        properties.put(DatabaseConfiguration.DB_NAME, "test");
        Configurator.setSingleSource(new PropertiesSource(properties));

        // When
        DatabaseConfiguration config = DatabaseConfiguration.fromConfigurator();

        // Then
        Assert.assertTrue(config.isValid());
        Assert.assertNull(config.getJdbcUrl());
        Assert.assertEquals(config.getHostname(), "localhost");
        Assert.assertNull(config.getPort());
        Assert.assertEquals(config.getDatabase(), "test");
        Assert.assertNull(config.getUsername());
        Assert.assertNull(config.getPassword());
    }

    @Test
    public void givenMinimalAlternativeConfig_whenConfiguringDatabase_thenSuccess() {
        // Given
        Properties properties = new Properties();
        properties.put(DatabaseConfiguration.JDBC_URL, FAKE_JDBC_URL);
        Configurator.setSingleSource(new PropertiesSource(properties));

        // When
        DatabaseConfiguration config = DatabaseConfiguration.fromConfigurator();

        // Then
        Assert.assertTrue(config.isValid());
        Assert.assertEquals(config.getJdbcUrl(), FAKE_JDBC_URL);
        Assert.assertNull(config.getHostname());
        Assert.assertNull(config.getPort());
        Assert.assertNull(config.getDatabase());
        Assert.assertNull(config.getUsername());
        Assert.assertNull(config.getPassword());
    }

    @Test
    public void givenInvalidPort_whenConfiguringDatabase_thenNoPortIsSet() {
        // Given
        Properties properties = new Properties();
        properties.put(DatabaseConfiguration.HOSTNAME, "localhost");
        properties.put(DatabaseConfiguration.PORT, "ab123");
        properties.put(DatabaseConfiguration.DB_NAME, "test");
        Configurator.setSingleSource(new PropertiesSource(properties));

        // When
        DatabaseConfiguration config = DatabaseConfiguration.fromConfigurator();

        // Then
        Assert.assertTrue(config.isValid());
        Assert.assertEquals(config.getHostname(), "localhost");
        Assert.assertNull(config.getPort());
        Assert.assertEquals(config.getDatabase(), "test");
    }

    @Test
    public void givenValidPort_whenConfiguringDatabase_thenPortIsSet() {
        // Given
        Properties properties = new Properties();
        properties.put(DatabaseConfiguration.HOSTNAME, "localhost");
        properties.put(DatabaseConfiguration.PORT, PostgresConfiguration.DEFAULT_PORT);
        properties.put(DatabaseConfiguration.DB_NAME, "test");
        Configurator.setSingleSource(new PropertiesSource(properties));

        // When
        DatabaseConfiguration config = DatabaseConfiguration.fromConfigurator();

        // Then
        Assert.assertTrue(config.isValid());
        Assert.assertEquals(config.getHostname(), "localhost");
        Assert.assertEquals(config.getPort(), PostgresConfiguration.DEFAULT_PORT);
        Assert.assertEquals(config.getDatabase(), "test");
    }

    @Test
    public void givenNoConfigurationAndDefault_whenConfiguringDatabase_thenDefaultsUsed() {
        // Given
        Configurator.setSingleSource(NullSource.INSTANCE);

        // When
        DatabaseConfiguration config =
                DatabaseConfiguration.fromConfigurator("localhost", PostgresConfiguration.DEFAULT_PORT, "some-default",
                                                       null, null);

        // Then
        Assert.assertTrue(config.isValid());
        Assert.assertEquals(config.getHostname(), "localhost");
        Assert.assertEquals(config.getPort(), PostgresConfiguration.DEFAULT_PORT);
        Assert.assertEquals(config.getDatabase(), "some-default");
    }
}
