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
package io.telicent.smart.cache.storage.rocksdb.counters;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class TestRocksDBCountersWithLogging extends TestRocksDBCounter {

    private ch.qos.logback.classic.Logger root;
    private Level originalLevel;

    @BeforeClass
    public void setupLogging() {
        root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        this.originalLevel = root.getLevel();
        root.setLevel(Level.DEBUG);
    }

    @AfterClass
    public void teardownLogging() {
        if (root != null) {
            root.setLevel(this.originalLevel);
        }
    }
}
