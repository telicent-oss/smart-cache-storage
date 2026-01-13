/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate;

import io.telicent.smart.cache.storage.hibernate.model.JsonHolder;
import io.telicent.smart.cache.storage.hibernate.model.JsonStore;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Abstract test suite for verifying Hibernate backends work with Hypersistence Utils
 * {@link io.hypersistence.utils.hibernate.type.json.JsonType}'d fields
 */
public abstract class AbstractJsonStorageTests {
    /**
     * Creates a fresh new instance of the store for testing
     *
     * @return Fresh store instance
     */
    protected abstract JsonStore createJsonStore();

    @Test
    public void givenEmptyStore_whenListing_thenEmpty() {
        // Given
        try (JsonStore store = createJsonStore()) {
            // When
            List<JsonHolder> data = store.all();

            // Then
            Assert.assertTrue(data.isEmpty());
        }
    }

    @Test
    public void givenEmptyStore_whenDeleting_thenNothingDeleted() {
        // Given
        try (JsonStore store = createJsonStore()) {
            // When and Then
            Assert.assertFalse(store.delete(UUID.randomUUID().toString()));
        }
    }

    @Test
    public void givenStore_whenSaving_thenRetrievable() {
        // Given
        try (JsonStore store = createJsonStore()) {
            JsonHolder input = new JsonHolder();
            input.setJson(Map.of("foo", "bar", "flag", true, "number", 123));

            // When
            JsonHolder saved = store.save(input);
            Assert.assertNotNull(saved.getUuid());
            Assert.assertNotNull(saved.getId());

            // Then
            JsonHolder retrieved = store.load(saved.getUuid());
            Assert.assertEquals(saved, retrieved);
            Assert.assertEquals(saved.getJson(), input.getJson());
            Assert.assertEquals(retrieved.getJson(), input.getJson());
        }
    }

    @Test
    public void givenStore_whenSavingAndDeleting_thenNoLongerRetrievable() {
        // Given
        try (JsonStore store = this.createJsonStore()) {
            JsonHolder input = new JsonHolder();
            input.setJson(Map.of("test", "input"));

            // When
            JsonHolder saved = store.save(input);
            Assert.assertTrue(store.delete(saved.getUuid()));

            // Then
            Assert.assertNull(store.load(saved.getUuid()));
        }
    }

    @Test
    public void givenStore_whenSavingUpdatedData_thenRetrievedDataReflectsMostRecentSave() {
        // Given
        try (JsonStore store = createJsonStore()) {
            JsonHolder input = new JsonHolder();
            input.setJson(Map.of("test", 1));
            JsonHolder saved = store.save(input);

            // When
            JsonHolder update = new JsonHolder();
            update.setUuid(saved.getUuid());
            update.setJson(Map.of("test", 2));
            store.save(update);

            // Then
            JsonHolder retrieved = store.load(saved.getUuid());
            Assert.assertEquals(retrieved.getJson().get("test"), 2);
        }
    }
}
