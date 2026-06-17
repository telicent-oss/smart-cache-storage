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

import org.rocksdb.RocksDBException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TestReadOnlyTransactions extends AbstractRocksDBTests {

    private static final byte[] KEY = "key".getBytes(StandardCharsets.UTF_8);
    private static final byte[] VALUE = "value".getBytes(StandardCharsets.UTF_8);

    @Test
    public void givenNoData_whenReadingViaReadOnlyTransaction_thenNull() throws Exception {
        // Given
        try (External external = new External(this.dbDir)) {
            // When
            try (TransactionContext read = external.startReadOnly()) {
                // Then
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
                    Assert.assertEquals(read.get(external.getDefaultHandle(), KEY), VALUE);
                }

                write.commit();
            }

            // And the value remains visible after commit via a fresh standalone read-only transaction
            try (TransactionContext read = external.startReadOnly()) {
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

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = ".*do not permit writes")
    public void givenReadOnlyTransaction_whenAttemptingWrite_thenIllegalState() throws RocksDBException, IOException {
        // Given
        try (External external = new External(this.dbDir)) {
            try (TransactionContext transaction = external.startReadOnly()) {
                transaction.put(external.getDefaultHandle(), KEY, VALUE);
            }
        }
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = ".*do not permit writes")
    public void givenReadOnlyTransaction_whenAttemptingDelete_thenIllegalState() throws RocksDBException, IOException {
        // Given
        try (External external = new External(this.dbDir)) {
            try (TransactionContext transaction = external.startReadOnly()) {
                transaction.delete(external.getDefaultHandle(), KEY);
            }
        }
    }
}
