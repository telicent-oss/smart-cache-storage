/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rocksdb;

import org.rocksdb.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestNestedTransactionContext {

    @DataProvider(name = "nestingLevels")
    private Object[][] nestingLevels() {
        return new Object[][] {
                { 1 },
                { 5 },
                { 25 }
        };
    }

    private static void verifyNestedCommitDoesNotCommit(NestedTransactionContext context, Transaction transaction, int recurse) throws RocksDBException {
        try (NestedTransactionContext nested = context.increment()) {
            if (recurse > 1) {
                verifyNestedCommitDoesNotCommit(nested, transaction, recurse - 1);
            }

            nested.commit();
            Assert.assertTrue(nested.isActive());
            verify(transaction, never()).commit();
        }
        Assert.assertTrue(context.isActive());
    }

    private static void verifyNestedCloseDoesNotClose(NestedTransactionContext context, Transaction transaction, int recurse) throws
            RocksDBException {
        NestedTransactionContext nested = context.increment();
        if (recurse > 1) {
            verifyNestedCloseDoesNotClose(nested, transaction, recurse - 1);
        }
        nested.close();
        Assert.assertTrue(nested.isActive());
        verify(transaction, never()).rollback();
        verify(transaction, never()).close();
    }

    @Test(dataProvider = "nestingLevels")
    public void givenDeeplyNestedTransactionContext_whenCommittingNestedTransaction_thenCommitIsDelayedUnderOutermostTransactionReached(int nestingLevels) throws
            RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        try (NestedTransactionContext context = new NestedTransactionContext(db, readOptions, writeOptions)) {
            // When
            verifyNestedCommitDoesNotCommit(context, transaction, nestingLevels);

            // Then
            context.commit();
            verify(transaction, times(1)).commit();
        }
    }

    @Test(dataProvider = "nestingLevels")
    public void givenShallowlyNestedTransactionContext_whenCommittingNestedTransaction_thenCommitIsDelayedUnderOutermostTransactionReached(int nestingLevels) throws
            RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        try (NestedTransactionContext context = new NestedTransactionContext(db, readOptions, writeOptions)) {
            // When
            for (int i = 1; i <= nestingLevels; i++) {
                verifyNestedCommitDoesNotCommit(context, transaction, 1);
            }

            // Then
            context.commit();
            verify(transaction, times(1)).commit();
        }
    }

    @Test(dataProvider = "nestingLevels")
    public void givenDeeplyNestedTransactionContext_whenClosingNestedTransaction_thenCloseIsDelayedUnderOutermostTransactionReached(int nestingLevels) throws
            RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        try (NestedTransactionContext context = new NestedTransactionContext(db, readOptions, writeOptions)) {
            // When
            verifyNestedCloseDoesNotClose(context, transaction, nestingLevels);
        }

        // Then
        verify(transaction, times(1)).rollback();
        verify(transaction, times(1)).close();
    }

    @Test(dataProvider = "nestingLevels")
    public void givenShallowlyNestedTransactionContext_whenClosingNestedTransaction_thenCloseIsDelayedUnderOutermostTransactionReached(int nestingLevels) throws
            RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        try (NestedTransactionContext context = new NestedTransactionContext(db, readOptions, writeOptions)) {
            // When
            for (int i = 1; i <= nestingLevels; i++) {
                verifyNestedCloseDoesNotClose(context, transaction, 1);
            }
        }

        // Then
        verify(transaction, times(1)).rollback();
        verify(transaction, times(1)).close();
    }
}
