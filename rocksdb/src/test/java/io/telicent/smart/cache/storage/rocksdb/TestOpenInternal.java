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

import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDBException;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public class TestOpenInternal extends AbstractRocksDBTests {

    @Test
    public void givenStorage_whenOpened_thenAllColumnFamilyHandlesPopulated() throws RocksDBException, IOException {
        // Given
        try (ManyFamilies storage = new ManyFamilies(this.dbDir)) {
            // Then
            Map<String, ColumnFamilyHandle> handles = storage.getColumnFamilyHandles();
            assertNotNull(handles);
            assertFalse(handles.isEmpty());

            assertEquals(handles.size(), ManyFamilies.NAMES.size() + 1);
            for (Map.Entry<String, ColumnFamilyHandle> entry : handles.entrySet()) {
                assertNotNull(entry.getValue(), "Handle for CF '" + entry.getKey() + "' should not be null");
                assertTrue(entry.getValue().isOwningHandle(),
                           "Handle for CF '" + entry.getKey() + "' should be owning");
            }
        }
    }

    @Test
    public void givenStorage_whenClosedAndReopened_thenDataIsPersisted() throws RocksDBException, IOException {
        // Given
        byte[] key = "key".getBytes(StandardCharsets.UTF_8);
        byte[] value = "value".getBytes(StandardCharsets.UTF_8);
        try (ManyFamilies storage = new ManyFamilies(this.dbDir)) {
            storage.put("A", key, value);
        }

        // When
        try (ManyFamilies storage = new ManyFamilies(this.dbDir)) {
            // Then
            byte[] retrieved = storage.get("A", key);
            assertNotNull(retrieved);
            assertEquals(new String(retrieved, StandardCharsets.UTF_8), "value");
        }
    }

    @Test
    public void givenStorage_whenClosedAndReopened_thenHandlesAreNewObjects() throws RocksDBException, IOException {
        // Given
        Map<String, ColumnFamilyHandle> firstHandles;
        try (ManyFamilies storage = new ManyFamilies(this.dbDir)) {
            firstHandles = new HashMap<>(storage.getColumnFamilyHandles());
        }

        // When
        try (ManyFamilies storage = new ManyFamilies(this.dbDir)) {
            Map<String, ColumnFamilyHandle> secondHandles = storage.getColumnFamilyHandles();

            // Then
            for (String cfName : secondHandles.keySet()) {
                if (firstHandles.containsKey(cfName)) {
                    assertNotSame(firstHandles.get(cfName), secondHandles.get(cfName),
                                  "Handle for CF '" + cfName + "' should be a new object after reopen");
                }
            }
        }
        for (Map.Entry<String, ColumnFamilyHandle> entry : firstHandles.entrySet()) {
            assertFalse(entry.getValue().isOwningHandle(),
                        "Old handle for CF '" + entry.getKey() + "' should no longer be owning after close");
        }
    }

    @Test
    public void givenStorage_whenClosedAndReopened_thenHandleMapDoesNotAccumulateStaleEntries()
            throws RocksDBException, IOException {
        // Given
        int firstSize;
        try (ManyFamilies storage = new ManyFamilies(this.dbDir)) {
            firstSize = storage.getColumnFamilyHandles().size();
        }

        // When
        try (ManyFamilies storage = new ManyFamilies(this.dbDir)) {
            // Then
            assertEquals(storage.getColumnFamilyHandles().size(), firstSize,
                         "Handle map should not accumulate stale entries across reopens");
        }
    }

    //TODO
    // it prob shouldn't be FileAlreadyExistsException
    @Test(expectedExceptions = FileAlreadyExistsException.class)
    public void givenInvalidDirectory_whenOpening_thenThrows() throws RocksDBException, IOException {
        // Given
        File invalidDir = new File(this.dbDir, "not-a-directory-2");
        assertTrue(invalidDir.createNewFile());

        // When and Then
        new ManyFamilies(invalidDir);
    }
}
