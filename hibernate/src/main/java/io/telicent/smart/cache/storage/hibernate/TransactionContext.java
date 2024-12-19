/**
 * Copyright (C) 2022 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;

/**
 * Transaction Context
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
