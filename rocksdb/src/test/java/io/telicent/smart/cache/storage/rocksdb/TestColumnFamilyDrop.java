/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rocksdb;

import org.apache.commons.lang3.RandomUtils;
import org.rocksdb.RocksDBException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class TestColumnFamilyDrop extends AbstractRocksDBTests {

    private static final byte[] KEY = "test".getBytes(StandardCharsets.UTF_8);
    private static final byte[] VALUE = "value".getBytes(StandardCharsets.UTF_8);

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
