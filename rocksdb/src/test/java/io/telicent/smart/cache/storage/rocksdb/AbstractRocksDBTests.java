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
