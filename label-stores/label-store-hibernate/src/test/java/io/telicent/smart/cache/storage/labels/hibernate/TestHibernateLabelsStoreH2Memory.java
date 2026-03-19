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
package io.telicent.smart.cache.storage.labels.hibernate;

import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.h2.H2Configuration;
import io.telicent.smart.cache.storage.labels.AbstractLabelStoreTests;
import io.telicent.smart.cache.storage.labels.LabelsStore;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class TestHibernateLabelsStoreH2Memory extends AbstractLabelStoreTests {
    private static final AtomicInteger counter = new AtomicInteger();

    @Override
    protected LabelsStore newStore() {
        String dbName = "test-" + counter.incrementAndGet();
        Properties props = H2Configuration.prepareInMemoryConnectionProperties(
                DatabaseConfiguration.builder().hostname("localhost").database(dbName).build());
        return new HibernateLabelsStore(props);
    }
}
