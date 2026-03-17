/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rocksdb;

import org.rocksdb.RocksDBException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TestForEach extends AbstractRocksDBTests {

    private static void insertKeyValues(Map<String, String> inputs, TransactionContext context,
                                        External external) throws
            RocksDBException {
        for (Map.Entry<String, String> input : inputs.entrySet()) {
            context.put(external.getDefaultHandle(), input.getKey().getBytes(StandardCharsets.UTF_8),
                        input.getValue().getBytes(
                                StandardCharsets.UTF_8));
        }
    }

    @Test
    public void givenEmptyStorage_whenForEach_thenConsumerNeverInvoked() throws RocksDBException, IOException {
        // Given
        AtomicInteger counter = new AtomicInteger(0);
        try (External external = new External(this.dbDir)) {
            try (TransactionContext context = external.start()) {
                // When
                context.forEach(external.getDefaultHandle(), kv -> counter.incrementAndGet());

                // Then
                Assert.assertEquals(counter.get(), 0);
            }
        }
    }

    @DataProvider(name = "inputs")
    private Object[][] inputs() {
        return new Object[][] {
                { Map.of("test", "test") },
                { Map.of("foo", "a", "bar", "b") },
                { Map.of("a", "1", "b", "2", "c", "3", "z", "26") }
        };
    }

    @Test(dataProvider = "inputs")
    public void givenSomeKeys_whenForEach_thenConsumerInvokedExpectedNumberOfTimes(Map<String, String> inputs) throws
            RocksDBException, IOException {
        // Given
        AtomicInteger counter = new AtomicInteger(0);
        try (External external = new External(this.dbDir)) {
            try (TransactionContext context = external.start()) {
                insertKeyValues(inputs, context, external);

                // When
                context.forEach(external.getDefaultHandle(), kv -> counter.incrementAndGet());

                // Then
                Assert.assertEquals(counter.get(), inputs.size());
            }
        }
    }

    @Test(dataProvider = "inputs")
    public void givenSomeKeys_whenForEachRecordsThem_thenOriginalKeyValuesReturned(Map<String, String> inputs) throws
            RocksDBException,
            IOException {
        // Given
        Map<String, String> recorded = new HashMap<>();
        try (External external = new External(this.dbDir)) {
            try (TransactionContext context = external.start()) {
                insertKeyValues(inputs, context, external);

                // When
                context.forEach(external.getDefaultHandle(),
                                kv -> recorded.put(new String(kv.key(), StandardCharsets.UTF_8),
                                                   new String(kv.value(), StandardCharsets.UTF_8)));

                // Then
                Assert.assertEquals(recorded, inputs);
            }
        }
    }
}
