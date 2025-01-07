/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate.configuration.h2;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestH2Configuration {

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
