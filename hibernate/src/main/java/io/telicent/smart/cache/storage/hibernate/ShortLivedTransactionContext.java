/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;
import org.hibernate.Session;

import java.util.Objects;

/**
 * Short-lived transaction Context, intended for use within a single method, or to be passed between several methods to
 * orchestrate a larger transaction.  Guarantees to commit/abort the transaction upon closure.
 */
@Getter
final class ShortLivedTransactionContext implements TransactionContext {

    private final EntityManager entityManager;
    private final Session session;

    /**
     * Creates and starts a new Transaction
     *
     * @param entityManagerFactory Entity Manager Factory
     */
    public ShortLivedTransactionContext(EntityManagerFactory entityManagerFactory) {
        this.entityManager = Objects.requireNonNull(entityManagerFactory).createEntityManager();
        this.session = entityManager.unwrap(Session.class);

        // Start the transaction as soon as we are created, we're auto-closeable so should be used in a
        // try-with-resources block and our close() method will rollback the transaction if the caller didn't
        // explicitly commit() it
        this.entityManager.getTransaction().begin();
    }

    @Override
    public boolean isActive() {
        return this.entityManager.getTransaction().isActive();
    }

    /**
     * Commits the transaction
     */
    public void commit() {
        this.entityManager.getTransaction().commit();
    }

    /**
     * Closes the transaction, rolling it back if it wasn't {@link #commit()}'d, and closing the Hibernate session and
     * entity manager
     */
    @Override
    public void close() {
        // If anything went wrong, and we're still in the transaction roll it back
        if (this.entityManager.getTransaction().isActive()) {
            this.entityManager.getTransaction().rollback();
        }
        this.session.close();
        this.entityManager.close();
    }
}
