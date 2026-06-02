package io.telicent.smart.cache.distribution.lifecycle.store.hibernate;

import io.telicent.smart.cache.distribution.lifecycle.ApplicationState;
import io.telicent.smart.cache.distribution.lifecycle.DistributionLifecycleState;
import io.telicent.smart.cache.distribution.lifecycle.Util;
import io.telicent.smart.cache.distribution.lifecycle.events.LifecycleAcknowledgement;
import io.telicent.smart.cache.distribution.lifecycle.events.LifecycleAction;
import io.telicent.smart.cache.distribution.lifecycle.store.DistributionLifecycleStateStore;
import io.telicent.smart.cache.storage.hibernate.configuration.DatabaseConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.h2.H2Configuration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class TestHibernateDistributionLifecycleStoreH2 {

    private static final AtomicInteger counter = new AtomicInteger();
    public static final String DISTRIBUTION_ID = "distro";
    public static final String APP_ID = "test";

    public DistributionLifecycleStateStore newStore() {
        String dbName = "test-" + counter.incrementAndGet();
        Properties props = H2Configuration.prepareInMemoryConnectionProperties(
                DatabaseConfiguration.builder().hostname("localhost").database(dbName).build());
        return new HibernateDistributionLifecycleStateStore(props);
    }

    @Test
    public void givenFreshStore_whenInspecting_thenEmpty() {
        // Given
        try (DistributionLifecycleStateStore store = newStore()) {
            // When and Then
            Assert.assertTrue(store.activeEvents().isEmpty());
            Assert.assertTrue(store.getLifecycleStates().isEmpty());
            Assert.assertTrue(store.getApplicationStates(UUID.randomUUID()).isEmpty());
            Assert.assertNull(store.getApplicationState(UUID.randomUUID(), APP_ID));
            Assert.assertEquals(store.getLifecycleState(DISTRIBUTION_ID), DistributionLifecycleState.Unregistered);
        }
    }

    @Test
    public void givenAction_whenAddingToStore_thenUpdated() {
        // Given
        LifecycleAction action =
                Util.action(UUID.randomUUID(), DISTRIBUTION_ID, DistributionLifecycleState.Unregistered,
                            DistributionLifecycleState.Registered);
        try (DistributionLifecycleStateStore store = newStore()) {
            // When
            store.add(action);

            // Then
            Assert.assertEquals(store.getLifecycleState(DISTRIBUTION_ID), DistributionLifecycleState.Registered);
            verifyActiveEvents(store);
        }
    }

    private void acknowledge(DistributionLifecycleStateStore store, UUID eventId, String appId, String distroId,
                             ApplicationState... states) {
        Assert.assertNull(store.getApplicationState(eventId, appId));
        for (ApplicationState state : states) {
            LifecycleAcknowledgement acknowledgement = Util.ack(eventId, distroId, state);
            store.add(appId, acknowledgement);
            Assert.assertEquals(store.getApplicationState(eventId, appId), state);

            if (state != ApplicationState.Completed) {
                verifyActiveEvents(store);
            } else {
                Assert.assertTrue(store.activeEvents().isEmpty());
            }
        }
    }

    @Test
    public void givenAction_whenAcknowledging_thenAppStateUpdated() {
        // Given
        UUID eventId = UUID.randomUUID();
        LifecycleAction action = Util.action(eventId, DISTRIBUTION_ID, DistributionLifecycleState.Unregistered,
                                             DistributionLifecycleState.Registered);
        try (DistributionLifecycleStateStore store = newStore()) {
            // When and Then
            store.add(action);
            Assert.assertNull(store.getApplicationState(eventId, APP_ID));
            verifyActiveEvents(store);
            acknowledge(store, eventId, APP_ID, DISTRIBUTION_ID, ApplicationState.Requested,
                        ApplicationState.InProgress, ApplicationState.Completed);
        }
    }

    @Test
    public void givenAction_whenAcknowledgingForMultipleApps_thenAppStatesUpdatedIndependently() {
        // Given
        UUID eventId = UUID.randomUUID();
        LifecycleAction action = Util.action(eventId, DISTRIBUTION_ID, DistributionLifecycleState.Unregistered,
                                             DistributionLifecycleState.Registered);
        try (DistributionLifecycleStateStore store = newStore()) {
            // When
            store.add(action);
            verifyActiveEvents(store);
            acknowledge(store, eventId, APP_ID, DISTRIBUTION_ID, ApplicationState.Requested,
                        ApplicationState.InProgress, ApplicationState.Completed);
            acknowledge(store, eventId, "other", DISTRIBUTION_ID, ApplicationState.Requested,
                        ApplicationState.InProgress, ApplicationState.Failed);
            acknowledge(store, eventId, "another", DISTRIBUTION_ID, ApplicationState.Requested);

            // Then
            Map<String, ApplicationState> appStates = store.getApplicationStates(eventId);
            Assert.assertEquals(appStates.get(APP_ID), ApplicationState.Completed);
            Assert.assertEquals(appStates.get("other"), ApplicationState.Failed);
            Assert.assertEquals(appStates.get("another"), ApplicationState.Requested);
        }
    }

    private static void verifyActiveEvents(DistributionLifecycleStateStore store) {
        Assert.assertFalse(store.activeEvents().isEmpty());
    }
}
