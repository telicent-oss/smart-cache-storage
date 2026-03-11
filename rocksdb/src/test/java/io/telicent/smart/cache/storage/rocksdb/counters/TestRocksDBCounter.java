/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rocksdb.counters;

import io.telicent.smart.cache.storage.rocksdb.AbstractRocksDBTests;
import io.telicent.smart.cache.storage.rocksdb.RocksDBCounter;
import org.apache.commons.lang3.RandomUtils;
import org.mockito.Mockito;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDBException;
import org.rocksdb.TransactionDB;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TestRocksDBCounter extends AbstractRocksDBTests {

    public static final String NEVER_EQUALS =
            "Next counter value issued MUST never equal previous counter value issued";
    public static final String ALWAYS_GREATER =
            "Next counter value issued MUST always be greater than previous counter value";

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = ".*cannot be null")
    public void givenNoDb_whenCreatingCounter_thenNPE() throws RocksDBException {
        // Given, When and Then
        new RocksDBCounter(null, null, null);
    }

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = ".*cannot be null")
    public void givenNoColumnFamily_whenCreatingCounter_thenNPE() throws RocksDBException {
        // Given, When and Then
        new RocksDBCounter(Mockito.mock(TransactionDB.class), null, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*cannot be blank/empty")
    public void givenNoKey_whenCreatingCounter_thenNPE() throws RocksDBException {
        // Given, When and Then
        new RocksDBCounter(Mockito.mock(TransactionDB.class), Mockito.mock(ColumnFamilyHandle.class), null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*cannot be blank/empty")
    public void givenEmptyKey_whenCreatingCounter_thenNPE() throws RocksDBException {
        // Given, When and Then
        new RocksDBCounter(Mockito.mock(TransactionDB.class), Mockito.mock(ColumnFamilyHandle.class), "");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*cannot be blank/empty")
    public void givenBlankKey_whenCreatingCounter_thenNPE() throws RocksDBException {
        // Given, When and Then
        new RocksDBCounter(Mockito.mock(TransactionDB.class), Mockito.mock(ColumnFamilyHandle.class), "  ");
    }


    @DataProvider(name = "increments")
    private Object[][] increments() {
        return new Object[][] {
                { 1 },
                { 50 },
                { 10_000 }
        };
    }

    @Test(dataProvider = "increments")
    public void givenSingleCounter_whenIncrementingThenClosing_thenCounterValuesArePreservedOnReopen(
            int increments) throws RocksDBException, IOException {
        // Given
        long next = Long.MIN_VALUE;
        try (SingleCounter counter = new SingleCounter(this.dbDir)) {
            // When
            for (int i = 1; i <= increments; i++) {
                long temp = counter.next(SingleCounter.NAME, false);
                Assert.assertNotEquals(temp, next, NEVER_EQUALS);
                Assert.assertTrue(temp > next, ALWAYS_GREATER);
                next = temp;
            }
        }

        // Then
        try (SingleCounter counter = new SingleCounter(this.dbDir)) {
            long another = counter.next(SingleCounter.NAME, false);
            Assert.assertNotEquals(another, next, NEVER_EQUALS);
            Assert.assertTrue(another > next, ALWAYS_GREATER);
            Assert.assertFalse(counter.isEmpty());
            Assert.assertEquals(counter.count(), 1L);
        }
    }

    @Test(dataProvider = "increments")
    public void givenSingleCounter_whenIncrementingNonTransactionally_thenSyncResetsValue(int increments) throws
            RocksDBException, IOException {
        // Given
        try (SingleCounter counter = new SingleCounter(this.dbDir)) {
            // When
            for (int i = 1; i <= increments; i++) {
                counter.next(SingleCounter.NAME, false);
            }

            // Then
            counter.sync(SingleCounter.NAME);
            Assert.assertEquals(counter.get(SingleCounter.NAME), 1L);
            Assert.assertEquals(counter.get(SingleCounter.NAME.getBytes(StandardCharsets.UTF_8)), 1L);
        }
    }

    @Test(dataProvider = "increments")
    public void givenSingleCounter_whenIncrementingTransactionally_thenSyncKeepsIncrementedValue(int increments) throws
            RocksDBException, IOException {
        // Given
        try (SingleCounter counter = new SingleCounter(this.dbDir)) {
            // When
            long next = 1L;
            for (int i = 1; i <= increments; i++) {
                next = counter.next(SingleCounter.NAME, true);
            }

            // Then
            counter.sync(SingleCounter.NAME);
            Assert.assertEquals(counter.get(SingleCounter.NAME), next + 1);
            Assert.assertEquals(counter.get(SingleCounter.NAME.getBytes(StandardCharsets.UTF_8)), next + 1);
        }
    }

    @Test(dataProvider = "increments")
    public void givenManyCounters_whenIncrementing_thenEachIsIndependent(int increments) throws
            RocksDBException, IOException {
        // Given
        Map<String, Long> values = new HashMap<>();
        try (AlphabetCounters counters = new AlphabetCounters(this.dbDir)) {
            // When
            for (String name : AlphabetCounters.NAMES) {
                for (int i = 1; i < RandomUtils.insecure().randomInt(0, increments); i++) {
                    values.put(name, counters.next(name, false));
                }
            }
        }

        // Then
        try (AlphabetCounters counters = new AlphabetCounters(this.dbDir)) {
            for (String name : AlphabetCounters.NAMES) {
                long expected = values.getOrDefault(name, 0L);
                // NB - A counter persists the next unused value so the counter value will always be one more than the
                //      last value that was issued (or zero if no value was ever issued for it)
                Assert.assertEquals(counters.get(name), expected + 1, "Counter '" + name + "' had unexpected value");
                Assert.assertEquals(counters.get(name.getBytes(StandardCharsets.UTF_8)), expected + 1,
                                    "Counter '" + name + "' had unexpected value");
            }
            Assert.assertFalse(counters.isEmpty());
            Assert.assertEquals(counters.count(), AlphabetCounters.NAMES.size());
        }
    }

    @Test
    public void givenNoCounters_whenCheckingState_thenEmptyAndZeroCount() throws RocksDBException, IOException {
        // Given
        try (NoCounters none = new NoCounters(this.dbDir)) {
            // When and Then
            Assert.assertTrue(none.isEmpty());
            Assert.assertEquals(none.count(), 0L);
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void givenNoCounters_whenAccessing_thenIllegalArgument() throws RocksDBException, IOException {
        // Given
        try (NoCounters none = new NoCounters(this.dbDir)) {
            // When and Then
            none.get("test");
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void givenNoCounters_whenAccessingByBytesName_thenIllegalArgument() throws RocksDBException, IOException {
        // Given
        try (NoCounters none = new NoCounters(this.dbDir)) {
            // When and Then
            none.get("test".getBytes(StandardCharsets.UTF_8));
        }
    }
}
