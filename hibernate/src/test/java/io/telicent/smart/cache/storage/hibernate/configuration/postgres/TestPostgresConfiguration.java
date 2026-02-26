/**
 * Copyright (C) Telicent Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.telicent.smart.cache.storage.hibernate.configuration.postgres;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.JpaConfiguration;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
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
                            "jdbc:postgresql://localhost:5432/test?tcpKeepAlive=true");
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
                            "jdbc:postgresql://localhost:1234/test?tcpKeepAlive=true");
        Assert.assertEquals(props.get(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_USER), "example");
        Assert.assertEquals(props.get(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_PASSWORD), "password");
    }

    @DataProvider(name = "jdbcUrls")
    private Object[][] exampleJdbcUrls() {
        return new Object[][] {
                { "jdbc:postgresql://localhost:1234/test" },
                { "jdbc:postgresql://localhost:1234/test?tcpKeepAlive=true" }
        };
    }

    @Test(dataProvider = "jdbcUrls")
    public void givenJdbcConfiguration_whenPreparingConnectionProperties_thenAsExpected(String jdbcUrl) {
        // Given
        DatabaseConfiguration config =
                DatabaseConfiguration.builder().jdbcUrl(jdbcUrl).username("example").password("password").build();
        Assert.assertTrue(config.isValid());

        // When
        Properties props = PostgresConfiguration.prepareConnectionProperties(config);

        // Then
        Assert.assertEquals(props.get(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_URL), jdbcUrl);
        Assert.assertEquals(props.get(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_USER), "example");
        Assert.assertEquals(props.get(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_PASSWORD), "password");
    }
}
