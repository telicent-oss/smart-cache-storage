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
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.testng.Assert.*;

public class TestOpenInternal extends AbstractRocksDBTests {

    @Test
    public void givenStorage_whenClosedAndReopenedInternally_thenDataIsPersisted() throws RocksDBException, IOException {
        // Given
        byte[] key = "key".getBytes(StandardCharsets.UTF_8);
        byte[] value = "value".getBytes(StandardCharsets.UTF_8);
        try (ManyFamilies storage = new ManyFamilies(this.dbDir)) {
            storage.put("A", key, value);
            // When
            storage.closeInternal();
            storage.openInternal();
            // Then
            byte[] retrieved = storage.get("A", key);
            assertNotNull(retrieved);
            assertEquals(new String(retrieved, StandardCharsets.UTF_8), "value");
        }
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void givenCorruptedDatabase_whenOpeningInternally_thenThrowsRuntimeException_2()
            throws RocksDBException, IOException {
        // Given
        ManyFamilies storage = new ManyFamilies(this.dbDir);
        storage.put("A", "key".getBytes(StandardCharsets.UTF_8), "value".getBytes(StandardCharsets.UTF_8));
        storage.closeInternal();

        File[] manifestFiles = this.dbDir.listFiles((dir, name) -> name.startsWith("MANIFEST"));
        assertNotNull(manifestFiles);
        assertTrue(manifestFiles.length > 0);
        try (FileOutputStream fos = new FileOutputStream(manifestFiles[0])) {
            fos.write("corrupted".getBytes(StandardCharsets.UTF_8));
        }

        // When and Then
        // throws the RuntimeException
        storage.openInternal();
    }
}
