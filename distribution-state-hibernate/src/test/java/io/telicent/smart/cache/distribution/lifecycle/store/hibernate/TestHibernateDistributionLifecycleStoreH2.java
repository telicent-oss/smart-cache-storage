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

import io.telicent.smart.cache.distribution.lifecycle.DistributionLifecycleState;
import io.telicent.smart.cache.distribution.lifecycle.Util;
import io.telicent.smart.cache.distribution.lifecycle.events.IngestStatus;
import io.telicent.smart.cache.distribution.lifecycle.events.utils.DistributionOffsets;
import io.telicent.smart.cache.distribution.lifecycle.events.utils.PartitionOffsets;
import io.telicent.smart.cache.distribution.lifecycle.store.AbstractDistributionLifecycleStoreTests;
import io.telicent.smart.cache.distribution.lifecycle.store.DistributionLifecycleStateStore;
import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.h2.H2Configuration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Properties;
import java.util.UUID;
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

    @Override
    public boolean tracksLastAppStateUpdated() {
        return true;
    }

    @Test
    public void givenFreshStore_whenCheckingIngestStatuses_thenEmpty() {
        try (DistributionLifecycleStateStore store = newStore()) {
            Assert.assertTrue(store.getIngestStatuses("test").isEmpty());
            Assert.assertTrue(store.getAllIngestStatuses().isEmpty());
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void givenBlankApplication_whenCheckingIngestStatuses_thenIllegalArgument() {
        try (DistributionLifecycleStateStore store = newStore()) {
            store.getIngestStatuses(" ");
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void givenBlankApplication_whenCheckingIngestStatus_thenIllegalArgument() {
        try (DistributionLifecycleStateStore store = newStore()) {
            store.getIngestStatus(" ", "distro");
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void givenBlankDistribution_whenCheckingIngestStatus_thenIllegalArgument() {
        try (DistributionLifecycleStateStore store = newStore()) {
            store.getIngestStatus("test", " ");
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void givenBlankPartition_whenCheckingIngestOffset_thenIllegalArgument() {
        try (DistributionLifecycleStateStore store = newStore()) {
            store.getIngestOffset("test", "distro", " ");
        }
    }

    @Test
    public void givenEmptyIngestStatus_whenAdding_thenIgnored() {
        try (DistributionLifecycleStateStore store = newStore()) {
            store.add("test", IngestStatus.builder().offsets(new DistributionOffsets()).build());

            Assert.assertTrue(store.getIngestStatuses("test").isEmpty());
            Assert.assertTrue(store.getAllIngestStatuses().isEmpty());
        }
    }

    @Test
    public void givenInvalidIngestEntries_whenAdding_thenIgnored() {
        DistributionOffsets offsets = new DistributionOffsets();
        offsets.setOffsets(" ", new PartitionOffsets());
        offsets.setOffsets("valid-distro", null);

        try (DistributionLifecycleStateStore store = newStore()) {
            store.add("test", IngestStatus.builder().offsets(offsets).build());

            Assert.assertTrue(store.getIngestStatuses("test").isEmpty());
            Assert.assertNull(store.getIngestStatus("test", "valid-distro"));
            Assert.assertTrue(store.getAllIngestStatuses().isEmpty());
        }
    }

    @Test
    public void givenInvalidPartitionsInIngestStatus_whenAdding_thenIgnored() {
        DistributionOffsets offsets = new DistributionOffsets();
        PartitionOffsets partitions = new PartitionOffsets();
        partitions.setOffset(" ", 10L);
        partitions.setOffset("partition-0", null);
        offsets.setOffsets("distro", partitions);

        try (DistributionLifecycleStateStore store = newStore()) {
            store.add("test", IngestStatus.builder().offsets(offsets).build());

            Assert.assertNotNull(store.getIngestStatus("test", "distro"));
            Assert.assertTrue(store.getIngestStatus("test", "distro").getOffsets().isEmpty());

            store.flush();

            Assert.assertNull(store.getIngestStatus("test", "distro"));
            Assert.assertNull(store.getIngestOffset("test", "distro", "partition-0"));
        }
    }

    @Test
    public void givenStoredState_whenFlushing_thenValuesRemainQueryable() {
        UUID eventId = UUID.randomUUID();

        try (DistributionLifecycleStateStore store = newStore()) {
            store.add(Util.action(eventId, "distro", DistributionLifecycleState.Unregistered,
                                  DistributionLifecycleState.Registered));
            store.add("test", Util.ingestStatus("distro", "partition-0", 123L));

            Assert.assertEquals(store.getLifecycleState("distro"), DistributionLifecycleState.Registered);
            Assert.assertEquals(store.getIngestOffset("test", "distro", "partition-0"), Long.valueOf(123L));

            store.flush();

            Assert.assertEquals(store.getLifecycleState("distro"), DistributionLifecycleState.Registered);
            Assert.assertEquals(store.getIngestOffset("test", "distro", "partition-0"), Long.valueOf(123L));
        }
    }
}
