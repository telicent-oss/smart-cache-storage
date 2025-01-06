/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;

/**
 * Transaction Context
 * <p>
 * A transaction context provides access to the Hibernate {@link Session} and corresponding JPA {@link EntityManager}
 * for the lifetime of a transaction.  The context is auto-closeable so is intended to be used by callers in a
 * try-with-resources block e.g.
 * </p>
 * <pre>
 * try (TransactionContext transaction = startTransaction()) {
 *   // Do some transactional actions
 *
 *   // Commit our transaction
 *   transaction.commit();
 * }
 * </pre>
 * <p>
 * As seen in the above example callers are expected to {@link #commit()} the transaction when they have completed it,
 * if they do not commit it, or an error is thrown, when the try-with-resources block exits and calls the
 * {@link #close()} method the transaction is rolled back if it is still active.  Therefore, callers
 * <strong>MUST</strong> ensure that they call {@link #commit()} if they want any changes made to the storage to be
 * persistent.
 * </p>
 */
public interface TransactionContext extends AutoCloseable {

    /**
     * Commits the transaction
     */
    void commit();

    /**
     * Closes the transaction, rolling it back if it wasn't {@link #commit()}'d, and closing the Hibernate session and
     * entity manager
     */
    @Override
    void close();

    /**
     * Gets whether the transaction is active i.e. has yet to be {@link #commit()}'d or {@link #close()}'d
     *
     * @return True if active, false otherwise
     */
    boolean isActive();

    /**
     * Gets the entity manager for this transaction
     *
     * @return Entity Manager
     */
    EntityManager getEntityManager();

    /**
     * Gets the Hibernate Session for this transaction
     *
     * @return Hibernate Session
     */
    Session getSession();
}
