/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.mongodb;

import com.mongodb.client.MongoClient;
import io.telicent.smart.cache.storage.mongodb.model.SavedData;
import io.telicent.smart.cache.storage.mongodb.model.User;
import io.telicent.smart.cache.storage.mongodb.model.UserDataStore;
import org.bson.UuidRepresentation;
import org.jetbrains.annotations.NotNull;
import org.mongojack.JacksonMongoCollection;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.*;

public class DockerTestMongoDBStorage extends AbstractMongoDBTests {

    private static final int MANY_USERS_SIZE = 1_000;

    private List<User> manyUsers;
    private Map<User, Integer> manyUsersDataCounts;

    @BeforeMethod(onlyForGroups = { "basic" })
    public void resetCollection() {
        if (this.mongo != null) {
            if (this.mongo.isRunning()) {
                try (MongoClient client = createMongoClient()) {
                    resetCollection(client, UserDataStore.USERS_COLLECTION);
                    resetCollection(client, UserDataStore.DATA_COLLECTION);
                }
            }
        }
    }

    @Test(groups = "basic")
    public void givenEmptyStorage_whenListingUsers_thenNoUsers() {
        // Given
        try (MongoClient client = createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When
                List<User> users = store.listUsers();

                // Then
                Assert.assertTrue(users.isEmpty());
            }
        }
    }

    @Test(groups = "basic")
    public void givenEmptyStorage_whenDeletingNonExistentSavedData_thenNothingDeleted() {
        // Given
        try (MongoClient client = createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When
                boolean deleted = store.deleteSavedDataById(UUID.randomUUID().toString());

                // Then
                Assert.assertFalse(deleted);
            }
        }
    }

    @Test(groups = "basic")
    public void givenEmptyStorage_whenDeletingNonExistentSavedDataForUser_thenNothingDeleted() {
        // Given
        try (MongoClient client = createMongoClient()) {
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
        try (MongoClient client = createMongoClient()) {
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
        try (MongoClient client = createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When and Then
                JacksonMongoCollection<User> users = store.getUsers();
                store.dropIndex(users, "noSuchIndex");
            }
        }
    }

    @Test(groups = "basic")
    public void givenEmptyStorage_whenDroppingIndexExistingIndex_thenSuccess() {
        // Given
        try (MongoClient client = createMongoClient()) {
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
        return new UserDataStore(client, DEFAULT_TEST_DB);
    }

    @Test(groups = "basic")
    public void givenStorage_whenAddingUsers_thenUsersArePresent() {
        // Given
        try (MongoClient client = createMongoClient()) {
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

    @Test(groups = "basic")
    public void givenStorage_whenAddingSameUserMultipleTimes_thenOneUserIsPresent() {
        // Given
        try (MongoClient client = createMongoClient()) {
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

    @Test(groups = "basic")
    public void givenStorage_whenAddingManyUsers_thenUsersAreAllPresent() {
        // Given
        try (MongoClient client = createMongoClient()) {
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
        try (MongoClient client = createMongoClient()) {
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

    @BeforeGroups({ "many-users-and-data" })
    public void setupManyUsersAndData() {
        Random random = new Random();
        try (MongoClient client = createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                manyUsers = new ArrayList<>();
                manyUsersDataCounts = new HashMap<>();
                for (int i = 1; i <= MANY_USERS_SIZE; i++) {
                    User user = User.builder().name("User" + i).id(Integer.toString(i)).build();
                    user = store.createUser(user);
                    manyUsers.add(user);

                    int dataItems = random.nextInt(1, 11);
                    manyUsersDataCounts.put(user, dataItems);
                    for (int j = 1; j <= dataItems; j++) {
                        SavedData data = SavedData.builder()
                                                  .user(user.getName())
                                                  .name("Data " + i + "/" + j)
                                                  .id(UUID.randomUUID().toString())
                                                  .build();
                        store.createSavedData(data);
                    }
                }
            }
        }
    }

    @AfterGroups(groups = { "many-users-and-data" })
    public void teardownManyUsersAndData() {
        this.resetCollection();
    }

    @Test(groups = { "many-users-and-data" }, timeOut = 1000L)
    public void givenStorageWithManyUsersAndData_whenRetrievingSavedData_thenReturnsPromptly() {
        // Given
        try (MongoClient client = createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When and Then
                for (User user : manyUsers) {
                    List<SavedData> saved = store.getSavedData(user);
                    Assert.assertEquals(saved.size(), manyUsersDataCounts.get(user));
                }
            }
        }
    }


    @Test(groups = { "many-users-and-data" })
    public void givenStorageWithManyUsersAndData_whenRetrievingAllSavedData_thenExpectedSize() {
        // Given
        Long expected = manyUsersDataCounts.values().stream().reduce(0L, Long::sum, Long::sum);
        try (MongoClient client = createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When
                List<SavedData> saved = store.getSavedData();

                // Then
                Assert.assertEquals(Long.valueOf(saved.size()), expected);
            }
        }
    }

    @Test(groups = { "many-users-and-data" }, timeOut = 200L)
    public void givenStorageWithManyUsersAndData_whenRandomlyAccessingSavedData_thenReturnsPromptly() {
        // Given
        Random random = new Random();
        try (MongoClient client = createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When and Then
                for (int i = 1; i <= 25; i++) {
                    User user = manyUsers.get(random.nextInt(0, manyUsers.size()));
                    List<SavedData> saved = store.getSavedData(user);
                    Assert.assertEquals(saved.size(), manyUsersDataCounts.get(user));
                }
            }
        }
    }

    @Test(groups = { "many-users-and-data" }, timeOut = 200L)
    public void givenStorageWithManyUsersAndData_whenRandomlyAccessingSavedDataById_thenReturnsPromptly() {
        // Given
        Random random = new Random();
        try (MongoClient client = createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                for (int i = 1; i <= 25; i++) {
                    // When
                    User user = manyUsers.get(random.nextInt(0, manyUsers.size()));
                    List<SavedData> saved = store.getSavedData(user);
                    SavedData savedData = saved.get(random.nextInt(0, saved.size()));
                    SavedData retrieved = store.getSavedDataById(savedData.getId());

                    // Then
                    Assert.assertEquals(retrieved, savedData);
                }
            }
        }
    }

    @Test(groups = { "many-users-and-data" })
    public void givenStorageWithManyUsersAndData_whenRandomlyDeletingSavedData_thenChangesAreReflected() {
        // Given
        Random random = new Random();
        try (MongoClient client = createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                for (int i = 1; i <= 25; i++) {
                    // When
                    User user = manyUsers.get(random.nextInt(0, manyUsers.size()));
                    List<SavedData> saved = store.getSavedData(user);
                    if (saved.isEmpty()) {
                        i--;
                        continue;
                    }
                    SavedData savedData = saved.get(random.nextInt(0, saved.size()));
                    Assert.assertTrue(store.deleteSavedData(savedData));
                    manyUsersDataCounts.put(user, manyUsersDataCounts.get(user) - 1);

                    // Then
                    saved = store.getSavedData(user);
                    Assert.assertEquals(saved.size(), manyUsersDataCounts.get(user));
                    Assert.assertFalse(saved.contains(savedData));
                }
            }
        }
    }

    @Test(groups = { "many-users-and-data" })
    public void givenStorageWithManyUsersAndData_whenRandomlyDeletingAllSavedDataForUser_thenChangesAreReflected() {
        // Given
        Random random = new Random();
        try (MongoClient client = createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                for (int i = 1; i <= 25; i++) {
                    // When
                    User user = manyUsers.get(random.nextInt(0, manyUsers.size()));
                    store.deleteSavedData(user);
                    manyUsersDataCounts.put(user, 0);

                    // Then
                    List<SavedData> saved = store.getSavedData(user);
                    Assert.assertTrue(saved.isEmpty());
                }
            }
        }
    }

    @Test(groups = { "many-users-and-data" })
    public void givenStorageWithManyUsersAndData_whenRandomlyDeletingSavedDataByNonExistentIds_thenNothingChanges() {
        // Given
        Random random = new Random();
        try (MongoClient client = createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                JacksonMongoCollection<SavedData> data =
                        store.getCollection(UserDataStore.DATA_COLLECTION, SavedData.class,
                                            UuidRepresentation.JAVA_LEGACY);
                long expected = data.countDocuments();
                for (int i = 1; i <= 25; i++) {
                    // When
                    Assert.assertFalse(store.deleteSavedDataById(UUID.randomUUID().toString()));

                    // Then
                    long actual = data.countDocuments();
                    Assert.assertEquals(actual, expected);
                }
            }
        }
    }

    @Test(groups = { "many-users-and-data" }, timeOut = 200L)
    public void givenStorageWithManyUsersAndData_whenRandomlyAccessingUsers_thenReturnsPromptly() {
        // Given
        Random random = new Random();
        try (MongoClient client = createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When
                for (int i = 1; i <= 25; i++) {
                    User user = manyUsers.get(random.nextInt(0, manyUsers.size()));
                    User retrievedById = store.getUserById(user.getId());
                    User retrievedByName = store.getUserByName(user.getName());

                    // Then
                    Assert.assertEquals(retrievedById, user);
                    Assert.assertEquals(retrievedByName, user);
                    Assert.assertEquals(retrievedById, retrievedByName);
                }
            }
        }
    }
}
