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

import io.telicent.smart.cache.distribution.lifecycle.ApplicationState;
import io.telicent.smart.cache.distribution.lifecycle.DistributionLifecycleState;
import io.telicent.smart.cache.distribution.lifecycle.events.LifecycleAcknowledgement;
import io.telicent.smart.cache.distribution.lifecycle.events.LifecycleAction;
import io.telicent.smart.cache.distribution.lifecycle.store.DistributionLifecycleStateStore;
import io.telicent.smart.cache.distribution.lifecycle.store.hibernate.model.AppStateId;
import io.telicent.smart.cache.distribution.lifecycle.store.hibernate.model.StoredApplicationState;
import io.telicent.smart.cache.distribution.lifecycle.store.hibernate.model.StoredDistributionState;
import io.telicent.smart.cache.distribution.lifecycle.store.hibernate.model.StoredLifecycleAction;
import io.telicent.smart.cache.storage.hibernate.AbstractHibernateStorage;
import io.telicent.smart.cache.storage.hibernate.TransactionContext;
import io.telicent.smart.cache.storage.hibernate.configuration.HibernateConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.JpaConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.hibernate.KeyType;

import java.util.*;
import java.util.stream.Collectors;

import static io.telicent.smart.cache.distribution.lifecycle.store.AbstractDistributionLifecycleStore.getTargetState;

