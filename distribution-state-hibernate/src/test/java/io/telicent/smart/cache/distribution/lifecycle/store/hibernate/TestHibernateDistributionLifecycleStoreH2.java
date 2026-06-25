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
package io.telicent.smart.cache.distribution.lifecycle.store.hibernate;

import io.telicent.smart.cache.distribution.lifecycle.store.AbstractDistributionLifecycleStoreTests;
import io.telicent.smart.cache.distribution.lifecycle.store.DistributionLifecycleStateStore;
import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.h2.H2Configuration;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class TestHibernateDistributionLifecycleStoreH2 extends AbstractDistributionLifecycleStoreTests {

    private static final AtomicInteger counter = new AtomicInteger();

    @Override
    public DistributionLifecycleStateStore newStore() {
        String dbName = "test-" + counter.incrementAndGet();
        return openStore(dbName);
    }

    private static HibernateDistributionLifecycleStateStore openStore(
            String dbName) {
        Properties props = H2Configuration.prepareInMemoryConnectionProperties(
                DatabaseConfiguration.builder().hostname("localhost").database(dbName).build());
        return new HibernateDistributionLifecycleStateStore(props);
    }

    @Override
    public DistributionLifecycleStateStore reopenStore() {
        String dbName = "test-" + counter.get();
        return openStore(dbName);
    }

    @Override
    public boolean isApplicationScoped() {
        return false;
    }

    @Override
    public boolean isImmediatelyPersistent() {
        return true;
    }
}
