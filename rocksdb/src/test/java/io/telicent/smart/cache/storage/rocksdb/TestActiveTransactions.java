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
import org.apache.commons.lang3.RandomUtils;
import org.rocksdb.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestActiveTransactions {

    @Test
    public void givenActiveTransactions_whenInspecting_thenEmpty() {
        // Given
        ActiveTransactions active = new ActiveTransactions();

        // When and Then
        Assert.assertEquals(active.count(), 0);
    }

    @Test
    public void givenActiveTransactions_whenTracking_thenNonEmpty_andCanBeRemoved() {
        // Given
        TransactionContext context = mock(TransactionContext.class);
        ActiveTransactions active = new ActiveTransactions();

        // When
        active.track(context);

        // Then
        Assert.assertEquals(active.count(), 1L);

        // And
        active.release(context);
        Assert.assertEquals(active.count(), 0);
    }

    @DataProvider(name = "txnEndings")
    private Object[][] transactionEndings() {
        return new Object[][] {
                {
                        consumer(t -> {
                            try {
                                t.commit();
                            } catch (RocksDBException e) {
                                // Ignore
                            }
                        })
                },
                { consumer(TransactionContext::close) },
                { consumer(TransactionContext::invalidate) }
        };
    }

    private Consumer<TransactionContext> consumer(Consumer<TransactionContext> consumer) {
        return consumer;
    }

    @Test(dataProvider = "txnEndings")
    public void givenActiveTransactions_whenCreatingAShortLivedTransaction_thenEndingTheTransactionReleasesIt(
            Consumer<TransactionContext> consumer) {
        // Given
        ActiveTransactions active = new ActiveTransactions();
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);

        // When
        try (TransactionContext context = new ShortLivedTransactionContext(db, mock(
                ReadOptions.class), mock(WriteOptions.class), false, mock(MetricsHolder.class), active)) {
            // Then
            Assert.assertEquals(active.count(), 1,
                                "Creating a transaction MUST register() it with the ActiveTransactions");
            consumer.accept(context);
            Assert.assertEquals(active.count(), 0,
                                "Ending a transaction MUST release() it from the ActiveTransactions");
        }
    }

    @Test(dataProvider = "txnEndings")
    public void givenActiveTransactions_whenCreatingAReadOnlyTransaction_thenEndingTheTransactionReleasesIt(
            Consumer<TransactionContext> consumer) {
        // Given
        ActiveTransactions active = new ActiveTransactions();
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);

        // When
        try (TransactionContext context = new ReadOnlyTransactionContext(db, mock(
                ReadOptions.class), false, mock(MetricsHolder.class), active)) {
            // Then
            Assert.assertEquals(active.count(), 1,
                                "Creating a transaction MUST register() it with the ActiveTransactions");
            consumer.accept(context);
            Assert.assertEquals(active.count(), 0,
                                "Ending a transaction MUST release() it from the ActiveTransactions");
        }
    }

    @Test(dataProvider = "txnEndings")
    public void givenActiveTransactions_whenCreatingANestedTransaction_thenEndingTheTransactionReleasesIt(
            Consumer<TransactionContext> consumer) {
        // Given
        ActiveTransactions active = new ActiveTransactions();
        TransactionDB db = mock(TransactionDB.class);
        Transaction transaction = mock(Transaction.class);
        when(db.beginTransaction(any())).thenReturn(transaction);

        // When
        try (TransactionContext context = new NestedTransactionContext(db, mock(
                ReadOptions.class), mock(WriteOptions.class), false, mock(MetricsHolder.class), active)) {
            // Then
            Assert.assertEquals(active.count(), 1,
                                "Creating a transaction MUST register() it with the ActiveTransactions");
            consumer.accept(context);
            Assert.assertEquals(active.count(), 0,
                                "Ending a transaction MUST release() it from the ActiveTransactions");
        }
    }

    @Test
    public void givenActiveTransactions_whenTrackingNull_thenNoOp() {
        // Given
        ActiveTransactions active = new ActiveTransactions();

        // When
        active.track(null);

        // Then
        Assert.assertEquals(active.count(), 0);
    }

    @Test
    public void givenActiveTransactions_whenReleasingNull_thenNoOp() {
        // Given
        ActiveTransactions active = new ActiveTransactions();
        TransactionContext txn = mock(TransactionContext.class);

        // When
        active.track(txn);
        Assert.assertEquals(active.count(), 1);
        active.release(null);

        // Then
        Assert.assertEquals(active.count(), 1, "Releasing null should not release anything");
    }

    @Test(invocationCount = 10)
    public void givenActiveTransactions_whenInvalidatingAll_thenInvalidateCalledOnEachTransaction() {
        // Given
        ActiveTransactions active = new ActiveTransactions();
        int count = RandomUtils.insecure().randomInt(1, 5);
        List<TransactionContext> transactions = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            transactions.add(mock(TransactionContext.class));
        }
        for (TransactionContext txn : transactions) {
            active.track(txn);
        }

        // When
        Assert.assertEquals(active.count(), count);
        active.invalidateAll();

        // Then
        Assert.assertEquals(active.count(), 0);
        for (TransactionContext txn : transactions) {
            verify(txn, times(1)).invalidate();
        }
    }
}
