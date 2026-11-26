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

import io.telicent.smart.cache.storage.AbstractStorage;
import io.telicent.smart.cache.storage.hibernate.configuration.HibernateConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.JpaConfiguration;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.flywaydb.core.Flyway;
import org.hibernate.KeyType;
import org.hibernate.query.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An abstract storage layer using Hibernate plus JPA to provide persistent {@link jakarta.persistence.Entity} centric
 * storage.
 * <p>
 * Derived storage classes will typically extend this class and implement an appropriate storage interface for the
 * service they are implementing storage for.  Derived classes have two main ways to interact with the underlying
 * storage:
 * </p>
 * <ol>
 *     <li>The {@link #begin()} method to start a new transaction.</li>
 *     <li>
 *     The various helper methods that take the {@link TransactionContext} returned by {@link #begin()} and use it
 *     to carry out common operations e.g. {@link #getOrCreateByNaturalId(TransactionContext, Object, Class, Supplier)}
 *     </li>
 * </ol>
 * <p>
 * Please see the Javadoc on individual methods for more details of each methods functionality.
 * </p>
 */
@ToString
@SuppressWarnings({ "unused", "UnusedReturnValue", "java:S119" })
public abstract class AbstractHibernateStorage extends AbstractStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHibernateStorage.class);

    @ToString.Exclude
    private final EntityManagerFactory entityManagerFactory;

    /**
     * The JDBC URL of the underlying data source
     */
    protected final String jdbcUrl;

    /**
     * Creates a new hibernate backed store
     *
     * @param dbProperties    Database Connection properties, this should contain at least a
     *                        {@value JpaConfiguration#JAKARTA_PERSISTENCE_JDBC_URL} property to provide a JDBC
     *                        connection to the database as well as any other relevant properties e.g.
     *                        {@value JpaConfiguration#JAKARTA_PERSISTENCE_JDBC_USER},
     *                        {@value HibernateConfiguration#HIBERNATE_DIALECT} etc.
     * @param persistenceUnit The name of the persistence unit to use, this should generally contain only the basic
     *                        configuration e.g. Entity Classes, JPA Provider, generic JPA configuration.  The actual
     *                        database connection properties <strong>MUST</strong> be supplied via the
     *                        {@code dbProperties} parameter instead.
     */
    protected AbstractHibernateStorage(Properties dbProperties, String persistenceUnit) {
        Flyway flyway = configureFlyway(dbProperties);
        if (flyway != null) {
            LOGGER.info("Beginning Flyway schema migration...");
            flyway.migrate();
            LOGGER.info("Flyway schema migration completed!");
        } else {
            LOGGER.info(
                    "Optional Flyway schema migration support not in-use, if database schema may evolve over time consider adopting this feature");
        }

        this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit, dbProperties);
        this.jdbcUrl = dbProperties.getProperty(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_URL);
    }

    /**
     * Returns a configured {@link Flyway} instance to use for automated database schema management and migration, by
     * default {@code null} is returned which means that Flyway is not used
     *
     * @return Flyway configuration, or {@code null} if Flyway is not used
     */
    protected Flyway configureFlyway(Properties dbProperties) {
        return null;
    }

    /**
     * Begins a transaction internally
     * <p>
     * Callers <strong>MUST</strong> ensure they use the return value in a try-with-resources block so that transactions
     * are promptly commited/rolled back as appropriate.  Callers should either {@link TransactionContext#commit()} the
     * transaction to persist their changes, or {@link #close()} the transaction to roll back those changes.
     * </p>
     *
     * @return Transaction Context
     */
    protected final TransactionContext begin() {
        ensureNotClosed();

        // Start a new short-lived transaction
        return new ShortLivedTransactionContext(this.entityManagerFactory);
    }

    @Override
    protected void closeInternal() {
        this.entityManagerFactory.close();
    }

    /**
     * Gets an entity by its ID, creating and persisting a new instance if necessary
     * <p>
     * The {@link jakarta.persistence.Entity} annotated class <strong>MUST</strong> have one, and only one, field
     * annotated with the Hibernate {@link org.hibernate.annotations.NaturalId} annotation.
     * </p>
     * <p>
     * Callers <strong>MUST</strong> ensure they {@link TransactionContext#commit()} the provided transaction to ensure
     * any newly created entity is actually persisted.
     * </p>
     *
     * @param transaction Transaction Context
     * @param id          ID
     * @param entityClass Entity Class
     * @param creator     Supplier to create a new entity instance if the requested instance does not yet exist
     * @param <T>         Entity type
     * @return Entity instance, guaranteed to be non-null
     */
    protected <T> T getOrCreateById(TransactionContext transaction, Object id, Class<T> entityClass,
                                    Supplier<T> creator) {
        ensureNotClosed();
        T instance = transaction.getSession().find(entityClass, id);
        if (instance == null) {
            T newInstance = creator.get();
            transaction.getEntityManager().persist(newInstance);
            return newInstance;
        } else {
            return instance;
        }
    }

    /**
     * Gets an entity by its natural ID, creating and persisting a new instance if necessary
     * <p>
     * The {@link jakarta.persistence.Entity} annotated class <strong>MUST</strong> have one, and only one, field
     * annotated with the Hibernate {@link org.hibernate.annotations.NaturalId} annotation.
     * </p>
     *
     * @param transaction Transaction Context
     * @param id          Natural ID
     * @param entityClass Entity Class
     * @param creator     Supplier to create a new entity instance if the requested instance does not yet exist
     * @param <T>         Entity type
     * @return Entity instance, guaranteed to be non-null
     */
    protected <T> T getOrCreateByNaturalId(TransactionContext transaction, Object id, Class<T> entityClass,
                                           Supplier<T> creator) {
        ensureNotClosed();
        T instance = this.loadByNaturalId(transaction, id, entityClass);
        if (instance == null) {
            T newInstance = creator.get();
            transaction.getEntityManager().persist(newInstance);
            return newInstance;
        } else {
            return instance;
        }
    }

    /**
     * Gets or creates an entity based on the outcome of a named query.
     * <p>
     * If the named query returns a single result then the existing entity is returned, if it returns no results then
     * the provided supplier is used to create the new entity instance and persist it.  Therefore, callers
     * <strong>MUST</strong> ensure that they call {@link TransactionContext#commit()} on the provided transaction
     * context otherwise the newly created entity won't be guaranteed to be persistent.
     * </p>
     * <p>
     * The named query, combined with the given query parameters <strong>MUST</strong> yield either a single result, or
     * no results, if it yields multiple results then an {@link IllegalStateException} is thrown.  This generally means
     * that the named query you are referencing is not sufficiently selective to determine whether an entity already
     * exists.
     * </p>
     *
     * @param transaction     Transaction Context
     * @param entityClass     Entity Class
     * @param queryName       Query Name
     * @param queryParameters Query parameters
     * @param creator         Supplier to create a new entity instance if the named query yields no result
     * @param <T>             Entity type
     * @return Entity instance
     * @throws IllegalStateException Thrown if the named query returns more than one results
     */
    protected <T> T getOrCreateByNamedQuery(TransactionContext transaction, Class<T> entityClass, String queryName,
                                            Map<String, Object> queryParameters, Supplier<T> creator) {
        ensureNotClosed();
        TypedQuery<T> namedQuery = prepareNamedQuery(transaction, entityClass, queryName, queryParameters);
        List<T> results = namedQuery.getResultList();
        if (results.isEmpty()) {
            T instance = creator.get();
            transaction.getEntityManager().persist(instance);
            return instance;
        } else if (results.size() > 1) {
            throw new IllegalStateException("Named Query " + queryName + " has more than one result");
        } else {
            return results.getFirst();
        }
    }

    /**
     * Loads a single entity instance by its Natural ID
     * <p>
     * The {@link jakarta.persistence.Entity} annotated class <strong>MUST</strong> have one, and only one, field
     * annotated with the Hibernate {@link org.hibernate.annotations.NaturalId} annotation.
     * </p>
     *
     * @param id          Natural ID
     * @param entityClass Entity Class
     * @param <T>         Entity type
     * @return Entity instance, or {@code null} if no entity with the given Natural ID exists
     */
    protected <T> T loadByNaturalId(TransactionContext transaction, Object id, Class<T> entityClass) {
        return loadByNaturalId(transaction, id, entityClass, x -> x);
    }

    /**
     * Loads a single entity instance by its Natural ID
     * <p>
     * The {@link jakarta.persistence.Entity} annotated class <strong>MUST</strong> have one, and only one, field
     * annotated with the Hibernate {@link org.hibernate.annotations.NaturalId} annotation.
     * </p>
     * <p>
     * As opposed to the {@link #loadByNaturalId(TransactionContext, Object, Class)} overload this method allows for a
     * loader function that can mutate the loaded entity before returning it e.g. unwrapping a JPA
     * {@link jakarta.persistence.Entity} into a plain POJO, calculating computed properties, forcing lazily loaded JPA
     * properties to be fetched etc.
     * </p>
     *
     * @param transaction Transaction Context
     * @param id          Natural ID
     * @param entityClass Entity Class
     * @param loader      Loader function that transforms the stored entity type into the desired output type
     * @param <TStored>   Entity type
     * @param <T>         Output type
     * @return Entity, possibly transformed by the loader function
     */
    protected <TStored, T> T loadByNaturalId(TransactionContext transaction, Object id, Class<TStored> entityClass,
                                             Function<TStored, T> loader) {
        ensureNotClosed();
        TStored instance = transaction.getSession().find(entityClass, id, KeyType.NATURAL);
        if (instance == null) {
            return null;
        }
        return loader.apply(instance);
    }

    /**
     * Loads entities based on a JPA {@link jakarta.persistence.NamedQuery} defined on the entity class
     * <p>
     * The given entity class <strong>MUST</strong> have a JPA {@link jakarta.persistence.NamedQuery} annotation whose
     * {@link NamedQuery#name()} matches the provided query name defined.
     * </p>
     * <p>
     * This overload assumes that the named query needs no query parameters, if parameters are needed please use the
     * {@link #loadByNamedQuery(TransactionContext, Class, String, Map)} or
     * {@link #loadByNamedQuery(TransactionContext, Class, String, Map, Function)} overloads instead.
     * </p>
     *
     * @param transaction Transaction Context
     * @param entityClass Entity Class
     * @param queryName   Query Name
     * @param <T>         Entity type
     * @return List of entities matching the query
     */
    protected <T> List<T> loadByNamedQuery(TransactionContext transaction, Class<T> entityClass, String queryName) {
        return loadByNamedQuery(transaction, entityClass, queryName, Collections.emptyMap(), x -> x);
    }

    /**
     * Loads entities based on a JPA {@link jakarta.persistence.NamedQuery} defined on the entity class
     * <p>
     * The given entity class <strong>MUST</strong> have a JPA {@link jakarta.persistence.NamedQuery} annotation whose
     * {@link NamedQuery#name()} matches the provided query name defined.
     * </p>
     * <p>
     * This overload assumes that the named query needs no query parameters, if parameters are needed please use the
     * {@link #loadByNamedQuery(TransactionContext, Class, String, Map)} or
     * {@link #loadByNamedQuery(TransactionContext, Class, String, Map, Function)} overloads instead.
     * </p>
     * <p>
     * The optional {@code page} parameter controls how many results are returned, allowing a caller to page through
     * results and not return all in a single call.
     * </p>
     *
     * @param transaction Transaction Context
     * @param entityClass Entity Class
     * @param queryName   Query Name
     * @param <T>         Entity type
     * @return List of entities matching the query
     */
    protected <T> List<T> loadByNamedQuery(TransactionContext transaction, Class<T> entityClass, String queryName,
                                           Page page) {
        return loadByNamedQuery(transaction, entityClass, queryName, Collections.emptyMap(), page, x -> x);
    }

    /**
     * Loads entities based on a JPA {@link jakarta.persistence.NamedQuery} defined on the entity class
     * <p>
     * The given entity class <strong>MUST</strong> have a JPA {@link jakarta.persistence.NamedQuery} annotation whose
     * {@link NamedQuery#name()} matches the provided query name defined.
     * </p>
     * <p>
     * The provided {@code queryParameters} map allows to pass in parameters for the named query, the values
     * <strong>MUST</strong> match the expected JPA types e.g. if joining by entities then the caller
     * <strong>MUST</strong> supply an instance of that entity, likely retrieved by a previous operation e.g.
     * {@link #getOrCreateByNaturalId(TransactionContext, Object, Class, Supplier)} or
     * {@link #loadByNaturalId(TransactionContext, Object, Class, Function)}.
     * </p>
     *
     * @param transaction     Transaction Context
     * @param entityClass     Entity Class
     * @param queryName       Query Name
     * @param queryParameters Parameters for the named query
     * @param <T>             Entity type
     * @return List of entities matching the query
     */
    protected <T> List<T> loadByNamedQuery(TransactionContext transaction, Class<T> entityClass, String queryName,
                                           Map<String, Object> queryParameters) {
        return loadByNamedQuery(transaction, entityClass, queryName, queryParameters, x -> x);
    }

    /**
     * Loads entities based on a JPA {@link jakarta.persistence.NamedQuery} defined on the entity class
     * <p>
     * The given entity class <strong>MUST</strong> have a JPA {@link jakarta.persistence.NamedQuery} annotation whose
     * {@link NamedQuery#name()} matches the provided query name defined.
     * </p>
     * <p>
     * The provided {@code queryParameters} map allows to pass in parameters for the named query, the values
     * <strong>MUST</strong> match the expected JPA types e.g. if joining by entities then the caller
     * <strong>MUST</strong> supply an instance of that entity, likely retrieved by a previous operation e.g.
     * {@link #getOrCreateByNaturalId(TransactionContext, Object, Class, Supplier)} or
     * {@link #loadByNaturalId(TransactionContext, Object, Class, Function)}.
     * </p>
     * <p>
     * The optional {@code page} parameter controls how many results are returned, allowing a caller to page through
     * results and not return all in a single call.
     * </p>
     *
     * @param transaction     Transaction Context
     * @param entityClass     Entity Class
     * @param queryName       Query Name
     * @param queryParameters Parameters for the named query
     * @param page            Optional page parameter to limit the results returned
     * @param <T>             Entity type
     * @return List of entities matching the query
     */
    protected <T> List<T> loadByNamedQuery(TransactionContext transaction, Class<T> entityClass, String queryName,
                                           Map<String, Object> queryParameters, Page page) {
        return loadByNamedQuery(transaction, entityClass, queryName, queryParameters, page, x -> x);
    }

    /**
     * Loads entities based on a JPA {@link jakarta.persistence.NamedQuery} defined on the entity class
     * <p>
     * The given entity class <strong>MUST</strong> have a JPA {@link jakarta.persistence.NamedQuery} annotation whose
     * {@link NamedQuery#name()} matches the provided query name defined.
     * </p>
     * <p>
     * The provided {@code queryParameters} map allows to pass in parameters for the named query, the values
     * <strong>MUST</strong> match the expected JPA types e.g. if joining by entities then the caller
     * <strong>MUST</strong> supply an instance of that entity, likely retrieved by a previous operation e.g.
     * {@link #getOrCreateByNaturalId(TransactionContext, Object, Class, Supplier)} or
     * {@link #loadByNaturalId(TransactionContext, Object, Class, Function)}.
     * </p>
     * <p>
     * As opposed to the {@link #loadByNamedQuery(TransactionContext, Class, String)} overload this method allows for a
     * loader function that can mutate the loaded entities before returning it e.g. unwrapping a JPA
     * {@link jakarta.persistence.Entity} into a plain POJO, calculating computed properties, forcing lazily loaded JPA
     * properties to be fetched etc.
     * </p>
     *
     * @param transaction     Transaction context
     * @param entityClass     Entity Class
     * @param queryName       Query Name
     * @param queryParameters Parameters for the named query
     * @param loader          Loader function that transforms the stored entity type into the desired output type
     * @param <TStored>       Entity type
     * @param <T>             Output type
     * @return List of entities matching the query
     */
    protected <TStored, T> List<T> loadByNamedQuery(TransactionContext transaction, Class<TStored> entityClass,
                                                    String queryName, Map<String, Object> queryParameters,
                                                    Function<List<TStored>, List<T>> loader) {
        return this.loadByNamedQuery(transaction, entityClass, queryName, queryParameters, null, loader);
    }

    /**
     * Loads entities based on a JPA {@link jakarta.persistence.NamedQuery} defined on the entity class
     * <p>
     * The given entity class <strong>MUST</strong> have a JPA {@link jakarta.persistence.NamedQuery} annotation whose
     * {@link NamedQuery#name()} matches the provided query name defined.
     * </p>
     * <p>
     * The provided {@code queryParameters} map allows to pass in parameters for the named query, the values
     * <strong>MUST</strong> match the expected JPA types e.g. if joining by entities then the caller
     * <strong>MUST</strong> supply an instance of that entity, likely retrieved by a previous operation e.g.
     * {@link #getOrCreateByNaturalId(TransactionContext, Object, Class, Supplier)} or
     * {@link #loadByNaturalId(TransactionContext, Object, Class, Function)}.
     * </p>
     * <p>
     * The optional {@code page} parameter controls how many results are returned, allowing a caller to page through
     * results and not return them all in a single call.
     * </p>
     * <p>
     * As opposed to the {@link #loadByNamedQuery(TransactionContext, Class, String)} overload this method allows for a
     * loader function that can mutate the loaded entities before returning it e.g. unwrapping a JPA
     * {@link jakarta.persistence.Entity} into a plain POJO, calculating computed properties, forcing lazily loaded JPA
     * properties to be fetched etc.
     * </p>
     *
     * @param transaction     Transaction context
     * @param entityClass     Entity Class
     * @param queryName       Query Name
     * @param queryParameters Parameters for the named query
     * @param page            Optional page parameter used to limit the results returned, if {@code null} then all
     *                        results are returned
     * @param loader          Loader function that transforms the stored entity type into the desired output type
     * @param <TStored>       Entity type
     * @param <T>             Output type
     * @return List of entities matching the query
     */
    protected <TStored, T> List<T> loadByNamedQuery(TransactionContext transaction, Class<TStored> entityClass,
                                                    String queryName, Map<String, Object> queryParameters, Page page,
                                                    Function<List<TStored>, List<T>> loader) {
        ensureNotClosed();
        TypedQuery<TStored> query = prepareNamedQuery(transaction, entityClass, queryName, queryParameters);
        if (page != null) {
            query.setFirstResult(page.getNumber() * page.getSize());
            query.setMaxResults(page.getSize());
        }

        return loader.apply(query.getResultList());
    }

    /**
     * Loads all entities of a given type
     * <p>
     * The provided entity class <strong>MUST</strong> be annotated with JPA {@link jakarta.persistence.Entity}
     * </p>
     *
     * @param transaction Transaction Context
     * @param entityClass Entity Class
     * @param <T>         Entity type
     * @return All entities
     */
    protected <T> List<T> loadAll(TransactionContext transaction, Class<T> entityClass) {
        return loadAll(transaction, entityClass, x -> x);
    }

    /**
     * Loads all entities
     * <p>
     * The provided entity class <strong>MUST</strong> be annotated with JPA {@link jakarta.persistence.Entity}
     * </p>
     * <p>
     * As opposed to the {@link #loadAll(TransactionContext, Class)} overload this method allows for a loader function
     * that can mutate the loaded entities before returning it e.g. unwrapping a JPA {@link jakarta.persistence.Entity}
     * into a plain POJO, calculating computed properties, forcing lazily loaded JPA properties to be fetched etc.
     * </p>
     *
     * @param transaction Transaction Context
     * @param entityClass Entity Class
     * @param loader      Loader function that transforms the stored entity type into the desired output type
     * @param <TStored>   Entity type
     * @param <T>         Output type
     * @return All entities, potentially transformed by the loader function
     */
    protected <TStored, T> List<T> loadAll(TransactionContext transaction, Class<TStored> entityClass,
                                           Function<List<TStored>, List<T>> loader) {
        return loadAll(transaction, entityClass, null, null, loader);
    }

    /**
     * Loads all entities
     * <p>
     * The provided entity class <strong>MUST</strong> be annotated with JPA {@link jakarta.persistence.Entity}
     * </p>
     * <p>
     * The optional {@code page} parameter controls how many results are returned, allowing a caller to page through
     * results and not return them all in a single call.  Use
     * {@link #calculatePageInfo(TransactionContext, Class, Page)} to calculate how many pages are available for the
     * given entity type.
     * </p>
     * <p>
     * As opposed to the {@link #loadAll(TransactionContext, Class)} overload this method allows for a loader function
     * that can mutate the loaded entities before returning it e.g. unwrapping a JPA {@link jakarta.persistence.Entity}
     * into a plain POJO, calculating computed properties, forcing lazily loaded JPA properties to be fetched etc.
     * </p>
     * <h4>Stable Ordering Requirement</h4>
     * <p>
     * Importantly the caller <strong>MUST</strong> provide a stable ordering of the entities, ideally by a non-mutable
     * field like its primary key field.  Otherwise, if the database is updated between calls to retrieve the next page
     * there's no guarantee that it won't just return the same entities again.
     * </p>
     * <p>
     * For this method the ordering will be by the field names given, each in ascending order with nulls first.  If you
     * want more control over the ordering then use
     * {@link #loadByNamedQuery(TransactionContext, Class, String, Map, Page)} instead and specify the desired ordering
     * in the named query.
     * </p>
     *
     * @param transaction Transaction Context
     * @param entityClass Entity Class
     * @param loader      Loader function that transforms the stored entity type into the desired output type
     * @param <TStored>   Entity type
     * @param <T>         Output type
     * @return All entities, potentially transformed by the loader function
     */
    protected <TStored, T> List<T> loadAll(TransactionContext transaction, Class<TStored> entityClass, Page page,
                                           List<String> ordering, Function<List<TStored>, List<T>> loader) {
        ensureNotClosed();

        TypedQuery<TStored> allQuery = prepareSelectAllQuery(transaction, entityClass, ordering);
        if (page != null) {
            allQuery.setFirstResult(page.getNumber() * page.getSize());
            allQuery.setMaxResults(page.getSize());
        }
        List<TStored> stored = allQuery.getResultList();
        return loader.apply(stored);
    }

    /**
     * Prepares a select all query for the given entity type
     * <p>
     * Note that ordering, if any fields to order by is supplied, is always in ascending order with nulls first.  If you
     * require more complex ordering use {@link #prepareNamedQuery(TransactionContext, Class, String, Map)} instead and
     * specify your desired ordering in the named query.
     * </p>
     *
     * @param transaction Transaction
     * @param entityClass Entity class
     * @param ordering    Optional fields to order by, fields are sorted ascending with nulls first
     * @param <T>         Entity type
     * @return Select all query
     */
    protected <T> TypedQuery<T> prepareSelectAllQuery(TransactionContext transaction, Class<T> entityClass,
                                                      List<String> ordering) {
        // Build a simple Criteria Query for the entity type
        CriteriaBuilder cb = transaction.getSession().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rootEntry = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(rootEntry);

        // If ordering is applied, sort by those fields in ascending order, nulls first
        if (CollectionUtils.isNotEmpty(ordering)) {
            all = all.orderBy(ordering.stream().map(f -> cb.asc(rootEntry.get(f), Nulls.FIRST)).toList());
        }
        return transaction.getSession().createQuery(all);
    }

    /**
     * Prepares a named query
     * <p>
     * The given entity class <strong>MUST</strong> have a JPA {@link jakarta.persistence.NamedQuery} annotation whose
     * {@link NamedQuery#name()} matches the provided query name defined.
     * </p>
     * <p>
     * The provided {@code queryParameters} map allows to pass in parameters for the named query, the values
     * <strong>MUST</strong> match the expected JPA types e.g. if joining by entities then the caller
     * <strong>MUST</strong> supply an instance of that entity, likely retrieved by a previous operation e.g.
     * {@link #getOrCreateByNaturalId(TransactionContext, Object, Class, Supplier)} or
     * {@link #loadByNaturalId(TransactionContext, Object, Class, Function)}.
     * </p>
     *
     * @param transaction     Transaction context
     * @param entityClass     Entity Class
     * @param queryName       Query Name
     * @param queryParameters Parameters for the named query
     * @param <T>             Entity type
     * @return Prepared named query
     */
    protected <T> TypedQuery<T> prepareNamedQuery(TransactionContext transaction, Class<T> entityClass,
                                                  String queryName, Map<String, Object> queryParameters) {
        TypedQuery<T> query = transaction.getEntityManager().createNamedQuery(queryName, entityClass);
        if (MapUtils.isNotEmpty(queryParameters)) {
            for (Map.Entry<String, Object> entry : queryParameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query;
    }

    /**
     * Prepares a named query that returns a single count as a long
     * <p>
     * There must be a JPA {@link jakarta.persistence.NamedQuery} annotation on one of your persistence units entity
     * classes whose {@link NamedQuery#name()} matches the provided query name defined.
     * </p>
     * <p>
     * The provided {@code queryParameters} map allows to pass in parameters for the named query, the values
     * <strong>MUST</strong> match the expected JPA types e.g. if joining by entities then the caller
     * <strong>MUST</strong> supply an instance of that entity, likely retrieved by a previous operation e.g.
     * {@link #getOrCreateByNaturalId(TransactionContext, Object, Class, Supplier)} or
     * {@link #loadByNaturalId(TransactionContext, Object, Class, Function)}.
     * </p>
     *
     * @param transaction     Transaction context
     * @param queryName       Query Name
     * @param queryParameters Parameters for the named query
     * @return Prepared named query
     */
    protected TypedQuery<Long> prepareNamedCountQuery(TransactionContext transaction, String queryName,
                                                      Map<String, Object> queryParameters) {
        TypedQuery<Long> query = transaction.getEntityManager().createNamedQuery(queryName, Long.class);
        if (MapUtils.isNotEmpty(queryParameters)) {
            for (Map.Entry<String, Object> entry : queryParameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query;
    }

    /**
     * Provides an iterator that allows scrolling through all entities of a given type, at most the specified
     * {@code page} size is held in-memory at any one time.
     * <p>
     * Note that the iterator needs to hold a reference to the transaction to allow the scrolling to happen.  Therefore,
     * callers must ensure that they do not close the provided {@code transaction} prior to returning the iterator
     * outside the storage where the external caller may have no awareness of the transaction.  The
     * {@code shouldCloseTransaction} parameter controls whether the returned iterator will call
     * {@link TransactionContext#close()} on the transaction when iteration ends.  It is strongly recommended to always
     * set this to {@code true} <strong>UNLESS</strong> the caller guarantees to consume the iterator fully/close the
     * transaction itself.
     * </p>
     * <p>
     * Should the transaction be closed outside the returned iterators control then the iterator still returns any
     * elements it has previously loaded into memory before finishing.
     * </p>
     * <p>
     * The {@code page} parameter controls the starting position of the scroll, and {@link Page#next()} will be called
     * as needed to move to the next page as the iterator proceeds.  Once no further results are retrieved from the
     * query then the transaction will be closed subject to the aforementioned {@code shouldCloseTransaction} parameter
     * being {@code true}.
     * </p>
     * <h4>Stable Ordering Requirement</h4>
     * <p>
     * Importantly the caller <strong>MUST</strong> provide a stable ordering of the entities, ideally by a non-mutable
     * field like its primary key field.  Otherwise, if the caller modifies the iterated entities and updates the
     * database when the iterator moves to the next page there's no guarantee that it won't just return the same
     * entities again.
     * </p>
     * <p>
     * For this method the ordering will be by the field names given, each in ascending order with nulls first.  If you
     * want more control over the ordering then use
     * {@link #scrollByNamedQuery(TransactionContext, Class, String, Map, Page, Function, boolean)} instead and specify
     * the desired ordering in the named query.
     * </p>
     *
     * @param transaction            Transaction Context
     * @param entityClass            Entity Class
     * @param page                   Page to specify the first page of results to start iterating from
     * @param ordering               Ordering to use to ensure stable iteration, a list of field names, each sorted in
     *                               ascending order with {@code null} first.
     * @param loader                 Loader function to convert from stored to return type
     * @param shouldCloseTransaction Whether to call {@link TransactionContext#close()} when the iterator determines no
     *                               further results are available
     * @param <TStored>              Stored type
     * @param <T>                    Return type
     * @return Iterator over results holding a reference to the transaction
     */
    protected <TStored, T> Iterator<T> scrollAll(TransactionContext transaction, Class<TStored> entityClass, Page page,
                                                 List<String> ordering, Function<TStored, T> loader,
                                                 boolean shouldCloseTransaction) {
        ensureNotClosed();
        Objects.requireNonNull(page, "Must specify a page size for scrolling");
        if (CollectionUtils.isEmpty(ordering)) {
            throw new IllegalArgumentException(
                    "MUST specify an explicit stable ordering otherwise iteration does not guarantee to return all entities");
        }
        TypedQuery<TStored> allQuery = prepareSelectAllQuery(transaction, entityClass, ordering);
        return new PagedResultIterator<>(transaction, allQuery, loader, page, shouldCloseTransaction);
    }

    /**
     * Provides an iterator that allows scrolling through all entities of a given type matching the given named query,
     * at most the specified {@code page} size is held in-memory at any one time.
     * <p>
     * Note that the iterator needs to hold a reference to the transaction to allow the scrolling to happen.  Therefore,
     * callers must ensure that they do not close the provided {@code transaction} prior to returning the iterator
     * outside the storage where the external caller may have no awareness of the transaction.  The
     * {@code shouldCloseTransaction} parameter controls whether the returned iterator will call
     * {@link TransactionContext#close()} on the transaction when iteration ends.  It is strongly recommended to always
     * set this to {@code true} <strong>UNLESS</strong> the caller guarantees to consume the iterator fully/close the
     * transaction itself.
     * </p>
     * <p>
     * Should the transaction be closed outside the returned iterators control then the iterator still returns any
     * elements it has previously loaded into memory before finishing.
     * </p>
     * <p>
     * The {@code page} parameter controls the starting position of the scroll, and {@link Page#next()} will be called
     * as needed to move to the next page as the iterator proceeds.  Once no further results are retrieved from the
     * query then the transaction will be closed subject to the aforementioned {@code shouldCloseTransaction} parameter
     * being {@code true}.
     * </p>
     * <h4>Stable Ordering Requirement</h4>
     * <p>
     * Importantly the named query <strong>MUST</strong> guarantee a stable ordering of the entities, ideally by a
     * non-mutable field like its primary key field.  Otherwise, if the caller modifies the iterated entities and
     * updates the database when the iterator moves to the next page there's no guarantee that it won't just return the
     * same entities again.
     * </p>
     *
     * @param transaction            Transaction Context
     * @param entityClass            Entity Class
     * @param queryName              Named query to use
     * @param queryParameters        Named parameters to pass to the named query
     * @param page                   Page to specify the first page of results to start iterating from
     * @param loader                 Loader function to convert from stored to return type
     * @param shouldCloseTransaction Whether to call {@link TransactionContext#close()} when the iterator determines no
     *                               further results are available
     * @param <TStored>              Stored type
     * @param <T>                    Return type
     * @return Iterator over results holding a reference to the transaction
     */
    protected <TStored, T> Iterator<T> scrollByNamedQuery(TransactionContext transaction, Class<TStored> entityClass,
                                                          String queryName, Map<String, Object> queryParameters,
                                                          Page page, Function<TStored, T> loader,
                                                          boolean shouldCloseTransaction) {
        ensureNotClosed();
        TypedQuery<TStored> query = prepareNamedQuery(transaction, entityClass, queryName, queryParameters);
        return new PagedResultIterator<>(transaction, query, loader, page, shouldCloseTransaction);
    }

    /**
     * Calculates how many pages would be available if using
     * {@link #loadAll(TransactionContext, Class, Page, List, Function)} or
     * {@link #scrollAll(TransactionContext, Class, Page, List, Function, boolean)}.
     * <p>
     * This is designed to allow the caller to augment results they may be obtaining with metadata about how many pages
     * of results are currently available.
     * </p>
     *
     * @param transaction Transaction context
     * @param entityClass Entity class
     * @param page        Page
     * @param <T>         Entity type
     * @return Page information
     */
    protected <T> PageInfo calculatePageInfo(TransactionContext transaction, Class<T> entityClass, Page page) {
        CriteriaBuilder cb = transaction.getSession().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> rootEntry = cq.from(entityClass);
        CriteriaQuery<Long> count = cq.select(cb.count(rootEntry));
        long results = transaction.getEntityManager().createQuery(count).getSingleResult();
        return new PageInfo(page.getSize(), results);
    }

    /**
     * Calculates how many pages would be available if using
     * {@link #loadByNamedQuery(TransactionContext, Class, String, Map, Page, Function)} or
     * {@link #scrollByNamedQuery(TransactionContext, Class, String, Map, Page, Function, boolean)}
     * <p>
     * This is designed to allow the caller to augment results they may be obtaining with metadata about how many pages
     * of results are currently available.
     * </p>
     *
     * @param transaction     Transaction context
     * @param queryName       Query name
     * @param queryParameters Query parameters
     * @param page            Page
     * @return Page information
     */
    protected PageInfo calculatePageInfo(TransactionContext transaction, String queryName,
                                         Map<String, Object> queryParameters, Page page) {
        TypedQuery<Long> count = prepareNamedCountQuery(transaction, queryName, queryParameters);
        Long results = count.getSingleResultOrNull();
        return new PageInfo(page.getSize(), results != null ? results : 0);

    }
}

