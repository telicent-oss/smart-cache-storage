/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.hibernate.Session;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Mockito.*;

public class TestShortLivedTransactionContext {

    private EntityTransaction mockTransaction() {
        EntityTransaction transaction = mock(EntityTransaction.class);
        AtomicBoolean active = new AtomicBoolean(false);

        // Mock begin()
        doAnswer(invocation -> {
            active.set(true);
            return null;
        }).when(transaction).begin();
        // Mock isActive()
        when(transaction.isActive()).thenAnswer((Answer<Boolean>) invocation -> active.get());
        // Mock commit()
        doAnswer(mockTransactionStateChange(active)).when(transaction).commit();
        // Mock rollback()
        doAnswer(mockTransactionStateChange(active)).when(transaction).rollback();

        return transaction;
    }

    private static Answer<Object> mockTransactionStateChange(AtomicBoolean active) {
        return invocation -> {
            if (active.get()) {
                active.set(false);
            } else {
                throw new IllegalStateException("Transaction is not active");
            }
            return null;
        };
    }

    private EntityManagerFactory mockEntityManagerFactory(EntityTransaction transaction) {
        EntityManagerFactory entityManagerFactory = Mockito.mock(EntityManagerFactory.class);
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        Session session = Mockito.mock(Session.class);
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(entityManager.getTransaction()).thenReturn(transaction);
        return entityManagerFactory;
    }

    @Test
    public void givenGoodTransaction_whenUsingShortLivedTransaction_thenExpectedMethodsAreCalled() {
        // Given
        EntityTransaction transaction = mockTransaction();
        EntityManagerFactory factory = mockEntityManagerFactory(transaction);

        // When
        try (TransactionContext context = new ShortLivedTransactionContext(factory)) {
            // Then
            Assert.assertTrue(context.isActive());
            verify(transaction, times(1)).begin();

            context.commit();
            verify(transaction, times(1)).commit();
        }
    }

    @Test
    public void givenGoodTransaction_whenUsingShortLivedTransactionWithoutCommit_thenRollbackCalled() {
        // Given
        EntityTransaction transaction = mockTransaction();
        EntityManagerFactory factory = mockEntityManagerFactory(transaction);

        // When
        try (TransactionContext context = new ShortLivedTransactionContext(factory)) {
            // Then
            Assert.assertTrue(context.isActive());
            verify(transaction, times(1)).begin();
        }

        verify(transaction, times(1)).rollback();
    }

    @Test
    public void givenBadTransaction_whenUsingShortLivedTransactionWithoutCommit_thenRollbackCalled() {
        // Given
        EntityTransaction transaction = mockTransaction();
        doThrow(new RuntimeException("bad")).when(transaction).rollback();
        EntityManagerFactory factory = mockEntityManagerFactory(transaction);

        // When
        try (TransactionContext context = new ShortLivedTransactionContext(factory)) {
            // Then
            Assert.assertTrue(context.isActive());
            verify(transaction, times(1)).begin();
        }

        verify(transaction, times(1)).rollback();
    }

}
