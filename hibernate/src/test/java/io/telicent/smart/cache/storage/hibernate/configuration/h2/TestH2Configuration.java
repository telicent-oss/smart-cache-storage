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
package io.telicent.smart.cache.storage.hibernate.configuration.h2;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static io.telicent.smart.cache.storage.hibernate.configuration.TestDatabaseConfiguration.FAKE_JDBC_URL;

public class TestH2Configuration {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void givenNullConfiguration_whenPreparing_thenIllegalArgument() {
        // Given, When and Then
        H2Configuration.prepareInMemoryConnectionProperties(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Explicit JDBC URL.*")
    public void givenJdbcUrl_whenPreparingInMemoryConnectionProperties_thenIllegalArgument() {
        // Given
        DatabaseConfiguration configuration = DatabaseConfiguration.builder().jdbcUrl(FAKE_JDBC_URL).build();

        // When and Then
        H2Configuration.prepareInMemoryConnectionProperties(configuration);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Explicit JDBC URL.*")
    public void givenJdbcUrl_whenPreparingFileConnectionProperties_thenIllegalArgument() {
        // Given
        DatabaseConfiguration configuration = DatabaseConfiguration.builder().jdbcUrl(FAKE_JDBC_URL).build();

        // When and Then
        H2Configuration.prepareFileConnectionProperties(configuration, new File("."));
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Must supply a database name.*")
    public void givenNoDatabaseName_whenPreparingConnectionProperties_thenIllegalArgument() {
        // Given
        DatabaseConfiguration configuration = DatabaseConfiguration.builder().build();

        // When and Then
        H2Configuration.prepareInMemoryConnectionProperties(configuration);
    }

    @Test
    public void givenNoBaseDir_whenResolvingDbDir_thenUsedAsIs() {
        // Given
        File baseDir = null;
        DatabaseConfiguration config = DatabaseConfiguration.builder().hostname("localhost").database("mydb").build();

        // When
        File dbDir = H2Configuration.resolveDatabaseDirectory(config, baseDir);

        // Then
        File expectedDbDir = new File("mydb").getAbsoluteFile();
        Assert.assertEquals(dbDir.getAbsolutePath(), expectedDbDir.getAbsolutePath());
    }

    @Test
    public void givenBaseDir_whenResolvingDbDir_thenRelativeToBaseDir() throws IOException {
        // Given
        File baseDir = Files.createTempDirectory("dbs").toFile();
        DatabaseConfiguration config = DatabaseConfiguration.builder().hostname("localhost").database("mydb").build();

        // When
        File dbDir = H2Configuration.resolveDatabaseDirectory(config, baseDir);

        // Then
        File expectedDbDir = new File(baseDir,"mydb").getAbsoluteFile();
        Assert.assertEquals(dbDir.getAbsolutePath(), expectedDbDir.getAbsolutePath());
    }
}
