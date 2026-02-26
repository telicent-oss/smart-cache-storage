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
package io.telicent.smart.cache.storage.labels;

import org.apache.commons.lang3.Strings;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;

public class TestDigestHelper {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void givenNullAlgorithm_whenConstructing_thenIllegalArgument() {
        // Given, When and Then
        new DigestHelper(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void givenEmptyAlgorithm_whenConstructing_thenIllegalArgument() {
        // Given, When and Then
        new DigestHelper("");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void givenBlankAlgorithm_whenConstructing_thenIllegalArgument() {
        // Given, When and Then
        new DigestHelper("   ");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void givenUnsupportedAlgorithm_whenConstructing_thenIllegalArgument() {
        // Given, When and Then
        new DigestHelper("foo");
    }

    @DataProvider(name = "algorithms")
    private Object[][] algorithms() {
        // Set of supported MessageDigest algorithms as specified in Java 17 Language Specs
        // https://docs.oracle.com/en/java/javase/17/docs/specs/security/standard-names.html#messagedigest-algorithms
        return new Object[][] {
                { "SHA1", 20 },
                { "SHA224", 28 },
                { "SHA256", 32 },
                { "SHA384", 48 },
                { "SHA512", 64 },
                { "SHA512/224", 28 },
                { "SHA512/256", 32 },
                { "SHA3-224", 28 },
                { "SHA3-256", 32 },
                { "SHA3-384", 48 },
                { "SHA3-512", 64 },
                { "MD2", 16 },
                { "MD5", 16 }
        };
    }

    @Test(dataProvider = "algorithms")
    public void givenKnownAlgorithm_whenConstructing_thenUsable(String name, int digestLength) {
        // Given and When
        DigestHelper helper = new DigestHelper(name);

        // Then
        byte[] digest = helper.digest("test".getBytes(StandardCharsets.UTF_8));
        Assert.assertNotNull(digest);
        Assert.assertEquals(digest.length, digestLength);
        Assert.assertTrue(Strings.CS.contains(helper.toString(), name));
    }
}
