/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate.configuration.postgres;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.JpaConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Properties;

public class TestPostgresConfiguration {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void givenNullConfiguration_whenPreparingConnectionProperties_thenIllegalArgument() {
        // Given, When and Then
        PostgresConfiguration.prepareConnectionProperties(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void givenNoConfiguration_whenPreparingConnectionProperties_thenIllegalArgument() {
        // Given
        DatabaseConfiguration config = DatabaseConfiguration.builder().build();

        // When and Then
        PostgresConfiguration.prepareConnectionProperties(config);
    }

    @Test
    public void givenMinimalConfiguration_whenPreparingConnectionProperties_thenAsExpected() {
        // Given
        DatabaseConfiguration config = DatabaseConfiguration.builder().hostname("localhost").database("test").build();
        Assert.assertTrue(config.isValid());

        // When
        Properties props = PostgresConfiguration.prepareConnectionProperties(config);

        // Then
        Assert.assertEquals(props.get(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_URL),
                            "jdbc:postgresql://localhost:5432/test");
        Assert.assertNull(props.get(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_USER));
        Assert.assertNull(props.get(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_PASSWORD));
    }

    @Test
    public void givenFullConfiguration_whenPreparingConnectionProperties_thenAsExpected() {
        // Given
        DatabaseConfiguration config = DatabaseConfiguration.builder()
                                                            .hostname("localhost")
                                                            .port(1234)
                                                            .database("test")
                                                            .username("example")
                                                            .password("password")
                                                            .build();
        Assert.assertTrue(config.isValid());

        // When
        Properties props = PostgresConfiguration.prepareConnectionProperties(config);

        // Then
        Assert.assertEquals(props.get(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_URL),
                            "jdbc:postgresql://localhost:1234/test");
        Assert.assertEquals(props.get(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_USER), "example");
        Assert.assertEquals(props.get(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_PASSWORD), "password");
    }

    @Test
    public void givenJdbcConfiguration_whenPreparingConnectionProperties_thenAsExpected() {
        // Given
        DatabaseConfiguration config = DatabaseConfiguration.builder()
                                                            .jdbcUrl("jdbc:postgresql://localhost:1234/test")
                                                            .username("example")
                                                            .password("password")
                                                            .build();
        Assert.assertTrue(config.isValid());

        // When
        Properties props = PostgresConfiguration.prepareConnectionProperties(config);

        // Then
        Assert.assertEquals(props.get(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_URL),
                            "jdbc:postgresql://localhost:1234/test");
        Assert.assertEquals(props.get(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_USER), "example");
        Assert.assertEquals(props.get(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_PASSWORD), "password");
    }
}
