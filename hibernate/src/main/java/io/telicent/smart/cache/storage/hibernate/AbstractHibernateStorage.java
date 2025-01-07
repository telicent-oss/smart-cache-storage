/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate;

import io.telicent.smart.cache.storage.hibernate.configuration.HibernateConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.JpaConfiguration;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.ToString;
import org.apache.commons.collections4.MapUtils;

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
@SuppressWarnings({"unused", "UnusedReturnValue"})
public abstract class AbstractHibernateStorage implements AutoCloseable {

    @ToString.Exclude
    private final EntityManagerFactory entityManagerFactory;

    private volatile boolean closed = false;
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
    public AbstractHibernateStorage(Properties dbProperties, String persistenceUnit) {
        this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit, dbProperties);
        this.jdbcUrl = dbProperties.getProperty(JpaConfiguration.JAKARTA_PERSISTENCE_JDBC_URL);
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
    public synchronized void close() {
        if (!this.closed) {
            this.entityManagerFactory.close();
            this.closed = true;
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
        Optional<T> instance = transaction.getSession().bySimpleNaturalId(entityClass).loadOptional(id);
        if (instance.isEmpty()) {
            T newInstance = creator.get();
            transaction.getEntityManager().persist(newInstance);
            return newInstance;
        } else {
            return instance.get();
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
            return results.get(0);
        }
    }

    /**
     * Method that checks whether the storage has been closed and throws an {@link IllegalStateException} if it has
     *
     * @throws IllegalStateException Thrown when the storage has been closed
     */
    protected final void ensureNotClosed() {
        if (this.closed) {
            throw new IllegalStateException("Storage already closed");
        }
    }

    /**
     * Gets whether the storage has been closed, for most use cases derived implementations likely want to call
     * {@link #ensureNotClosed()} instead.
     *
     * @return True if closed, false otherwise
     */
    protected boolean isClosed() {
        return this.closed;
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
        // And that the given notification exists
        TStored instance = transaction.getSession().bySimpleNaturalId(entityClass).loadOptional(id).orElse(null);
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
     * As opposed to the {@link #loadByNamedQuery(TransactionContext, Class, String)} overload this method allows for a
     * loader function that can mutate the loaded entities before returning it e.g. unwrapping a JPA
     * {@link jakarta.persistence.Entity} into a plain POJO, calculating computed properties, forcing lazily loaded JPA
     * properties to be fetched etc.
     * </p>
     *
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
        ensureNotClosed();
        TypedQuery<TStored> all = prepareNamedQuery(transaction, entityClass, queryName, queryParameters);
        return loader.apply(all.getResultList());
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
        ensureNotClosed();

        // Build a simple Criteria Query for the entity type
        CriteriaBuilder cb = transaction.getSession().getCriteriaBuilder();
        CriteriaQuery<TStored> cq = cb.createQuery(entityClass);
        Root<TStored> rootEntry = cq.from(entityClass);
        CriteriaQuery<TStored> all = cq.select(rootEntry);

        // Run the query and transform the results into the output type
        TypedQuery<TStored> allQuery = transaction.getSession().createQuery(all);
        List<TStored> stored = allQuery.getResultList();
        return loader.apply(stored);
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

}

