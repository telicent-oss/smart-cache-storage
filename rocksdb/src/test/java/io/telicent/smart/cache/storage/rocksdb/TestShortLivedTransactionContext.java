package io.telicent.smart.cache.storage.rocksdb;

import org.rocksdb.*;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestShortLivedTransactionContext {

    @Test
    public void givenMockTransaction_whenCommitting_thenCommitsTransaction_andCommitAgainIsSafe() throws RocksDBException {
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
    public void givenMockTransaction_whenClosingWithoutCommit_thenTransactionIsRolledBack_andCloseAgainIsSafe() throws RocksDBException {
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
}
