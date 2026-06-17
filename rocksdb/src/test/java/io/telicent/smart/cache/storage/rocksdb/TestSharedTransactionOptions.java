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

import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;

/**
 * Tests that the shared (storage owned) read/write options are reused across transactions and are not closed by
 * individual transactions committing/closing.
 */
public class TestSharedTransactionOptions extends AbstractRocksDBTests {

    private static byte[] key(int i) {
        return ("key-" + i).getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] value(int i) {
        return ("value-" + i).getBytes(StandardCharsets.UTF_8);
    }

    @Test
    public void givenManyTransactions_whenReusingSharedOptions_thenAllSucceed() throws Exception {
        // Given
        try (RocksDBStorageForTest storage = new RocksDBStorageForTest(this.dbDir)) {
            // When - many independent transactions reuse the same shared read/write options.  If a committed/closed
            //        transaction had (incorrectly) closed the shared options, a subsequent transaction would fail with
            //        a native error, so simply exercising the path many times is the assertion.
            for (int i = 0; i < 250; i++) {
                storage.put(key(i), value(i));
            }

            // Then
            for (int i = 0; i < 250; i++) {
                Assert.assertEquals(storage.get(key(i)), value(i));
            }
            Assert.assertEquals(storage.count(), 250L);
        }
    }
}
