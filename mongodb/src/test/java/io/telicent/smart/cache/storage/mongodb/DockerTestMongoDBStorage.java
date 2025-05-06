/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.mongodb;

import com.mongodb.client.MongoClient;
import io.telicent.smart.cache.storage.mongodb.cluster.MongoTestCluster;
import io.telicent.smart.cache.storage.mongodb.model.SavedData;
import io.telicent.smart.cache.storage.mongodb.model.User;
import io.telicent.smart.cache.storage.mongodb.model.UserDataStore;
import org.jetbrains.annotations.NotNull;
import org.mongojack.JacksonMongoCollection;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.*;

public class DockerTestMongoDBStorage extends AbstractMongoDBTests {

    @BeforeMethod
    public void resetCollection() {
        if (this.mongo != null) {
            if (this.mongo.isRunning()) {
                try (MongoClient client = this.mongo.createMongoClient()) {
                    MongoTestCluster.resetCollection(client, UserDataStore.USERS_COLLECTION);
                    MongoTestCluster.resetCollection(client, UserDataStore.DATA_COLLECTION);
                }
            }
        }
    }

    @Test
    public void givenEmptyStorage_whenListingUsers_thenNoUsers() {
        // Given
        try (MongoClient client = this.mongo.createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When
                List<User> users = store.listUsers();

                // Then
                Assert.assertTrue(users.isEmpty());
            }
        }
    }

    @Test
    public void givenEmptyStorage_whenDeletingNonExistentSavedData_thenNothingDeleted() {
        // Given
        try (MongoClient client = this.mongo.createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When
                boolean deleted = store.deleteSavedDataById(UUID.randomUUID().toString());

                // Then
                Assert.assertFalse(deleted);
            }
        }
    }

    @Test
    public void givenEmptyStorage_whenDeletingNonExistentSavedDataForUser_thenNothingDeleted() {
        // Given
        try (MongoClient client = this.mongo.createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When
                boolean deleted = store.deleteSavedData(User.builder().name("test").build());

                // Then
                Assert.assertFalse(deleted);
            }
        }
    }

    @Test(groups = "basic", expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".* no name provided")
    public void givenEmptyStorage_whenDroppingIndexWithNoName_thenIllegalArgument() {
        // Given
        try (MongoClient client = this.mongo.createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When and Then
                JacksonMongoCollection<User> users = store.getUsers();
                store.dropIndex(users, null);
            }
        }
    }

    @Test(groups = "basic", expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "No index.*")
    public void givenEmptyStorage_whenDroppingNonExistentIndex_thenIllegalArgument() {
        // Given
        try (MongoClient client = this.mongo.createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When and Then
                JacksonMongoCollection<User> users = store.getUsers();
                store.dropIndex(users, "noSuchIndex");
            }
        }
    }

    @Test
    public void givenEmptyStorage_whenDroppingIndexExistingIndex_thenSuccess() {
        // Given
        try (MongoClient client = this.mongo.createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When
                JacksonMongoCollection<User> users = store.getUsers();
                Assert.assertTrue(store.hasIndex(users, UserDataStore.BY_NAME));
                store.dropIndex(users, UserDataStore.BY_NAME);

                // Then
                Assert.assertFalse(store.hasIndex(users, UserDataStore.BY_NAME));
            }
        }
    }

    private static @NotNull UserDataStore createStorage(MongoClient client) {
        return new UserDataStore(client, MongoTestCluster.DEFAULT_TEST_DB);
    }

    @Test
    public void givenStorage_whenAddingUsers_thenUsersArePresent() {
        // Given
        try (MongoClient client = this.mongo.createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When
                User a = User.builder().name("Adam").build();
                User b = User.builder().name("Bob").build();
                a = store.createUser(a);
                b = store.createUser(b);

                // Then
                Assert.assertNotNull(a.getId());
                Assert.assertNotNull(b.getId());
                List<User> users = store.listUsers();
                Assert.assertEquals(users.size(), 2);
            }
        }
    }

    @Test
    public void givenStorage_whenAddingSameUserMultipleTimes_thenOneUserIsPresent() {
        // Given
        try (MongoClient client = this.mongo.createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When
                User a = User.builder().name("Adam").id("1").build();
                for (int i = 1; i <= 10; i++) {
                    store.createUser(a);
                }

                // Then
                List<User> users = store.listUsers();
                Assert.assertEquals(users.size(), 1);
            }
        }
    }

    @Test
    public void givenStorage_whenAddingManyUsers_thenUsersAreAllPresent() {
        // Given
        try (MongoClient client = this.mongo.createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When
                for (int i = 1; i <= 1_000; i++) {
                    User user = User.builder().name("User" + i).id(Integer.toString(i)).build();
                    store.createUser(user);
                }

                // Then
                List<User> users = store.listUsers();
                Assert.assertEquals(users.size(), 1_000);
            }
        }
    }

    @Test
    public void givenStorageWithUsers_whenSavingData_thenDataIsRetrievable_andNotRetrievableForDifferentUser() {
        // Given
        try (MongoClient client = this.mongo.createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                User a = User.builder().name("Adam").build();
                User b = User.builder().name("Bob").build();
                a = store.createUser(a);
                b = store.createUser(b);

                // When
                SavedData data = SavedData.builder().user(a.getName()).name("Test").build();
                store.createSavedData(data);

                // Then
                List<SavedData> retrieved = store.getSavedData(a);
                Assert.assertEquals(retrieved.size(), 1);
                retrieved = store.getSavedData(b);
                Assert.assertEquals(retrieved.size(), 0);
            }
        }
    }
}
