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
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.helpers.NOPAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs the counter tests with DEBUG logging enabled so that the logging code paths are exercised.  The root logger's
 * appenders are swapped for a no-op appender for the duration, so the output doesn't flood the build log.
 */
public class TestRocksDBCountersWithLogging extends TestRocksDBCounter {

    private ch.qos.logback.classic.Logger root;
    private Level originalLevel;
    private final List<Appender<ILoggingEvent>> originalAppenders = new ArrayList<>();
    private NOPAppender<ILoggingEvent> nopAppender;

    @BeforeClass
    public void setupLogging() {
        root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        this.originalLevel = root.getLevel();
        root.iteratorForAppenders().forEachRemaining(this.originalAppenders::add);
        this.originalAppenders.forEach(root::detachAppender);
        this.nopAppender = new NOPAppender<>();
        this.nopAppender.setContext(root.getLoggerContext());
        this.nopAppender.start();
        root.addAppender(this.nopAppender);
        root.setLevel(Level.DEBUG);
    }

    @AfterClass
    public void teardownLogging() {
        if (root != null) {
            root.setLevel(this.originalLevel);
            root.detachAppender(this.nopAppender);
            this.nopAppender.stop();
            this.originalAppenders.forEach(root::addAppender);
        }
    }
}
