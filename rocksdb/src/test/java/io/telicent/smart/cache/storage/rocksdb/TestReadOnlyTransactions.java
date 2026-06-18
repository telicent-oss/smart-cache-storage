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

import org.rocksdb.RocksIterator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public class TestReadOnlyTransactions extends AbstractRocksDBTests {

    private static final byte[] KEY = "key".getBytes(StandardCharsets.UTF_8);
    private static final byte[] OTHER_KEY = "other-key".getBytes(StandardCharsets.UTF_8);
    private static final byte[] VALUE = "value".getBytes(StandardCharsets.UTF_8);

    @Test
    public void givenNoData_whenReadingViaReadOnlyTransaction_thenNull() throws Exception {
        // Given
        try (External external = new External(this.dbDir)) {
            // When
            try (TransactionContext read = external.startReadOnly()) {
                // Then
                Assert.assertTrue(read instanceof ReadOnlyTransactionContext);
                Assert.assertNull(read.get(external.getDefaultHandle(), KEY));
            }
        }
    }

    @Test
    public void givenCommittedKey_whenReadingViaReadOnlyTransaction_thenValueVisible() throws Exception {
        // Given
        try (External external = new External(this.dbDir)) {
            try (TransactionContext write = external.start()) {
                write.put(external.getDefaultHandle(), KEY, VALUE);
                write.commit();
            }

            // When
            try (TransactionContext read = external.startReadOnly()) {
                // Then
                Assert.assertTrue(read instanceof ReadOnlyTransactionContext);
                Assert.assertEquals(read.get(external.getDefaultHandle(), KEY), VALUE);
            }
        }
    }

    @Test
    public void givenActiveWriteTransaction_whenReadingViaReadOnly_thenUncommittedWriteVisible() throws Exception {
        // Given
        try (External external = new External(this.dbDir)) {
            // When - a read-only transaction started within an active (write) transaction should join it so that
            //        read-your-writes semantics are preserved
            try (TransactionContext write = external.start()) {
                write.put(external.getDefaultHandle(), KEY, VALUE);

                try (TransactionContext read = external.startReadOnly()) {
                    // Then - the uncommitted write is visible
                    Assert.assertTrue(read instanceof NestedTransactionContext);
                    Assert.assertEquals(read.get(external.getDefaultHandle(), KEY), VALUE);
                }

                write.commit();
            }

            // And the value remains visible after commit via a fresh standalone read-only transaction
            try (TransactionContext read = external.startReadOnly()) {
                Assert.assertTrue(read instanceof ReadOnlyTransactionContext);
                Assert.assertEquals(read.get(external.getDefaultHandle(), KEY), VALUE);
            }
        }
    }

    @Test
    public void givenManyReadOnlyTransactions_whenSharedOptionsReused_thenAllSucceed() throws Exception {
        // Given
        try (External external = new External(this.dbDir)) {
            try (TransactionContext write = external.start()) {
                write.put(external.getDefaultHandle(), KEY, VALUE);
                write.commit();
            }

            // When - repeatedly opening read-only transactions reuses the shared read/write options without taking a
            //        snapshot.  Exercising the path many times confirms nothing is closed/freed prematurely.
            for (int i = 0; i < 100; i++) {
                try (TransactionContext read = external.startReadOnly()) {
                    // Then
                    Assert.assertEquals(read.get(external.getDefaultHandle(), KEY), VALUE);
                }
            }
        }
    }

    @Test
    public void givenNoData_whenCheckingViaReadOnly_thenEmptyAndZeroCount() throws Exception {
        // Given
        try (External external = new External(this.dbDir)) {
            // When and Then - exercises the empty branch of isEmpty()/count() on the direct read path
            try (TransactionContext read = external.startReadOnly()) {
                Assert.assertTrue(read.isEmpty(external.getDefaultHandle()));
                Assert.assertEquals(read.count(external.getDefaultHandle()), 0L);
            }
        }
    }

    @Test
    public void givenData_whenCountingViaReadOnly_thenAllEntriesCounted() throws Exception {
        // Given
        try (External external = new External(this.dbDir)) {
            try (TransactionContext write = external.start()) {
                write.put(external.getDefaultHandle(), KEY, VALUE);
                write.put(external.getDefaultHandle(), OTHER_KEY, VALUE);
                write.commit();
            }

            // When and Then - exercises the non-empty branch / iteration body of count() and isEmpty()
            try (TransactionContext read = external.startReadOnly()) {
                Assert.assertFalse(read.isEmpty(external.getDefaultHandle()));
                Assert.assertEquals(read.count(external.getDefaultHandle()), 2L);
            }
        }
    }

    @Test
    public void givenData_whenIteratingViaReadOnly_thenForEachAndIteratorVisitAllEntries() throws Exception {
        // Given
        try (External external = new External(this.dbDir)) {
            try (TransactionContext write = external.start()) {
                write.put(external.getDefaultHandle(), KEY, VALUE);
                write.put(external.getDefaultHandle(), OTHER_KEY, VALUE);
                write.commit();
            }

            // When - forEach visits every entry
            try (TransactionContext read = external.startReadOnly()) {
                AtomicInteger visited = new AtomicInteger();
                read.forEach(external.getDefaultHandle(), kv -> visited.incrementAndGet());
                // Then
                Assert.assertEquals(visited.get(), 2);
            }

            // When - a raw iterator visits every entry
            try (TransactionContext read = external.startReadOnly()) {
                int counted = 0;
                try (RocksIterator iterator = read.iterator(external.getDefaultHandle())) {
                    iterator.seekToFirst();
                    while (iterator.isValid()) {
                        counted++;
                        iterator.next();
                    }
                }
                // Then
                Assert.assertEquals(counted, 2);
            }
        }
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void givenReadOnlyTransaction_whenDeleting_thenUnsupported() throws Exception {
        // Given
        try (External external = new External(this.dbDir)) {
            try (TransactionContext read = external.startReadOnly()) {
                // When and Then
                read.delete(external.getDefaultHandle(), KEY);
            }
        }
    }
}
