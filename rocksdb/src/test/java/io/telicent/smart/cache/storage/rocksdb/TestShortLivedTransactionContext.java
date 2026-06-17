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
package io.telicent.smart.cache.storage.rocksdb;

import io.telicent.smart.cache.storage.rocksdb.metrics.MetricsHolder;
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
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        try (ShortLivedTransactionContext context = new ShortLivedTransactionContext(db, readOptions, writeOptions,
                                                                                     metrics)) {
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
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        try (ShortLivedTransactionContext context = new ShortLivedTransactionContext(db, readOptions, writeOptions,
                                                                                     metrics)) {
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
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        try (TransactionContext context = new ShortLivedTransactionContext(db, readOptions, writeOptions, metrics)) {
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
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        try (ShortLivedTransactionContext context = new ShortLivedTransactionContext(db, readOptions, writeOptions,
                                                                                     metrics)) {
            // When and Then
            context.close();
        }
    }

    @Test
    public void givenMockTransaction_whenDeleting_thenDelegatesDeleteToRocksTransaction() throws RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        ColumnFamilyHandle handle = mock(ColumnFamilyHandle.class);
        byte[] key = "deleteKey".getBytes();

        try (ShortLivedTransactionContext context = new ShortLivedTransactionContext(db, readOptions, writeOptions,
                                                                                     metrics)) {
            // When
            context.delete(handle, key);

            // Then
            verify(transaction, times(1)).delete(handle, key);
        }
    }

    @Test(expectedExceptions = RocksDBException.class)
    public void givenMockTransaction_whenDeleteFails_thenRocksDBExceptionPropagates() throws RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        ColumnFamilyHandle handle = mock(ColumnFamilyHandle.class);
        doThrow(new RocksDBException("delete failed")).when(transaction).delete(any(), any(byte[].class));
        when(db.beginTransaction(any())).thenReturn(transaction);
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);

        try (ShortLivedTransactionContext context = new ShortLivedTransactionContext(db, readOptions, writeOptions,
                                                                                     metrics)) {
            // When and Then
            context.delete(handle, "key".getBytes());
        }
    }

    @Test
    public void givenOwnedOptions_whenCommitting_thenOptionsAreClosed() throws RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        // This constructor owns the options
        try (ShortLivedTransactionContext context = new ShortLivedTransactionContext(db, readOptions, writeOptions, metrics)) {
            // When
            context.commit();

            // Then - commit() itself must release the owned native options (leak-safety), not defer to close()
            verify(readOptions, atLeastOnce()).close();
            verify(writeOptions, atLeastOnce()).close();
        }
    }

    @Test
    public void givenOwnedOptions_whenClosingWithoutCommit_thenOptionsAreClosed() {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        ShortLivedTransactionContext context = new ShortLivedTransactionContext(db, readOptions, writeOptions, metrics);

        // When
        context.close();

        // Then
        verify(readOptions, atLeastOnce()).close();
        verify(writeOptions, atLeastOnce()).close();
    }

    @Test
    public void givenSharedOptions_whenCommittingAndClosing_thenOptionsAreNotClosed() throws RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        // ownsOptions = false => the options are shared/owned by the caller and must never be closed by the context
        try (ShortLivedTransactionContext context =
                     new ShortLivedTransactionContext(db, readOptions, writeOptions, false, metrics)) {
            // When
            context.commit();
        }

        // Then
        verify(readOptions, never()).close();
        verify(writeOptions, never()).close();
    }

    @Test
    public void givenSnapshotRequested_whenCreated_thenSnapshotTaken() {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);

        // When (constructors default to taking a snapshot)
        try (ShortLivedTransactionContext context = new ShortLivedTransactionContext(db, readOptions, writeOptions, metrics)) {
            // Then
            verify(transaction, times(1)).setSnapshot();
        }
    }

    @Test
    public void givenNoSnapshotRequested_whenCreated_thenSnapshotNotTaken() {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);

        // When (withSnapshot = false)
        try (ShortLivedTransactionContext context =
                     new ShortLivedTransactionContext(db, readOptions, writeOptions, false, false, metrics)) {
            // Then - read-only transactions must not pin a snapshot
            verify(transaction, never()).setSnapshot();
        }
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void givenClosedContext_whenDeleting_thenIllegalState() throws RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        ColumnFamilyHandle handle = mock(ColumnFamilyHandle.class);

        try (ShortLivedTransactionContext context = new ShortLivedTransactionContext(db, readOptions, writeOptions, metrics)) {
            // When
            context.close();

            // Then - delete after close must be rejected like the other operations
            context.delete(handle, "key".getBytes());
        }
    }
}
