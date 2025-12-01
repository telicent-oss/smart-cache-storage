/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels;

import org.apache.commons.lang3.RandomUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.*;

/**
 * Abstract test suite for label dictionary stores
 */
public abstract class AbstractDictionaryLabelStoreTests {

    /**
     * Creates a new fresh empty instance of a label dictionary store for testing
     *
     * @return New fresh label dictionary store
     */
    protected abstract DictionaryLabelsStore newDictionaryStore();

    @Test(expectedExceptions = { IllegalArgumentException.class, NullPointerException.class })
    public void givenDictionaryLabelsStore_whenInsertingNullLabels_thenNPE() {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            // When and Then
            store.idForLabel(null);
        }
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void givenDictionaryLabelsStore_whenClosing_thenIdForLabelThrowsIllegalState() {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            // When
            store.close();

            // Then
            store.idForLabel(new byte[4]);
        }
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void givenDictionaryLabelsStore_whenClosing_thenLabelForIdThrowsIllegalState() {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            // When
            store.close();

            // Then
            store.labelForId(0);
        }
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void givenDictionaryLabelsStore_whenClosing_thenIdsForLabelsThrowsIllegalState() {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            // When
            store.close();

            // Then
            store.idsForLabels(List.of(new byte[4]));
        }
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void givenDictionaryLabelsStore_whenClosing_thenLabelsForIdsThrowsIllegalState() {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            // When
            store.close();

            // Then
            store.labelsForIds(List.of(0L));
        }
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void givenDictionaryLabelsStore_whenClosing_thenLabelSizeThrowsIllegalState() {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            // When
            store.close();

            // Then
            store.labelSize();
        }
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void givenDictionaryLabelStore_whenInsertingNullLabel_thenIllegalArgument() {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            // When and Then
            store.idForLabel(null);
        }
    }

    @DataProvider(name = "nonExistentIds")
    public static Object[][] nonExistentIds() {
        return new Object[][] {
                { 0 }, { 77777 }, { (long) (Math.pow(2, 32)) }, { Long.MAX_VALUE }, { Long.MIN_VALUE }
        };
    }

    @Test(dataProvider = "nonExistentIds", dataProviderClass = AbstractDictionaryLabelStoreTests.class)
    public void givenDictionaryLabelsStore_whenQueryingLabelsForNonExistentIds_thenNullReturned(long badId) {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            // When
            byte[] label = store.labelForId(badId);

            // Then
            if (label != null) {
                System.out.println(badId);
            }
            Assert.assertNull(label, "Label for id " + badId + " was not null");
        }
    }

    /**
     * Generates a collection of unique labels
     *
     * @param total Number of labels to generate
     * @return Collection of unique labels
     */
    protected final Collection<byte[]> generateUniqueLabels(int total) {
        Set<byte[]> labels = new LinkedHashSet<>();
        while (labels.size() < total) {
            labels.add(RandomUtils.insecure().randomBytes(50));
        }
        return labels;
    }

    /**
     * Generates a collection of non-unique labels
     *
     * @param total        Total number of labels to generate
     * @param uniqueLabels Number of unique labels to use
     * @return List of non-unique labels
     */
    protected final List<byte[]> generateRepeatingLabels(int total, int uniqueLabels) {
        if (uniqueLabels >= total) {
            throw new IllegalArgumentException(
                    "Number of uniqueLabels MUST be less than total number of labels otherwise there will be no repeated labels");
        }

        List<byte[]> labels = new ArrayList<>();
        List<byte[]> possible = generateUniqueLabels(uniqueLabels).stream().toList();
        RandomUtils rnd = RandomUtils.insecure();
        for (int i = 0; i < total; i++) {
            labels.add(possible.get(rnd.randomInt(0, possible.size())));
        }
        return labels;
    }

    @DataProvider(name = "uniqueSizes")
    protected static Object[][] uniqueSizes() {
        return new Object[][] {
                { 100 }, { 1_000 }, { 10_000 }
        };
    }

    @Test(dataProvider = "uniqueSizes", dataProviderClass = AbstractDictionaryLabelStoreTests.class)
    public void givenDictionaryLabelStore_whenInsertingManyUniqueLabels_thenAllUniqueIdsReturned_andAllIdsResolveToOriginalLabel(
            int uniqueLabels) {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            Collection<byte[]> labels = generateUniqueLabels(uniqueLabels);
            List<Long> ids = new ArrayList<>();

            // When
            insertLabelsAndTrackAssignedIds(labels, ids, store);

            // Then
            Assert.assertEquals(ids.size(), labels.size());
            Assert.assertEquals(ids.stream().distinct().count(), labels.size());
            Assert.assertEquals(ids.stream().distinct().count(), uniqueLabels);

            // And
            int i = 0;
            for (byte[] label : labels) {
                long expectedId = ids.get(i++);
                Assert.assertEquals(store.idForLabel(label), expectedId);
                Assert.assertEquals(store.labelForId(expectedId), label);
            }
            Assert.assertEquals(store.labelSize(), uniqueLabels);
        }
    }

    protected static void insertLabelsAndTrackAssignedIds(Collection<byte[]> labels, Collection<Long> ids,
                                                          DictionaryLabelsStore store) {
        for (byte[] label : labels) {
            ids.add(store.idForLabel(label));
        }
    }

    @Test(dataProvider = "uniqueSizes", dataProviderClass = AbstractDictionaryLabelStoreTests.class)
    public void givenDictionaryLabelStore_whenBulkInsertingManyUniqueLabels_thenAllUniqueIdsReturned_andBulkIdsResolveToOriginalLabel(
            int uniqueLabels) {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            List<byte[]> labels = new ArrayList<>(generateUniqueLabels(uniqueLabels));

            // When
            Map<byte[], Long> ids = store.idsForLabels(labels);

            // Then
            Assert.assertEquals(ids.size(), labels.size());
            Assert.assertEquals(ids.values().stream().distinct().count(), labels.size());
            Assert.assertEquals(ids.values().stream().distinct().count(), uniqueLabels);

            // And
            Map<Long, byte[]> retrieved = store.labelsForIds(ids.values().stream().toList());
            for (Map.Entry<byte[], Long> entry : ids.entrySet()) {
                Assert.assertNotNull(retrieved.get(entry.getValue()));
                Assert.assertEquals(retrieved.get(entry.getValue()), entry.getKey());
            }
            Assert.assertEquals(store.labelSize(), uniqueLabels);
        }
    }

    @DataProvider(name = "repeatedSizes")
    protected static Object[][] repeatedSizes() {
        return new Object[][] {
                { 1_000, 1 }, { 10_000, 100 }, { 10_000, 1_000 }, { 25_000, 10 }
        };
    }

    @Test(dataProvider = "repeatedSizes", dataProviderClass = AbstractDictionaryLabelStoreTests.class)
    public void givenDictionaryLabelStore_whenInsertingManyNonUniqueLabels_thenIdsReusedAppropriately(int total,
                                                                                                      int uniqueLabels) {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            List<byte[]> labels = generateRepeatingLabels(total, uniqueLabels);
            Set<Long> ids = new HashSet<>();

            // When
            insertLabelsAndTrackAssignedIds(labels, ids, store);

            // Then
            // NB - Randomness means not all unique labels might have been in generated labels
            verifyUniqueLabelCounts(ids.size(), uniqueLabels, store);
        }
    }

    @Test(dataProvider = "repeatedSizes", dataProviderClass = AbstractDictionaryLabelStoreTests.class)
    public void givenDictionaryLabelStore_whenBulkInsertingManyNonUniqueLabels_thenIdsReusedAppropriately(int total,
                                                                                                          int uniqueLabels) {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            List<byte[]> labels = generateRepeatingLabels(total, uniqueLabels);

            // When
            Map<byte[], Long> ids = store.idsForLabels(labels);

            // Then
            // NB - Randomness means not all unique labels might have been in generated labels
            verifyUniqueLabelCounts(ids.size(), uniqueLabels, store);
        }
    }

    @Test(dataProvider = "repeatedSizes", dataProviderClass = AbstractDictionaryLabelStoreTests.class)
    public void givenDictionaryLabelStore_whenBulkInsertingManyNonUniqueLabelsTwice_thenIdsReusedAppropriately(
            int total,
            int uniqueLabels) {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            List<byte[]> labels = generateRepeatingLabels(total, uniqueLabels);

            // When
            Map<byte[], Long> ids = store.idsForLabels(labels);
            Map<byte[], Long> ids2 = store.idsForLabels(labels);

            // Then
            // NB - Randomness means not all unique labels might have been in generated labels
            verifyUniqueLabelCounts(ids.size(), uniqueLabels, store);
            Assert.assertEquals(ids2, ids);
        }
    }

    private static void verifyUniqueLabelCounts(int uniqueAssignedIds, int uniqueLabels, DictionaryLabelsStore store) {
        Assert.assertTrue(uniqueAssignedIds <= uniqueLabels,
                          "Expected at most " + uniqueLabels + " IDs but found " + uniqueAssignedIds);
        long storeLabelsCount = store.labelSize();
        Assert.assertTrue(storeLabelsCount <= uniqueLabels,
                          "Store reported label size " + storeLabelsCount + " greater than expected unique labels " + uniqueLabels);
    }

    @Test(dataProvider = "repeatedSizes", dataProviderClass = AbstractDictionaryLabelStoreTests.class)
    public void givenDictionaryLabelStore_whenInsertingManyNonUniqueLabelsAcrossMultipleThreads_thenIdsReusedAppropriately(
            int total, int uniqueLabels) throws InterruptedException {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            List<byte[]> labels = generateRepeatingLabels(total, uniqueLabels);
            Set<Long> ids = ConcurrentHashMap.newKeySet();
            ExecutorService executor = Executors.newFixedThreadPool(4);
            Semaphore semaphore = new Semaphore(0);

            // When
            for (int i = 0; i < 4; i++) {
                final List<byte[]> threadLabels = new ArrayList<>(labels.size());
                Collections.shuffle(labels);
                executor.submit(() -> {
                    try {
                        semaphore.acquire(1);

                        insertLabelsAndTrackAssignedIds(threadLabels, ids, store);

                        semaphore.release(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            semaphore.release(4);
            executor.shutdown();
            Assert.assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
            Assert.assertEquals(semaphore.availablePermits(), 4);

            // Then
            // NB - Randomness means not all unique labels might have been in generated labels
            verifyUniqueLabelCounts(ids.size(), uniqueLabels, store);
        }
    }

    @Test
    public void givenDictionaryLabelStore_whenBulkInsertingLabelsWithSomeNulls_thenNullLabelsIgnored() {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            List<byte[]> labels = new ArrayList<>(generateUniqueLabels(100));
            insertNulls(labels, 5);

            // When
            Map<byte[], Long> ids = store.idsForLabels(labels);

            // Then
            Assert.assertEquals(ids.size(), 100);
            Assert.assertEquals(store.labelSize(), 100);
        }
    }

    private static void insertNulls(List<?> labels, int total) {
        for (int i = 1; i <= total; i++) {
            labels.add(RandomUtils.insecure().randomInt(0, labels.size()), null);
        }
    }

    @Test
    public void givenDictionaryLabelStore_whenBulkLookupIdsWithSomeNulls_thenNullIdsIgnored() {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            List<byte[]> labels = new ArrayList<>(generateUniqueLabels(100));
            Map<byte[], Long> ids = store.idsForLabels(labels);

            // When
            List<Long> lookupIds = new ArrayList<>(ids.values());
            insertNulls(lookupIds, 15);
            Map<Long, byte[]> retrieved = store.labelsForIds(lookupIds);

            // Then
            Assert.assertEquals(retrieved.size(), 100);
            for (byte[] label : retrieved.values()) {
                Assert.assertNotNull(label);
            }
        }
    }

    @Test
    public void givenDictionaryLabelStore_whenBulkInsertingAllNulls_thenNothingInserted() {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            List<byte[]> labels = new ArrayList<>();
            insertNulls(labels, 50);

            // When
            Map<byte[], Long> ids = store.idsForLabels(labels);

            // Then
            Assert.assertTrue(ids.isEmpty());
            Assert.assertEquals(store.labelSize(), 0L);
        }
    }

    @Test
    public void givenDictionaryLabelStore_whenBulkInsertingNullList_thenNothingInserted() {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            // When
            Map<byte[], Long> ids = store.idsForLabels(null);

            // Then
            Assert.assertTrue(ids.isEmpty());
            Assert.assertEquals(store.labelSize(), 0L);
        }
    }

    @Test
    public void givenDictionaryLabelStore_whenBulkInsertingEmptyList_thenNothingInserted() {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            // When
            Map<byte[], Long> ids = store.idsForLabels(Collections.emptyList());

            // Then
            Assert.assertTrue(ids.isEmpty());
            Assert.assertEquals(store.labelSize(), 0L);
        }
    }

    @Test
    public void givenDictionaryLabelStore_whenBulkLookupAllNulls_thenNothingReturned() {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            List<Long> ids = new ArrayList<>();
            insertNulls(ids, 50);

            // When
            Map<Long, byte[]> labels = store.labelsForIds(ids);

            // Then
            Assert.assertTrue(labels.isEmpty());
        }
    }

    @Test
    public void givenDictionaryLabelStore_whenBulkLookupNullList_thenNothingReturned() {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            // When
            Map<Long, byte[]> labels = store.labelsForIds(null);

            // Then
            Assert.assertTrue(labels.isEmpty());
        }
    }

    @Test
    public void givenDictionaryLabelStore_whenBulkLookupEmptyList_thenNothingReturned() {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            // When
            Map<Long, byte[]> labels = store.labelsForIds(Collections.emptyList());

            // Then
            Assert.assertTrue(labels.isEmpty());
        }
    }

    @Test
    public void givenDictionaryLabelStore_whenBulkLookupMixOfValidAndInvalidIds_thenOnlyValidIdsReturnNonNull() {
        // Given
        try (DictionaryLabelsStore store = newDictionaryStore()) {
            List<byte[]> labels = new ArrayList<>(generateUniqueLabels(5));
            Map<byte[], Long> ids = store.idsForLabels(labels);

            // When
            List<Long> lookups = new ArrayList<>(ids.values());
            insertNulls(lookups, 1);
            lookups.add(Long.MIN_VALUE);
            lookups.add(Long.MAX_VALUE);
            Map<Long, byte[]> retrieved = store.labelsForIds(lookups);

            // Then
            for (Long knownId : ids.values()) {
                Assert.assertNotNull(retrieved.get(knownId));
            }
            Assert.assertNull(retrieved.get(Long.MAX_VALUE));
        }
    }
}
