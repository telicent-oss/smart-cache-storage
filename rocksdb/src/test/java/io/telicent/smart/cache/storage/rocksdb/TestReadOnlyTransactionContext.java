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
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.ReadOptions;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.rocksdb.Transaction;
import org.rocksdb.TransactionDB;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class TestReadOnlyTransactionContext {

    @DataProvider(name = "closedConsumers")
    private Object[][] closedConsumers() {
        ColumnFamilyHandle handle = mock(ColumnFamilyHandle.class);
        return new Object[][] {
                { consumer(context -> {
                    try {
                        context.get(handle, "key".getBytes());
                    } catch (RocksDBException e) {
                        fail("Unexpected RocksDBException", e);
                    }
                }) },
                { consumer(context -> {
                    try {
                        context.multiGetAsList(List.of(handle), List.of("key".getBytes()));
                    } catch (RocksDBException e) {
                        fail("Unexpected RocksDBException", e);
                    }
                }) },
                { consumer(context -> context.count(handle)) },
                { consumer(context -> context.isEmpty(handle)) },
                { consumer(context -> context.forEach(handle, kv -> {
                })) },
                { consumer(context -> context.iterator(handle)) }
        };
    }

    private Consumer<ReadOnlyTransactionContext> consumer(Consumer<ReadOnlyTransactionContext> consumer) {
        return consumer;
    }

    @Test
    public void givenStandaloneReadOnlyContext_whenReading_thenDirectDbReadUsedWithoutTransactionAllocation() throws
            RocksDBException {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        ColumnFamilyHandle handle = mock(ColumnFamilyHandle.class);
        byte[] key = "key".getBytes();
        byte[] value = "value".getBytes();
        when(db.get(handle, readOptions, key)).thenReturn(value);

        // When
        try (ReadOnlyTransactionContext context = new ReadOnlyTransactionContext(db, readOptions, false, metrics)) {
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
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        ColumnFamilyHandle handle = mock(ColumnFamilyHandle.class);
        List<ColumnFamilyHandle> handles = List.of(handle);
        List<byte[]> keys = List.of("key".getBytes());
        List<byte[]> values = List.of("value".getBytes());
        when(db.multiGetAsList(readOptions, handles, keys)).thenReturn(values);

        // When
        try (ReadOnlyTransactionContext context = new ReadOnlyTransactionContext(db, readOptions, false, metrics)) {
            // Then
            assertEquals(context.multiGetAsList(handles, keys), values);
            verify(db, never()).beginTransaction(any());
            verify(db, times(1)).multiGetAsList(readOptions, handles, keys);
        }
    }

    @Test
    public void givenStandaloneReadOnlyContext_whenIterating_thenDirectDbIteratorUsedWithoutTransactionAllocation() {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        ColumnFamilyHandle handle = mock(ColumnFamilyHandle.class);
        RocksIterator iterator = mock(RocksIterator.class);
        when(db.newIterator(handle, readOptions)).thenReturn(iterator);
        when(iterator.isValid()).thenReturn(false);

        // When
        try (ReadOnlyTransactionContext context = new ReadOnlyTransactionContext(db, readOptions, false, metrics)) {
            // Then
            assertEquals(context.count(handle), 0L);
            assertTrue(context.isEmpty(handle));
            verify(db, never()).beginTransaction(any());
            verify(db, atLeastOnce()).newIterator(handle, readOptions);
            verify(iterator, atLeastOnce()).seekToFirst();
        }
    }

    @Test
    public void givenOwnedReadOptions_whenCommittingOrClosing_thenOptionsClosed() {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);

        // When
        try (ReadOnlyTransactionContext context = new ReadOnlyTransactionContext(db, readOptions, metrics)) {
            assertTrue(context.isActive());
            context.commit();
            assertFalse(context.isActive());
            context.close();
        }

        // Then
        verify(readOptions, times(1)).close();
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void givenReadOnlyContext_whenWriting_thenUnsupported() {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        ColumnFamilyHandle handle = mock(ColumnFamilyHandle.class);

        // When
        try (ReadOnlyTransactionContext context = new ReadOnlyTransactionContext(db, readOptions, false, metrics)) {
            context.put(handle, "key".getBytes(), "value".getBytes());
        }
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void givenReadOnlyContext_whenDeleting_thenUnsupported() {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);
        ColumnFamilyHandle handle = mock(ColumnFamilyHandle.class);

        // When
        try (ReadOnlyTransactionContext context = new ReadOnlyTransactionContext(db, readOptions, false, metrics)) {
            context.delete(handle, "key".getBytes());
        }
    }

    @Test(expectedExceptions = UnsupportedOperationException.class, dataProvider = "closedConsumers")
    public void givenReadOnlyContext_whenUsedAfterCommit_thenUnsupported(
            Consumer<ReadOnlyTransactionContext> consumer) {
        // Given
        TransactionDB db = mock(TransactionDB.class);
        MetricsHolder metrics = mock(MetricsHolder.class);
        ReadOptions readOptions = mock(ReadOptions.class);

        // When
        ReadOnlyTransactionContext context = new ReadOnlyTransactionContext(db, readOptions, false, metrics);
        context.commit();

        // Then
        consumer.accept(context);
    }
}
