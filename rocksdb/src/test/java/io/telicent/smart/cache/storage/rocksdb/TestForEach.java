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

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static io.telicent.smart.cache.storage.rocksdb.AbstractRocksDBStorage.bytesToLong;
import static io.telicent.smart.cache.storage.rocksdb.AbstractRocksDBStorage.longToBytes;

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

    @Test(dataProvider = "inputs")
    public void givenSomeKeys_whenManuallyIterating_thenOriginalKeyValuesReturned(Map<String, String> inputs) throws
            RocksDBException,
            IOException {
        // Given
        Map<String, String> recorded = new HashMap<>();
        try (External external = new External(this.dbDir)) {
            try (TransactionContext context = external.start()) {
                insertKeyValues(inputs, context, external);

                // When
                try (RocksIterator iterator = context.iterator(external.getDefaultHandle())) {
                    iterator.seekToFirst();
                    KeyValue keyValue = KeyValue.of(iterator);
                    while (iterator.isValid()) {
                        recorded.put(new String(keyValue.key(), StandardCharsets.UTF_8),
                                     new String(keyValue.value(), StandardCharsets.UTF_8));
                        iterator.next();
                    }
                }

                // Then
                Assert.assertEquals(recorded, inputs);
            }
        }
    }

    @Test(dataProvider = "inputs")
    public void givenSomeKeys_whenManuallyIteratingWithSeeks_thenOriginalKeyValuesReturned(
            Map<String, String> inputs) throws
            RocksDBException,
            IOException {
        // Given
        Map<String, String> recorded = new HashMap<>();
        try (External external = new External(this.dbDir)) {
            try (TransactionContext context = external.start()) {
                insertKeyValues(inputs, context, external);

                // When
                byte[] lastKey = null;
                while (recorded.size() < inputs.size()) {
                    try (RocksIterator iterator = context.iterator(external.getDefaultHandle())) {
                        if (lastKey == null) {
                            iterator.seekToFirst();
                        } else {
                            iterator.seek(lastKey);
                            // NB - This next() call is only necessary because we process a batch of 1 and never
                            // otherwise call next().  For a more normal batch processing loop where we call next()
                            // within the batch processing this would be unnecessary
                            iterator.next();
                        }
                        KeyValue keyValue = KeyValue.of(iterator);
                        if (iterator.isValid()) {
                            recorded.put(new String(keyValue.key(), StandardCharsets.UTF_8),
                                         new String(keyValue.value(), StandardCharsets.UTF_8));
                            lastKey = Arrays.copyOf(keyValue.key(), keyValue.key().length);
                        }
                    }
                }

                // Then
                Assert.assertEquals(recorded, inputs);
            }
        }
    }

    @Test
    public void givenNumericValues_whenIteratingInBatches_thenAllValuesRewritten() throws RocksDBException,
            IOException {
        // Given
        try (External external = new External(this.dbDir)) {
            Map<String, Long> initialValues = new LinkedHashMap<>();
            try (TransactionContext context = external.begin()) {
                for (int i = 1; i <= 10_000; i++) {
                    String key = RandomStringUtils.insecure().nextAlphabetic(10);
                    long value = RandomUtils.insecure().randomLong(0, 100_000);
                    initialValues.put(key, value);
                    context.put(external.getDefaultHandle(), key.getBytes(StandardCharsets.UTF_8),
                                longToBytes(value));
                }
                context.commit();
            }

            // When
            long totalProcessed = 0;
            boolean complete = false;
            byte[] lastKey = null;
            while (!complete) {
                try (TransactionContext context = external.begin()) {
                    try (RocksIterator iterator = context.iterator(external.getDefaultHandle())) {
                        // Start from beginning or resume from last processed key
                        if (lastKey == null) {
                            iterator.seekToFirst();
                        } else {
                            iterator.seek(lastKey);
                        }

                        int batchCount = 0;
                        while (iterator.isValid() && batchCount < 100) {
                            // Increment each value by 1
                            context.put(external.getDefaultHandle(),
                                        iterator.key(),
                                        longToBytes(bytesToLong(iterator.value()) + 1));
                            totalProcessed++;
                            batchCount++;
                            iterator.next();
                        }

                        complete = !iterator.isValid();
                        if (!complete) {
                            // Remember the last key we processed so next time round the loop we'll seek our new
                            // iterator from that point
                            lastKey = Arrays.copyOf(iterator.key(), iterator.key().length);
                        }
                    }

                    // Commit changes from current batch
                    context.commit();
                }
            }

            // Then
            Assert.assertEquals(totalProcessed, 10_000);
            try (TransactionContext context = external.begin()) {
                for (Map.Entry<String, Long> kvp : initialValues.entrySet()) {
                    long actual = bytesToLong(
                            context.get(external.getDefaultHandle(),
                                        kvp.getKey().getBytes(StandardCharsets.UTF_8)));
                    Assert.assertEquals(actual, kvp.getValue() + 1,
                                        "Key " + kvp.getKey() + " did not have its value incremented as expected");
                }
            }
        }
    }
}
