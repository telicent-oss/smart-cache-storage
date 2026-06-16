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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
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
import jakarta.persistence.RollbackException;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.hibernate.KeyType;
import org.hibernate.annotations.NaturalId;
import org.hibernate.exception.ConstraintViolationException;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.telicent.smart.cache.distribution.lifecycle.store.AbstractDistributionLifecycleStore.getTargetState;

public class HibernateDistributionLifecycleStateStore extends AbstractHibernateStorage
        implements DistributionLifecycleStateStore {

    public static final String PERSISTENCE_UNIT = "hibernate-distribution-lifecycle-store";
    public static final int RECENT_CACHE_SIZE = 1_000;
    public static final Duration RECENT_CACHE_DURATION = Duration.ofMinutes(1);

    /**
     * Keep a cache of recently received actions, this is used for duplicate checking and to avoid unnecessary database
     * lookups
     */
    private final Cache<UUID, LifecycleAction> recentActions;
    private final Cache<String, DistributionLifecycleState> recentLifecycleStates;
    private final Cache<AppStateId, ApplicationState> recentAppStates;

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

        // Create the caches that are used to avoid unnecessary database operations
        this.recentActions = Caffeine.newBuilder()
                                     .maximumSize(RECENT_CACHE_SIZE)
                                     .initialCapacity(RECENT_CACHE_SIZE)
                                     .expireAfterWrite(RECENT_CACHE_DURATION)
                                     .build();
        this.recentLifecycleStates = Caffeine.newBuilder()
                                             .maximumSize(RECENT_CACHE_SIZE)
                                             .initialCapacity(RECENT_CACHE_SIZE)
                                             .expireAfterWrite(RECENT_CACHE_DURATION)
                                             .build();
        this.recentAppStates = Caffeine.newBuilder()
                                       .maximumSize(RECENT_CACHE_SIZE)
                                       .initialCapacity(RECENT_CACHE_SIZE)
                                       .expireAfterWrite(RECENT_CACHE_DURATION)
                                       .build();
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

    private final Map<Class<?>, Class<?>> naturalIdTypes = new HashMap<>();

    /**
     * Checks whether a given ID value is the natural ID type, used by {@link #fromCacheOrDb(TransactionContext, UUID)}
     * and {@link #fromCacheOrDbWithUpdate(TransactionContext, Object, Cache, Class, Function)} to determine how to
     * query the database when no cache hit.
     * <p>
     * This method determines this by inspecting the annotations on the entity class to find the {@link NaturalId}
     * annotated field (if any) and caches that information.  It compares this type information with the actual type of
     * the ID value given to make a determination.
     * </p>
     *
     * @param id          ID value
     * @param entityClass Entity class
     * @param <TId>       ID type
     * @param <TEntity>   Entity type
     * @return True if the ID value is of the Natural ID type of the entity class, false otherwise
     */
    private <TId, TEntity> boolean isNaturalId(TId id, Class<TEntity> entityClass) {
        Class<?> naturalIdType = naturalIdTypes.computeIfAbsent(entityClass, e -> {
            for (Field field : e.getDeclaredFields()) {
                if (field.isAnnotationPresent(NaturalId.class)) {
                    return field.getType();
                }
            }
            return Void.class;
        });
        return Objects.equals(id.getClass(), naturalIdType);
    }

    /**
     * Gets the lifecycle action with the given Event ID from the cache or database
     *
     * @param context Transaction Context
     * @param eventId Event ID
     * @return Lifecycle action, or {@code null} if unknown event
     */
    private LifecycleAction fromCacheOrDb(TransactionContext context, UUID eventId) {
        return fromCacheOrDb(context, eventId, this.recentActions, StoredLifecycleAction.class,
                             StoredLifecycleAction::getAction);
    }

    /**
     * Gets a value from the cache or the database
     *
     * @param context     Transaction Context
     * @param id          ID
     * @param cache       Cache
     * @param entityClass Entity Class
     * @param loader      Function that converts from stored database entities to desired value type
     * @param <TId>       ID type
     * @param <TEntity>   Stored entity type
     * @param <T>         Value type
     * @return Value, or {@code null} if no value associated with the ID in the database
     */
    private <TId, TEntity, T> T fromCacheOrDb(TransactionContext context, TId id, Cache<TId, T> cache,
                                              Class<TEntity> entityClass, Function<TEntity, T> loader) {
        T existing = cache.getIfPresent(id);
        if (existing == null) {
            if (isNaturalId(id, entityClass)) {
                existing = loadByNaturalId(context, id, entityClass, loader);
            } else {
                TEntity entity = context.getSession().find(entityClass, id);
                existing = entity != null ? loader.apply(entity) : null;
            }
        }
        return existing;
    }

    /**
     * Gets a value from the cache or database updating the cache if appropriate
     * <p>
     * Differs from {@link #fromCacheOrDb(TransactionContext, Object, Cache, Class, Function)} in that this method
     * actively updates the cache with the database value (if any), whereas the other method only queries the cache and
     * database leaving the cache unmodified if there's no match in the database.
     * </p>
     *
     * @param context     Transaction Context
     * @param id          ID
     * @param cache       Cache
     * @param entityClass Entity Class
     * @param loader      Function that converts from stored database entities to desired value type
     * @param <TId>       ID Type
     * @param <TEntity>   Entity type
     * @param <T>         Value type
     * @return Value, or {@code null} if nothing associated with the ID in the cache or database
     */
    private <TId, TEntity, T> T fromCacheOrDbWithUpdate(TransactionContext context, TId id, Cache<TId, T> cache,
                                                        Class<TEntity> entityClass, Function<TEntity, T> loader) {
        if (isNaturalId(id, entityClass)) {
            return cache.get(id, k -> loadByNaturalId(context, k, entityClass, loader));
        } else {
            return cache.get(id, k -> {
                TEntity entity = context.getSession().find(entityClass, k);
                return entity != null ? loader.apply(entity) : null;
            });
        }
    }


    @Override
    public void add(LifecycleAction action) {
        Objects.requireNonNull(action, "Action cannot be null");

        try (TransactionContext context = this.begin()) {
            // Ensure this action is not already present
            LifecycleAction existing = fromCacheOrDb(context, action.getEventId());
            if (existing != null) {
                if (!Objects.equals(action, existing)) {
                    throw new IllegalStateException(
                            "A Lifecycle Action Event " + action.getEventId() + " with differing content is already known to this state store");
                } else {
                    // Duplicate event has already been received and processed, do not re-apply as that could revert the
                    // distribution into an unexpected state

                    // NB - We intentionally invalidate the cache here because if this store is being shared between
                    //      multiple applications this means another application already received and processed this
                    //      action.  Thus,  any cache of the lifecycle state for the distribution maynow be outdated and
                    //      should be refreshed from the underlying database when next needed
                    this.recentLifecycleStates.invalidate(action.getDistributionId());
                    return;
                }
            }

            // Ensure the action is created in the store if not yet present
            getOrCreateByNaturalId(context, action.getEventId(), StoredLifecycleAction.class, () -> {
                StoredLifecycleAction newAction = new StoredLifecycleAction();
                newAction.setEventId(action.getEventId());
                newAction.setAction(action);
                return newAction;
            });

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

            // Update the caches only if we've successfully committed the action to the store
            this.recentActions.put(action.getEventId(), action);
            this.recentLifecycleStates.put(action.getDistributionId(), distributionState.getState());
        } catch (RollbackException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                // This can happen if the store is shared across multiple applications and several attempt to process
                // the action at the same time.  In this case another application has already committed the action and
                // lifecycle state updates so retrying should detect that and be able to proceed
                this.add(action);
            } else {
                throw new IllegalStateException("Failed to apply lifecycle action", e);
            }
        }
    }

    @Override
    public void add(String application, LifecycleAcknowledgement ack) {
        Objects.requireNonNull(ack, "Acknowledgement cannot be null");
        if (StringUtils.isBlank(application)) {
            throw new IllegalArgumentException("Application ID cannot be null/blank");
        }

        try (TransactionContext context = this.begin()) {
            LifecycleAction existingAction = fromCacheOrDb(context, ack.getEventId());
            // Don't permit acknowledgements for events we aren't aware of
            if (existingAction == null) {
                throw new IllegalStateException(
                        "Lifecycle Action Event " + ack.getEventId() + " is not known to this state store so cannot track application state against this event");
            }

            AppStateId id = new AppStateId(ack.getEventId(), application);
            StoredApplicationState stored = getOrCreateById(context, id, StoredApplicationState.class, () -> {
                StoredApplicationState newState = new StoredApplicationState();
                newState.setId(id);
                return newState;
            });
            ApplicationState target = getTargetState(ack, stored.getState());

            if (stored.getState() != target) {
                stored.setState(target);
                context.getEntityManager().merge(stored);
            }
            context.commit();

            // Update the caches
            this.recentAppStates.put(id, stored.getState());
        } catch (RollbackException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                // This can happen if the store is shared across multiple applications and several attempt to process
                // the acknowledgement at the same time.  In this case another application has already committed an
                // application state so retrying should allow this to complete
                add(application, ack);
            } else {
                throw new IllegalStateException("Failed to apply lifecycle acknowledgement", e);
            }
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
            Map<String, DistributionLifecycleState> states = this.loadAll(context, StoredDistributionState.class)
                                                                 .stream()
                                                                 .collect(Collectors.toMap(
                                                                         StoredDistributionState::getDistributionId,
                                                                         StoredDistributionState::getState));
            // Update the cache as appropriate
            if (states.isEmpty()) {
                this.recentLifecycleStates.invalidateAll();
            } else {
                this.recentLifecycleStates.putAll(states);
            }
            return states;
        }
    }

    @Override
    public DistributionLifecycleState getLifecycleState(String distributionId) {
        if (StringUtils.isBlank(distributionId)) {
            throw new IllegalArgumentException("Distribution ID cannot be null/blank");
        }

        try (TransactionContext context = this.begin()) {
            DistributionLifecycleState state =
                    fromCacheOrDbWithUpdate(context, distributionId, this.recentLifecycleStates,
                                            StoredDistributionState.class, StoredDistributionState::getState);
            return state != null ? state : DistributionLifecycleState.Unregistered;
        }
    }

    @Override
    public Map<String, ApplicationState> getApplicationStates(UUID eventId) {
        if (eventId == null) {
            throw new IllegalArgumentException("Event ID cannot be null");
        }

        try (TransactionContext context = this.begin()) {
            Map<String, ApplicationState> states =
                    this.loadByNamedQuery(context, StoredApplicationState.class, "findForEvent",
                                          Map.of("eventId", eventId))
                        .stream()
                        .collect(Collectors.toMap(s -> s.getId().getApplication(), StoredApplicationState::getState));

            if (!states.isEmpty()) {
                states.forEach((key, value) -> this.recentAppStates.put(new AppStateId(eventId, key), value));
            }

            return states;
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
            AppStateId id = new AppStateId(eventId, application);
            return fromCacheOrDbWithUpdate(context, id, this.recentAppStates, StoredApplicationState.class,
                                           StoredApplicationState::getState);
        }
    }

    @Override
    public void flush() {
        // Flush is mostly a no-op because any changes to the state store are immediately persistent to the underlying
        // database
        // However we do need to honour API contract of not permitting any operations after a close()
        ensureNotClosed();

        // Also since we have caches flush() is a good point to invalidate those caches since if this store is being
        // shared by multiple applications our cached state might have drifted depending on how our application has used
        // the store relative to other applications
        clearCaches();
    }

    /**
     * Clears all our caches
     */
    private void clearCaches() {
        this.recentActions.invalidateAll();
        this.recentLifecycleStates.invalidateAll();
        this.recentAppStates.invalidateAll();
    }

    @Override
    protected void closeInternal() {
        super.closeInternal();

        clearCaches();
    }
}
