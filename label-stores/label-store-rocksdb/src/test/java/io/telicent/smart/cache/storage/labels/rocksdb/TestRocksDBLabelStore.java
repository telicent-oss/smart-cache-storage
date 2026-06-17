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
package io.telicent.smart.cache.storage.labels.rocksdb;

import io.telicent.smart.cache.storage.labels.AbstractLabelStoreTests;
import io.telicent.smart.cache.storage.labels.LabelsStore;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestRocksDBLabelStore extends AbstractLabelStoreTests {

    private static final byte[] KEY = "rocks-key".getBytes(StandardCharsets.UTF_8);

    private File rocksDir;

    @Override
    protected LabelsStore newStore() {
        try {
            return new RocksDbLabelsStore(rocksDir.getAbsoluteFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void givenAssignedLabel_whenGettingLabelBytesViaCollapsedReadPath_thenLabelReturned() {
        // Given
        byte[] label = { 9, 8, 7 };
        try (LabelsStore store = newStore()) {
            long labelId = store.idForLabel(label);
            store.setLabel(KEY, labelId);

            // When (getLabelAsBytes resolves key -> id -> label in a single read-only transaction)
            byte[] retrieved = store.getLabelAsBytes(KEY);

            // Then
            Assert.assertEquals(retrieved, label);
        }
    }

    @Test
    public void givenNonExistentKey_whenGettingLabelBytes_thenNull() {
        // Given
        try (LabelsStore store = newStore()) {
            // When and Then
            Assert.assertNull(store.getLabelAsBytes(KEY));
        }
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void givenNullKey_whenGettingLabelBytes_thenNPE() {
        // Given
        try (LabelsStore store = newStore()) {
            // When and Then
            store.getLabelAsBytes(null);
        }
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void givenEmptyKey_whenGettingLabelBytes_thenNPE() {
        // Given
        try (LabelsStore store = newStore()) {
            // When and Then
            store.getLabelAsBytes(new byte[0]);
        }
    }

    // ---- Bulk and removal API (not exercised by the single-value AbstractLabelStoreTests suite) ----

    private RocksDbLabelsStore newRocksStore() {
        try {
            return new RocksDbLabelsStore(rocksDir.getAbsoluteFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] label(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    @Test
    public void givenLabels_whenBulkResolvingIds_thenNewIdsAllocatedAndExistingReused() {
        // Given
        byte[] a = label("a");
        byte[] b = label("b");
        byte[] c = label("c");
        try (RocksDbLabelsStore store = newRocksStore()) {
            // When - first call allocates new ids for both
            Map<byte[], Long> first = store.idsForLabels(List.of(a, b));

            // Then
            Assert.assertEquals(first.size(), 2);
            Assert.assertNotNull(first.get(a));
            Assert.assertNotNull(first.get(b));

            // When - second call mixes an existing label (a) with a new one (c)
            Map<byte[], Long> second = store.idsForLabels(List.of(a, c));

            // Then - existing id is reused, new id allocated
            Assert.assertEquals(second.get(a), first.get(a));
            Assert.assertNotNull(second.get(c));
            Assert.assertNotEquals(second.get(c), first.get(a));

            // When - third call is entirely existing labels (no new allocation / no commit)
            Map<byte[], Long> third = store.idsForLabels(List.of(a, b));

            // Then
            Assert.assertEquals(third.get(a), first.get(a));
            Assert.assertEquals(third.get(b), first.get(b));
        }
    }

    @Test
    public void givenEmptyOrAllInvalidLabels_whenBulkResolvingIds_thenEmptyMap() {
        // Given
        try (RocksDbLabelsStore store = newRocksStore()) {
            // When and Then - empty input short circuits
            Assert.assertTrue(store.idsForLabels(Collections.emptyList()).isEmpty());

            // And - input containing only invalid (null/empty) labels is skipped, yielding an empty result
            Assert.assertTrue(store.idsForLabels(Arrays.asList(null, new byte[0])).isEmpty());
        }
    }

    @Test
    public void givenIds_whenBulkResolvingLabels_thenKnownLabelsReturnedAndUnknownsOmitted() {
        // Given
        byte[] a = label("alpha");
        byte[] b = label("beta");
        try (RocksDbLabelsStore store = newRocksStore()) {
            Map<byte[], Long> ids = store.idsForLabels(List.of(a, b));
            long idA = ids.get(a);
            long idB = ids.get(b);

            // When - resolve known ids plus an unknown one
            Map<Long, byte[]> labels = store.labelsForIds(List.of(idA, idB, 999_999L));

            // Then - known ids resolve to their labels, unknown id is omitted
            Assert.assertEquals(labels.get(idA), a);
            Assert.assertEquals(labels.get(idB), b);
            Assert.assertFalse(labels.containsKey(999_999L));
        }
    }

    @Test
    public void givenNullEmptyOrInvalidIds_whenBulkResolvingLabels_thenEmptyMap() {
        // Given
        try (RocksDbLabelsStore store = newRocksStore()) {
            // When and Then
            Assert.assertTrue(store.labelsForIds(null).isEmpty());
            Assert.assertTrue(store.labelsForIds(Collections.emptyList()).isEmpty());

            // And - null, zero and negative ids are all filtered out
            Assert.assertTrue(store.labelsForIds(Arrays.asList(null, 0L, -5L)).isEmpty());
        }
    }

    @Test
    public void givenAssignedKey_whenRemoved_thenNoLongerRetrievable() {
        // Given
        try (RocksDbLabelsStore store = newRocksStore()) {
            long id = store.idForLabel(label("public"));
            store.setLabel(KEY, id);
            Assert.assertEquals(store.getLabel(KEY), (Long) id);

            // When
            store.removeLabel(KEY);

            // Then
            Assert.assertNull(store.getLabel(KEY));
        }
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void givenNullKey_whenRemoving_thenNPE() {
        try (RocksDbLabelsStore store = newRocksStore()) {
            store.removeLabel(null);
        }
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void givenEmptyKey_whenRemoving_thenNPE() {
        try (RocksDbLabelsStore store = newRocksStore()) {
            store.removeLabel(new byte[0]);
        }
    }

    @BeforeMethod
    public void setup() throws IOException {
        rocksDir = Files.createTempDirectory("rocks").toFile();
    }

    @AfterMethod
    public void cleanUp() throws IOException {
        // Walk and delete directory tree properly
        FileUtils.deleteDirectory(rocksDir);
    }
}
