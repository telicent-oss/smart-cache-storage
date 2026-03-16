/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rocksdb;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AbstractRocksDBTests {
    /**
     * The current temporary database directory
     */
    protected File dbDir;

    @BeforeMethod
    public void createDatabaseDirectory() throws IOException {
        this.dbDir = Files.createTempDirectory("rocks").toFile();
    }

    @AfterMethod
    public void deleteDatabaseDirectory() throws IOException {
        if (this.dbDir != null && this.dbDir.exists()) {
            FileUtils.deleteDirectory(this.dbDir);
        }
    }
}
