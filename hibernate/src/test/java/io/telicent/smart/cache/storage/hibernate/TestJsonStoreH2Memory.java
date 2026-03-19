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
package io.telicent.smart.cache.storage.hibernate;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.h2.H2Configuration;
import io.telicent.smart.cache.storage.hibernate.model.JsonStore;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class TestJsonStoreH2Memory extends AbstractJsonStorageTests {
    private static final AtomicInteger counter = new AtomicInteger();

    @Override
    protected JsonStore createJsonStore() {
        String dbName = "test-json-" + counter.incrementAndGet();
        Properties props = H2Configuration.prepareInMemoryConnectionProperties(
                DatabaseConfiguration.builder().hostname("localhost").database(dbName).build());
        return new JsonStore(props);
    }

}
