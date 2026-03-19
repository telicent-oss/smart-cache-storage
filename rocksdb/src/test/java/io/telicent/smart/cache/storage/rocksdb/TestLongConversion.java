/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rocksdb;

import org.apache.commons.lang3.RandomUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestLongConversion {

    @DataProvider(name = "badArrays")
    private Object[][] badArrays() {
        return new Object[][] {
                { null },
                { new byte[0] },
                { new byte[4] },
                { new byte[7] },
                { new byte[16] }
        };
    }

    @Test(dataProvider = "badArrays", expectedExceptions = IllegalArgumentException.class)
    public void givenWrongLengthByteArrays_whenConvertingToLong_thenFails(byte[] data) {
        // Given, When and Then
        AbstractRocksDBStorage.bytesToLong(data);
    }

    @Test(invocationCount = 100)
    public void givenLongValue_whenConvertingToAndFromByteArray_thenRoundTrips() {
        // Given
        long original = RandomUtils.insecure().randomLong();

        // When
        byte[] encoded = AbstractRocksDBStorage.longToBytes(original);
        long decoded = AbstractRocksDBStorage.bytesToLong(encoded);

        // Then
        Assert.assertEquals(decoded, original);
    }

    @Test(invocationCount = 100)
    public void givenNegativeLongValue_whenConvertingToAndFromByteArray_thenRoundTrips() {
        // Given
        long original = RandomUtils.insecure().randomLong() * -1;

        // When
        byte[] encoded = AbstractRocksDBStorage.longToBytes(original);
        long decoded = AbstractRocksDBStorage.bytesToLong(encoded);

        // Then
        Assert.assertEquals(decoded, original);
    }

    @Test
    public void givenExtremeLongValues_whenConvertingToAndFromByteArray_thenRoundTrips() {
        // Given and When
        byte[] minEncoded = AbstractRocksDBStorage.longToBytes(Long.MIN_VALUE);
        byte[] maxEncoded = AbstractRocksDBStorage.longToBytes(Long.MAX_VALUE);
        long minDecoded = AbstractRocksDBStorage.bytesToLong(minEncoded);
        long maxDecoded = AbstractRocksDBStorage.bytesToLong(maxEncoded);

        // Then
        Assert.assertEquals(minDecoded, Long.MIN_VALUE);
        Assert.assertEquals(maxDecoded, Long.MAX_VALUE);
    }
}
