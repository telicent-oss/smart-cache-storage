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
package io.telicent.smart.cache.storage.mongodb;

import com.mongodb.client.MongoClient;
import io.telicent.smart.cache.storage.mongodb.cluster.ClusterUtils;
import io.telicent.smart.cache.storage.mongodb.cluster.MongoTestCluster;
import io.telicent.smart.cache.storage.mongodb.model.SavedData;
import io.telicent.smart.cache.storage.mongodb.model.User;
import io.telicent.smart.cache.storage.mongodb.model.UserDataStore;
import org.bson.UuidRepresentation;
import org.jetbrains.annotations.NotNull;
import org.mongojack.JacksonMongoCollection;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.*;

public class DockerTestMongoPerformance extends AbstractMongoDBTests {

    private static final int MANY_USERS_SIZE = 1_000;

    private List<User> manyUsers;
    private Map<User, Integer> manyUsersDataCounts;

    @BeforeClass
    @Override
    public void setup() {
        super.setup();

        this.setupManyUsersAndData();
    }

    @AfterClass
    @Override
    public void teardown() {
        super.teardown();
    }

    private static @NotNull UserDataStore createStorage(MongoClient client) {
        return new UserDataStore(client, MongoTestCluster.DEFAULT_TEST_DB);
    }

    public void setupManyUsersAndData() {
        long started = ClusterUtils.logStart("Generating many users and data for performance tests");

        Random random = new Random();
        try (MongoClient client = this.mongo.createMongoClient()) {
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

        ClusterUtils.logFinished(String.format("Generated %,d users with %,d data items", manyUsers.size(),
                                               manyUsersDataCounts.values().stream().reduce(Integer::sum).orElse(0)),
                                 started);
    }

    @Test(timeOut = 1000L)
    public void givenStorageWithManyUsersAndData_whenRetrievingSavedData_thenReturnsPromptly() {
        // Given
        try (MongoClient client = this.mongo.createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When and Then
                for (User user : manyUsers) {
                    List<SavedData> saved = store.getSavedData(user);
                    Assert.assertEquals(saved.size(), manyUsersDataCounts.get(user));
                }
            }
        }
    }


    @Test
    public void givenStorageWithManyUsersAndData_whenRetrievingAllSavedData_thenExpectedSize() {
        // Given
        Long expected = manyUsersDataCounts.values().stream().reduce(0L, Long::sum, Long::sum);
        try (MongoClient client = this.mongo.createMongoClient()) {
            try (UserDataStore store = createStorage(client)) {
                // When
                List<SavedData> saved = store.getSavedData();

                // Then
                Assert.assertEquals(Long.valueOf(saved.size()), expected);
            }
        }
    }

    @Test(timeOut = 200L)
    public void givenStorageWithManyUsersAndData_whenRandomlyAccessingSavedData_thenReturnsPromptly() {
        // Given
        Random random = new Random();
        try (MongoClient client = this.mongo.createMongoClient()) {
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

    @Test(timeOut = 200L)
    public void givenStorageWithManyUsersAndData_whenRandomlyAccessingSavedDataById_thenReturnsPromptly() {
        // Given
        Random random = new Random();
        try (MongoClient client = this.mongo.createMongoClient()) {
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

    @Test
    public void givenStorageWithManyUsersAndData_whenRandomlyDeletingSavedData_thenChangesAreReflected() {
        // Given
        Random random = new Random();
        try (MongoClient client = this.mongo.createMongoClient()) {
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

    @Test
    public void givenStorageWithManyUsersAndData_whenRandomlyDeletingAllSavedDataForUser_thenChangesAreReflected() {
        // Given
        Random random = new Random();
        try (MongoClient client = this.mongo.createMongoClient()) {
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

    @Test
    public void givenStorageWithManyUsersAndData_whenRandomlyDeletingSavedDataByNonExistentIds_thenNothingChanges() {
        // Given
        Random random = new Random();
        try (MongoClient client = this.mongo.createMongoClient()) {
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

    @Test(timeOut = 200L)
    public void givenStorageWithManyUsersAndData_whenRandomlyAccessingUsers_thenReturnsPromptly() {
        // Given
        Random random = new Random();
        try (MongoClient client = this.mongo.createMongoClient()) {
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
