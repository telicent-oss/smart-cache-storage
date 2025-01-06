/**
 * Copyright (C) 2022 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.ToString;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A Hibernate based notifications store that can persist to some underlying database supported by Hibernate
 * <p>
 * This is the only persistent storage implementation currently provided, it is persistent provided that the underlying
 * database used is persistent.  For example in a test scenario you could use an H2 In-Memory store that would be
 * non-persistent, but in production we'd generally expect to be using a persistent Postgres database.
 * </p>
 */
@ToString
@SuppressWarnings("unused")
public abstract class AbstractHibernateStorage implements AutoCloseable {

    @ToString.Exclude
    private final EntityManagerFactory entityManagerFactory;

    private volatile boolean closed = false;
    protected final String jdbcUrl;

    /**
     * Creates a new hibernate backed store
     *
     * @param dbProperties    Database Connection properties, this should contain at least a
     *                        {@value HibernateConfiguration#JAKARTA_PERSISTENCE_JDBC_URL} property to provide a JDBC
     *                        connection to the database as well as any other relevant properties e.g.
     *                        {@value HibernateConfiguration#JAKARTA_PERSISTENCE_JDBC_USER},
     *                        {@value HibernateConfiguration#HIBERNATE_DIALECT} etc.
     * @param persistenceUnit The name of the persistence unit to use, this should generally contain only the basic
     *                        configuration e.g. Entity Classes, JPA Provider, generic JPA configuration.  The actual
     *                        database connection properties <strong>MUST</strong> be supplied via the
     *                        {@code dbProperties} parameter instead.
     */
    public AbstractHibernateStorage(Properties dbProperties, String persistenceUnit) {
        this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit, dbProperties);
        this.jdbcUrl = dbProperties.getProperty(HibernateConfiguration.JAKARTA_PERSISTENCE_JDBC_URL);
    }

    /**
     * Begins a transaction internally
     * </p>
     * <p>
     * Callers <strong>MUST</strong> ensure they use the return value in a try-with-resources block so that short-lived
     * transactions are promptly commited/aborted as appropriate.
     * </p>
     * <p>
     * If callers need to do multiple operations within a transaction they should use the various overloads of methods
     * that take a {@link TransactionContext} as their first argument
     * </p>
     *
     * @return Transaction Context
     */
    protected final TransactionContext beginInternal() {
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
     * @return Entity instance
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
     * Gets or creates an entity based on the outcome of a named query
     *
     * @param transaction     Transaction Context
     * @param entityClass     Entity Class
     * @param queryName       Query Name
     * @param queryParameters Query parameters
     * @param creator         Supplier to create a new entity instance if the named query yields no result
     * @param <T>             Entity type
     * @return Entity instance
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
     * Loads all entities
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
        // Cache the named queries for performance reasons
        TypedQuery<T> query = transaction.getEntityManager().createNamedQuery(queryName, entityClass);
        if (queryParameters != null) {
            for (Map.Entry<String, Object> entry : queryParameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query;
    }

}

