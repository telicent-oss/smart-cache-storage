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
        MetricsHolder metrics = mock(MetricsHolder.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        try (NestedTransactionContext context = new NestedTransactionContext(db, readOptions, writeOptions, metrics)) {
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
        MetricsHolder metrics = mock(MetricsHolder.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        try (NestedTransactionContext context = new NestedTransactionContext(db, readOptions, writeOptions, metrics)) {
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
        MetricsHolder metrics = mock(MetricsHolder.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        try (NestedTransactionContext context = new NestedTransactionContext(db, readOptions, writeOptions, metrics)) {
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
        MetricsHolder metrics = mock(MetricsHolder.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        ReadOptions readOptions = mock(ReadOptions.class);
        WriteOptions writeOptions = mock(WriteOptions.class);
        try (NestedTransactionContext context = new NestedTransactionContext(db, readOptions, writeOptions, metrics)) {
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
