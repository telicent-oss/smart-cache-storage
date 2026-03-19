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
import java.util.List;
import java.util.Objects;

public class TestTransactionNesting extends AbstractRocksDBTests {

    @Test
    public void givenBasicTransaction_whenWhenWritingWithCommit_thenBasicValueStored() throws RocksDBException,
            IOException {
        // Given
        try (Nestable nestable = new Nestable(this.dbDir)) {
            // When
            nestable.basic(true);

            // Then
            Assert.assertEquals(nestable.get(), "basic");
        }
    }

    @Test
    public void givenBasicTransaction_whenWhenWritingWithRollback_thenNoValueStored() throws RocksDBException,
            IOException {
        // Given
        try (Nestable nestable = new Nestable(this.dbDir)) {
            // When
            nestable.basic(false);

            // Then
            Assert.assertNull(nestable.get());
        }
    }

    @Test
    public void givenNestedTransaction_whenWhenWritingWithCommit_thenOutermostValueStored() throws RocksDBException,
            IOException {
        // Given
        try (Nestable nestable = new Nestable(this.dbDir)) {
            // When
            nestable.nested(true);

            // Then
            Assert.assertEquals(nestable.get(), "nested");
        }
    }

    @Test
    public void givenNestedTransaction_whenWhenWritingWithRollback_thenNoValueStored() throws RocksDBException,
            IOException {
        // Given
        try (Nestable nestable = new Nestable(this.dbDir)) {
            // When
            nestable.basic(false);

            // Then
            Assert.assertNull(nestable.get());
        }
    }

    @Test
    public void givenDeeplyNestedTransaction_whenWhenWritingWithCommit_thenOutermostValueStored() throws
            RocksDBException, IOException {
        // Given
        try (Nestable nestable = new Nestable(this.dbDir)) {
            // When
            nestable.deeplyNested(true);

            // Then
            Assert.assertEquals(nestable.get(), "deeplyNested");
        }
    }

    @Test
    public void givenDeeplyNestedTransaction_whenWhenWritingWithRollback_thenNoValueStored() throws RocksDBException,
            IOException {
        // Given
        try (Nestable nestable = new Nestable(this.dbDir)) {
            // When
            nestable.deeplyNested(false);

            // Then
            Assert.assertNull(nestable.get());
        }
    }

    @Test
    public void givenCombinedTransaction_whenWhenWritingWithCommit_thenAllValuesStored() throws RocksDBException,
            IOException {
        // Given
        try (Nestable nestable = new Nestable(this.dbDir)) {
            // When
            nestable.combined(true);

            // Then
            Assert.assertEquals(nestable.get(), "nested");
            Assert.assertEquals(nestable.get(Nestable.OTHER_KEY), "combined");
            List<byte[]> values = nestable.multiGet(List.of(Nestable.KEY, Nestable.OTHER_KEY));
            Assert.assertEquals(values.get(0), "nested".getBytes(StandardCharsets.UTF_8));
            Assert.assertEquals(values.get(1), "combined".getBytes(StandardCharsets.UTF_8));
        }
    }

    @Test
    public void givenCombinedTransaction_whenWhenWritingWithRollback_thenNoValueStored() throws RocksDBException,
            IOException {
        // Given
        try (Nestable nestable = new Nestable(this.dbDir)) {
            // When
            nestable.combined(false);

            // Then
            Assert.assertNull(nestable.get());
            Assert.assertNull(nestable.get(Nestable.OTHER_KEY));
            List<byte[]> values = nestable.multiGet(List.of(Nestable.KEY, Nestable.OTHER_KEY));
            Assert.assertTrue(values.stream().allMatch(Objects::isNull));
        }
    }

    @Test
    public void givenTransactionSequence_whenWritingWithCommit_thenValueReflectsMostRecentTransaction() throws
            RocksDBException, IOException {
        // Given
        try (Nestable nestable = new Nestable(this.dbDir)) {
            // When
            nestable.nested(true);
            Assert.assertEquals(nestable.get(), "nested");
            nestable.deeplyNested(true);
            Assert.assertEquals(nestable.get(), "deeplyNested");
            nestable.basic(true);

            // Then
            Assert.assertEquals(nestable.get(), "basic");
        }
    }
}