public class HibernateDistributionLifecycleStateStore extends AbstractHibernateStorage
        implements DistributionLifecycleStateStore {

    public static final String PERSISTENCE_UNIT = "hibernate-distribution-lifecycle-store";

    /**
     * Creates a new hibernate backed distribution lifecycle state store
     *
     * @param dbProperties Database Connection properties, this should contain at least a
     *                     {@value JpaConfiguration#JAKARTA_PERSISTENCE_JDBC_URL} property to provide a JDBC connection
     *                     to the database as well as any other relevant properties e.g.
     *                     {@value JpaConfiguration#JAKARTA_PERSISTENCE_JDBC_USER},
     *                     {@value HibernateConfiguration#HIBERNATE_DIALECT} etc.
     */
    public HibernateDistributionLifecycleStateStore(Properties dbProperties) {
        super(dbProperties, PERSISTENCE_UNIT);
    }

    @Override
    protected Flyway configureFlyway(Properties dbProperties) {
        return Flyway.configure()
                     .dataSource(dbProperties.getProperty(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_URL),
                                 dbProperties.getProperty(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_USER),
                                 dbProperties.getProperty(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_PASSWORD))
                     .baselineVersion("0")
                     .baselineOnMigrate(true)
                     .load();
    }

    @Override
    public void add(LifecycleAction action) {
        Objects.requireNonNull(action, "Action cannot be null");

        try (TransactionContext context = this.begin()) {
            // Ensure the action is created in the store if not yet present
            StoredLifecycleAction stored =
                    getOrCreateByNaturalId(context, action.getEventId(), StoredLifecycleAction.class, () -> {
                        StoredLifecycleAction newAction = new StoredLifecycleAction();
                        newAction.setEventId(action.getEventId());
                        newAction.setAction(action);
                        return newAction;
                    });
            if (!Objects.equals(action, stored.getAction())) {
                throw new IllegalStateException(
                        "A Lifecycle Action Event " + action.getEventId() + " with differing content is already known to this state store");
            }

            // Ensure the distribution state is recorded in the store if not yet present
            StoredDistributionState distributionState =
                    this.getOrCreateByNaturalId(context, action.getDistributionId(), StoredDistributionState.class,
                                                () -> {
                                                    // All distributions start as Unregistered
                                                    StoredDistributionState newState = new StoredDistributionState();
                                                    newState.setDistributionId(action.getDistributionId());
                                                    newState.setState(DistributionLifecycleState.Unregistered);
                                                    return newState;
                                                });

            // Check whether a transition into the target lifecycle state is possible, if so update the lifecycle state
            // and commit our changes
            DistributionLifecycleState current = distributionState.getState();
            DistributionLifecycleState target = action.getState().getTo();
            if (!current.canTransition(target)) {
                throw new IllegalStateException(
                        "Distribution Lifecycle state transition from " + current + " to " + target + " is not permitted");
            }
            if (target != current) {
                distributionState.setState(target);
                context.getEntityManager().merge(distributionState);
            }
            context.commit();
        }
    }

    @Override
    public void add(String application, LifecycleAcknowledgement ack) {
        Objects.requireNonNull(ack, "Acknowledgement cannot be null");
        if (StringUtils.isBlank(application)) {
            throw new IllegalArgumentException("Application ID cannot be null/blank");
        }

        try (TransactionContext context = this.begin()) {
            StoredLifecycleAction action =
                    context.getSession().find(StoredLifecycleAction.class, ack.getEventId(), KeyType.NATURAL);
            // Don't permit acknowledgements for events we aren't aware of
            if (action == null) {
                throw new IllegalStateException(
                        "Lifecycle Action Event " + ack.getEventId() + " is not known to this state store so cannot track application state against this event");
            }

            StoredApplicationState stored = getOrCreateById(context, new AppStateId(ack.getEventId(), application),
                                                            StoredApplicationState.class, () -> {
                        StoredApplicationState newState = new StoredApplicationState();
                        newState.setId(new AppStateId(ack.getEventId(), application));
                        return newState;
                    });
            ApplicationState target = getTargetState(ack, stored.getState());

            if (stored.getState() != target) {
                stored.setState(target);
                context.getEntityManager().merge(stored);
            }
            context.commit();
        }
    }

    @Override
    public List<LifecycleAction> activeEvents() {
        try (TransactionContext context = this.begin()) {
            return this.loadByNamedQuery(context, StoredLifecycleAction.class, "activeEvents", Collections.emptyMap(),
                                         l -> {
                                             List<LifecycleAction> active = new ArrayList<>();
                                             for (StoredLifecycleAction action : l) {
                                                 active.add(action.getAction());
                                             }
                                             return active;
                                         });
        }
    }

    @Override
    public Map<String, DistributionLifecycleState> getLifecycleStates() {
        try (TransactionContext context = this.begin()) {
            return this.loadAll(context, StoredDistributionState.class)
                       .stream()
                       .collect(Collectors.toMap(StoredDistributionState::getDistributionId,
                                                 StoredDistributionState::getState));
        }
    }

    @Override
    public DistributionLifecycleState getLifecycleState(String distributionId) {
        if (StringUtils.isBlank(distributionId)) {
            throw new IllegalArgumentException("Distribution ID cannot be null/blank");
        }

        try (TransactionContext context = this.begin()) {
            StoredDistributionState stored =
                    context.getSession().find(StoredDistributionState.class, distributionId, KeyType.NATURAL);
            return stored != null ? stored.getState() : DistributionLifecycleState.Unregistered;
        }
    }

    @Override
    public Map<String, ApplicationState> getApplicationStates(UUID eventId) {
        if (eventId == null) {
            throw new IllegalArgumentException("Event ID cannot be null");
        }

        try (TransactionContext context = this.begin()) {
            return this.loadByNamedQuery(context, StoredApplicationState.class, "findForEvent",
                                         Map.of("eventId", eventId))
                       .stream()
                       .collect(Collectors.toMap(s -> s.getId().getApplication(), StoredApplicationState::getState));
        }
    }

    @Override
    public ApplicationState getApplicationState(UUID eventId, String application) {
        if (eventId == null) {
            throw new IllegalArgumentException("Event ID cannot be null");
        } else if (StringUtils.isBlank(application)) {
            throw new IllegalArgumentException("Application ID cannot be null/blank");
        }

        try (TransactionContext context = this.begin()) {
            StoredApplicationState stored =
                    context.getSession().find(StoredApplicationState.class, new AppStateId(eventId, application));
            if (stored == null) {
                return null;
            } else {
                return stored.getState();
            }
        }
    }

    @Override
    public void flush() {
        // Flush is a no-op because any changes to the state store are immediately persistent to the underlying database
        // However we do need to honour API contract of not permitting any operations after a close()
        ensureNotClosed();
    }
}
