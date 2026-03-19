/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rocksdb;

import org.mockito.Mockito;
import org.rocksdb.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestShortLivedTransactionContext {

    @Test
    public void givenMockTransaction_whenCommitting_thenCommitsTransaction_andCommitAgainIsSafe() throws
            RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        try (ShortLivedTransactionContext context = new ShortLivedTransactionContext(db, readOptions, writeOptions)) {
            // When
            context.commit();

            // Then
            verify(transaction, times(1)).commit();
            verify(transaction, times(1)).close();

            // And
            context.commit();
            verify(transaction, times(1)).commit();
            verify(transaction, times(1)).close();
        }
    }

    @Test
    public void givenMockTransaction_whenClosingWithoutCommit_thenTransactionIsRolledBack_andCloseAgainIsSafe() throws
            RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        try (ShortLivedTransactionContext context = new ShortLivedTransactionContext(db, readOptions, writeOptions)) {
            // When
            context.close();

            // Then
            verify(transaction, never()).commit();
            verify(transaction, times(1)).rollback();
            verify(transaction, times(1)).close();
        }

        // And
        verify(transaction, times(1)).rollback();
        verify(transaction, times(1)).close();
    }

    @DataProvider(name = "consumers")
    private Object[][] consumers() {
        ColumnFamilyHandle handle = Mockito.mock(ColumnFamilyHandle.class);
        return new Object[][] {
                { consumer(t -> t.isEmpty(handle)) },
                { consumer(t -> t.count(handle)) },
                {
                        consumer(t -> {
                            try {
                                t.get(handle, new byte[0]);
                            } catch (RocksDBException e) {
                                // Ignore
                            }
                        })
                },
                {
                        consumer(t -> {
                            try {
                                t.put(handle, new byte[0], new byte[0]);
                            } catch (RocksDBException e) {
                                // Ignore
                            }
                        })
                },
                {
                        consumer(t -> {
                            try {
                                t.multiGetAsList(List.of(handle), List.of(new byte[0]));
                            } catch (RocksDBException e) {
                                // Ignore
                            }
                        })
                },
                { consumer(t -> t.count(handle)) },
                { consumer(t -> t.isEmpty(handle)) }
        };
    }

    private Consumer<TransactionContext> consumer(Consumer<TransactionContext> consumer) {
        return consumer;
    }

    @Test(expectedExceptions = IllegalStateException.class, dataProvider = "consumers")
    public void givenMockTransaction_whenClosed_thenOperationsAfterCloseThrowsIllegalState(
            Consumer<TransactionContext> consumer) {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        try (TransactionContext context = new ShortLivedTransactionContext(db, readOptions, writeOptions)) {
            // When
            context.close();

            // Then
            consumer.accept(context);
        }
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void givenMockTransaction_whenRollbackFails_thenThrowsError() throws RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        doThrow(new RocksDBException("failed")).when(transaction).rollback();
        when(db.beginTransaction(any())).thenReturn(transaction);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        try (ShortLivedTransactionContext context = new ShortLivedTransactionContext(db, readOptions, writeOptions)) {
            // When and Then
            context.close();
        }
    }
}
