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

import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.ReadOptions;
import org.rocksdb.RocksDBException;
import org.rocksdb.Transaction;
import org.rocksdb.TransactionDB;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class TestReadOnlyTransactionContext {

    @Test
    public void givenStandaloneReadOnlyContext_whenReading_thenDirectDbReadUsedWithoutTransactionAllocation() throws
            RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        ReadOptions readOptions = mock(ReadOptions.class);
        ColumnFamilyHandle handle = mock(ColumnFamilyHandle.class);
        byte[] key = "key".getBytes();
        byte[] value = "value".getBytes();
        when(db.get(handle, readOptions, key)).thenReturn(value);

        // When
        try (ReadOnlyTransactionContext context = new ReadOnlyTransactionContext(db, readOptions, false)) {
            // Then
            assertEquals(context.get(handle, key), value);
            verify(db, never()).beginTransaction(any());
            verify(db, times(1)).get(handle, readOptions, key);
        }
    }

    @Test
    public void givenStandaloneReadOnlyContext_whenMultiGetting_thenDirectDbMultiGetUsedWithoutTransactionAllocation()
            throws RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        ReadOptions readOptions = mock(ReadOptions.class);
        ColumnFamilyHandle handle = mock(ColumnFamilyHandle.class);
        List<ColumnFamilyHandle> handles = List.of(handle);
        List<byte[]> keys = List.of("key".getBytes());
        List<byte[]> values = List.of("value".getBytes());
        when(db.multiGetAsList(readOptions, handles, keys)).thenReturn(values);

        // When
        try (ReadOnlyTransactionContext context = new ReadOnlyTransactionContext(db, readOptions, false)) {
            // Then
            assertEquals(context.multiGetAsList(handles, keys), values);
            verify(db, never()).beginTransaction(any());
            verify(db, times(1)).multiGetAsList(readOptions, handles, keys);
        }
    }

    @Test
    public void givenOwnedReadOptions_whenCommittingOrClosing_thenOptionsClosed() {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        ReadOptions readOptions = mock(ReadOptions.class);

        // When
        try (ReadOnlyTransactionContext context = new ReadOnlyTransactionContext(db, readOptions)) {
            context.commit();
            context.close();
        }

        // Then
        verify(readOptions, times(1)).close();
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void givenReadOnlyContext_whenWriting_thenUnsupported() {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        ColumnFamilyHandle handle = mock(ColumnFamilyHandle.class);

        // When
        try (ReadOnlyTransactionContext context = new ReadOnlyTransactionContext(db, readOptions, false)) {
            context.put(handle, "key".getBytes(), "value".getBytes());
        }
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void givenReadOnlyContext_whenUsedAfterCommit_thenIllegalState() throws RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        ColumnFamilyHandle handle = mock(ColumnFamilyHandle.class);

        // When
        ReadOnlyTransactionContext context = new ReadOnlyTransactionContext(db, readOptions, false);
        context.commit();
        context.get(handle, "key".getBytes());
    }
}
