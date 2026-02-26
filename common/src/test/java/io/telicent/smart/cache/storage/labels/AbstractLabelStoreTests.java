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

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

/**
 * Abstract test suite for label stores
 */
public abstract class AbstractLabelStoreTests extends AbstractDictionaryLabelStoreTests {

    public static final byte[] TEST_KEY = { 1 };

    @Override
    protected final DictionaryLabelsStore newDictionaryStore() {
        return this.newStore();
    }

    /**
     * Creates a new fresh empty instance of a label store for testing
     *
     * @return New fresh label store
     */
    protected abstract LabelsStore newStore();

    @Test(expectedExceptions = NullPointerException.class)
    public void givenLabelStore_whenAssigningEmptyKey_thenNPE() {
        // Given
        byte[] key = new byte[0];
        try (LabelsStore store = newStore()) {
            // When and Then
            store.setLabel(key, 1);
        }
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void givenLabelStore_whenAssigningNullKey_thenNPE() {
        // Given
        try (LabelsStore store = newStore()) {
            // When and Then
            store.setLabel(null, 1);
        }
    }

    @Test
    public void givenLabelStore_whenRetrievingLabelForNonExistentKey_thenNull() {
        // Given
        try (LabelsStore store = newStore()) {
            // When and Then
            Assert.assertNull(store.getLabel(TEST_KEY));
        }
    }

    @Test
    public void givenLabelStore_whenRetrievingLabelBytesForNonExistentKey_thenNull() {
        // Given
        try (LabelsStore store = newStore()) {
            // When and Then
            Assert.assertNull(store.getLabelAsBytes(TEST_KEY));
        }
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void givenLabelStore_whenRetrievingLabelBytesForNullKey_thenNull() {
        // Given
        try (LabelsStore store = newStore()) {
            // When and Then
            store.getLabel(null);
        }
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void givenLabelStore_whenRetrievingLabelBytesForEmptyKey_thenNPE() {
        // Given
        try (LabelsStore store = newStore()) {
            // When and Then
            store.getLabel(new byte[0]);
        }
    }

    @Test
    public void givenLabelStore_whenAssigningKeyToLabel_thenAssigned() {
        // Given
        byte[] key = TEST_KEY;
        byte[] label = new byte[] { 2, 3, 4};
        try (LabelsStore store = newStore()) {
            // When
            long labelId = store.idForLabel(label);
            store.setLabel(key, labelId);

            // Then
            Assert.assertEquals(store.getLabel(key), labelId);
            Assert.assertEquals(store.getLabelAsBytes(key), label);
        }
    }

    @Test
    public void givenLabelStore_whenAssigningSameKeyToDifferentLabels_thenLastAssignmentReturned() {
        // Given
        byte[] key = TEST_KEY;
        List<byte[]> labels = generateUniqueLabels(10).stream().toList();
        try (LabelsStore store = newStore()) {
            // When
            List<Long> labelIds = new ArrayList<>();
            insertLabelsAndTrackAssignedIds(labels, labelIds, store);
            for (Long labelId : labelIds) {
                store.setLabel(key, labelId);
            }

            // Then
            Assert.assertEquals(store.getLabel(key), labelIds.get(labels.size() - 1));
            Assert.assertEquals(store.getLabelAsBytes(key), labels.get(labels.size() - 1));
        }
    }

    private List<byte[]> generateUniqueKeys(int total) {
        List<byte[]> keys = new ArrayList<>();
        for (int i = 1; i <= total; i++) {
            keys.add(Integer.toString(i).getBytes());
        }
        return keys;
    }

    @Test
    public void givenLabelStore_whenAssigningManyKeysToLabels_thenAllAssignmentsRetrievable() {
        // Given
        List<byte[]> keys = generateUniqueKeys(50);
        List<byte[]> labels = generateUniqueLabels(50).stream().toList();
        try (LabelsStore store = newStore()) {
            // When
            List<Long> labelIds = new ArrayList<>();
            insertLabelsAndTrackAssignedIds(labels, labelIds, store);
            for (int i = 0; i < keys.size(); i++) {
                store.setLabel(keys.get(i), labelIds.get(i));
            }

            // Then
            verifyAssignedLabels(keys, store, labelIds, labels);
        }
    }

    private static void verifyAssignedLabels(List<byte[]> keys, LabelsStore store, List<Long> labelIds, List<byte[]> labels) {
        for (int i = 0; i < keys.size(); i++) {
            Assert.assertEquals(store.getLabel(keys.get(i)), labelIds.get(i));
            Assert.assertEquals(store.getLabelAsBytes(keys.get(i)), labels.get(i));
        }
    }

    @Test
    public void givenLabelStore_whenBulkAssigningNullMap_thenNoOp() {
        // Given
        try (LabelsStore store = newStore()) {
            // When
            store.setLabels(null);

            // Then
            Assert.assertEquals(store.keyCount(), 0L);
        }
    }

    @Test
    public void givenLabelStore_whenBulkAssigningEmptyMap_thenNoOp() {
        // Given
        try (LabelsStore store = newStore()) {
            // When
            store.setLabels(Collections.emptyMap());

            // Then
            Assert.assertEquals(store.keyCount(), 0L);
        }
    }

    @Test
    public void givenLabelStore_whenBulkAssigningMapWithInvalidAssignments_thenNoOp() {
        // Given
        Map<byte[], Long> assignments = new HashMap<>();
        assignments.put(null, 1L);
        assignments.put(new byte[0], 1L);
        assignments.put(TEST_KEY, null);
        try (LabelsStore store = newStore()) {
            // When
            store.setLabels(assignments);

            // Then
            Assert.assertEquals(store.keyCount(), 0L);
        }
    }

    @Test
    public void givenLabelStore_whenBulkAssigningManyKeysToLabels_thenAllAssignmentsRetrievable() {
        // Given
        List<byte[]> keys = generateUniqueKeys(50);
        List<byte[]> labels = generateUniqueLabels(50).stream().toList();
        try (LabelsStore store = newStore()) {
            // When
            List<Long> labelIds = new ArrayList<>();
            insertLabelsAndTrackAssignedIds(labels, labelIds, store);
            Map<byte[], Long> bulkLabels = new LinkedHashMap<>();
            for (int i = 0; i < keys.size(); i++) {
                bulkLabels.put(keys.get(i), labelIds.get(i));
            }
            store.setLabels(bulkLabels);

            // Then
            verifyAssignedLabels(keys, store, labelIds, labels);
        }
    }
}
