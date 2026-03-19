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

import org.apache.commons.lang3.RandomUtils;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class TestColumnFamilyDrop extends AbstractRocksDBTests {

    private static final byte[] KEY = "test".getBytes(StandardCharsets.UTF_8);
    private static final byte[] VALUE = "value".getBytes(StandardCharsets.UTF_8);

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void givenStorage_whenDroppingDefault_thenIllegalArgument() throws RocksDBException, IOException {
        // Given
        try (ManyFamilies manyFamilies = new ManyFamilies(this.dbDir)) {
            // When and Then
            manyFamilies.drop(new String(RocksDB.DEFAULT_COLUMN_FAMILY, StandardCharsets.UTF_8));
        }
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void givenStorage_whenDroppingNonExistent_thenNPE() throws RocksDBException, IOException {
        // Given
        try (External external = new External(this.dbDir)) {
            // When and Then
            external.dropColumnFamily(external.getHandle("no-such-family".getBytes(StandardCharsets.UTF_8)));
        }
    }

    @Test
    public void givenManyFamilies_whenAddingDataAndDroppingFamilies_thenDataNoLongerAvailable_andRemainsUnavailableAfterReopen() throws RocksDBException,
            IOException {
        // Given
        String toDrop;
        try (ManyFamilies manyFamilies = new ManyFamilies(this.dbDir)) {
            // When
            populateTestData(manyFamilies);
            toDrop = ManyFamilies.NAMES.get(RandomUtils.insecure().randomInt(0, ManyFamilies.NAMES.size()));
            manyFamilies.drop(toDrop);

            // Then
            Assert.assertThrows(IllegalArgumentException.class, () -> manyFamilies.get(toDrop, KEY));
        }

        // And
        try (ManyFamilies manyFamilies = new ManyFamilies(this.dbDir)) {
            verifyTestData(toDrop, manyFamilies);
        }
    }

    private static void verifyTestData(String toDrop, ManyFamilies manyFamilies) throws RocksDBException {
        for (String name : ManyFamilies.NAMES) {
            if (Objects.equals(name, toDrop)) {
                Assert.assertNull(manyFamilies.get(name, KEY));
            } else {
                Assert.assertEquals(manyFamilies.get(name, KEY), VALUE);
            }
        }
    }

    private static void populateTestData(ManyFamilies manyFamilies) throws RocksDBException {
        for (String name : ManyFamilies.NAMES) {
            manyFamilies.put(name, KEY, VALUE);
            Assert.assertEquals(manyFamilies.get(name, KEY), VALUE);
        }
    }

    @Test
    public void givenManyFamilies_whenAddingDataAndClosingBeforeDroppingFamilies_thenDataNoLongerAvailable() throws RocksDBException,
            IOException {
        // Given
        String toDrop;
        try (ManyFamilies manyFamilies = new ManyFamilies(this.dbDir)) {
            // When
            populateTestData(manyFamilies);
        }

        // Then
        try (ManyFamilies manyFamilies = new ManyFamilies(this.dbDir)) {
            toDrop = ManyFamilies.NAMES.get(RandomUtils.insecure().randomInt(0, ManyFamilies.NAMES.size()));
            manyFamilies.drop(toDrop);
            Assert.assertThrows(IllegalArgumentException.class, () -> manyFamilies.get(toDrop, KEY));
        }
    }

    @Test
    public void givenManyFamilies_whenAddingDataAndDroppingAllFamilies_thenDataNoLongerAvailable() throws RocksDBException,
            IOException {
        // Given
        try (ManyFamilies manyFamilies = new ManyFamilies(this.dbDir)) {
            // When
            populateTestData(manyFamilies);
            for (String name : ManyFamilies.NAMES) {
                manyFamilies.drop(name);
            }

            // Then
            for (String name : ManyFamilies.NAMES) {
                Assert.assertThrows(IllegalArgumentException.class, () -> manyFamilies.get(name, KEY));
            }
        }

        try (ManyFamilies manyFamilies = new ManyFamilies(this.dbDir)) {
            for (String name : ManyFamilies.NAMES) {
                Assert.assertNull(manyFamilies.get(name, KEY));
            }
        }
    }
}
