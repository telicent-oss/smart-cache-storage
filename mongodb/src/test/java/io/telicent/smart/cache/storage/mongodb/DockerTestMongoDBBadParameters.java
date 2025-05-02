/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.IndexOptions;
import io.telicent.smart.cache.storage.AbstractStorage;
import io.telicent.smart.cache.storage.mongodb.cluster.MongoTestCluster;
import io.telicent.smart.cache.storage.mongodb.model.User;
import io.telicent.smart.cache.storage.mongodb.model.UserDataStore;
import org.apache.commons.lang3.ArrayUtils;
import org.bson.UuidRepresentation;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.mongojack.JacksonMongoCollection;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DockerTestMongoDBBadParameters extends AbstractMongoDBTests {

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "Mongo Client.*")
    public void givenNoMongoClient_whenConstructing_thenNPE() {
        try (BadStorage storage = new BadStorage(MongoTestCluster.DEFAULT_TEST_DB)) {
            Assert.fail("Constructor should have errored");
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Database cannot.*")
    public void givenNoDatabase_whenConstructing_thenIllegalArgument() {
        try (MongoClient client = this.mongo.createMongoClient()) {
            try (BadStorage storage = new BadStorage(client)) {
                Assert.fail("Constructor should have errored");
            }
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Collection.*")
    public void givenStorage_whenRetrievingCollectionWithoutName_thenIllegalArgument() {
        // Given
        try (MongoClient client = this.mongo.createMongoClient()) {
            try (BadStorage storage = new BadStorage(client, MongoTestCluster.DEFAULT_TEST_DB)) {
                // When and Then
                storage.getCollection(null, User.class, UuidRepresentation.STANDARD);
            }
        }
    }

    @Test
    public void givenStorage_whenCheckingForUnnamedIndex_thenFalse() {
        // Given
        try (MongoClient client = this.mongo.createMongoClient()) {
            try (BadStorage storage = new BadStorage(client, MongoTestCluster.DEFAULT_TEST_DB)) {
                // When
                boolean hasIndex = storage.hasIndex(getUsersCollection(storage), null);

                // Then
                Assert.assertFalse(hasIndex);
            }
        }
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = AbstractStorage.STORAGE_ALREADY_CLOSED)
    public void givenClosedStorage_whenAttemptingOperations_thenIllegalState() {
        // Given
        try (MongoClient mongoClient = this.mongo.createMongoClient()) {
            try (BadStorage storage = new BadStorage(mongoClient, MongoTestCluster.DEFAULT_TEST_DB)) {
                storage.close();

                // When and Then
                getUsersCollection(storage);
            }
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*must include a non-empty name")
    public void givenStorage_whenCreatingIndexIfNotExistWithoutName_thenIllegalArgument() {
        // Given
        try (MongoClient mongoClient = this.mongo.createMongoClient()) {
            try (BadStorage storage = new BadStorage(mongoClient,MongoTestCluster.DEFAULT_TEST_DB)) {
                // When
                JacksonMongoCollection<User> collection = getUsersCollection(storage);
                Assert.assertNotNull(collection);
                storage.createIndexIfNotExist(collection, new IndexOptions(), "user");
            }
        }
    }

    private static JacksonMongoCollection<User> getUsersCollection(BadStorage storage) {
        return storage.getCollection(UserDataStore.USERS_COLLECTION, User.class, UuidRepresentation.JAVA_LEGACY);
    }

    @Test
    public void givenStorage_whenCreatingIndexWithoutName_thenGeneratedNameReturned() {
        // Given
        try (MongoClient mongoClient = this.mongo.createMongoClient()) {
            try (BadStorage storage = new BadStorage(mongoClient,MongoTestCluster.DEFAULT_TEST_DB)) {
                // When
                JacksonMongoCollection<User> collection = getUsersCollection(storage);
                String name = storage.createIndex(collection, null, "user");

                // Then
                Assert.assertNotNull(name);
            }
        }
    }

    @DataProvider(name = "collectionMethods")
    private Object[][] methodsWithCollectionArgument() {
        Class<AbstractMongoStorage> cls = AbstractMongoStorage.class;

        List<Method> methods = new ArrayList<>();
        for (Method method : cls.getDeclaredMethods()) {
            if (method.getParameterCount() >= 1) {
                if (Objects.equals(method.getParameterTypes()[0], JacksonMongoCollection.class)) {
                    methods.add(method);
                }
            }
        }

        Object[][] data = new Object[methods.size()][];
        int i = 0;
        for (Method method : methods) {
            data[i] = new Object[] { method.getName(), method.getParameterTypes() };
            i++;
        }
        return data;
    }

    @Test(dataProvider = "collectionMethods", expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "Collection.*")
    public void givenStorage_whenCallingMethodWithNullCollection_thenNPE(String methodName,
                                                                         Class<?>[] parameterTypes) throws Throwable {
        // Given
        try (MongoClient mongoClient = this.mongo.createMongoClient()) {
            try (AbstractMongoStorage storage = new BadStorage(mongoClient,MongoTestCluster.DEFAULT_TEST_DB)) {
                Method method = AbstractMongoStorage.class.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);

                // When and Then
                Object[] args = new Object[method.getParameterCount()];
                try {
                    method.invoke(storage, args);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }
            }
        }
    }

    @Test(dataProvider = "collectionMethods", expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = AbstractStorage.STORAGE_ALREADY_CLOSED)
    public void givenClosedStorage_whenCallingMethodsWithCollection_thenIllegalState(String methodName,
                                                                                     Class<?>[] parameterTypes) throws
            Throwable {
        // Given
        try (MongoClient mongoClient = this.mongo.createMongoClient()) {
            try (AbstractMongoStorage storage = new BadStorage(mongoClient,MongoTestCluster.DEFAULT_TEST_DB)) {
                storage.close();
                Method method = AbstractMongoStorage.class.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);

                // When and Then
                Object[] args = new Object[method.getParameterCount()];
                try {
                    method.invoke(storage, args);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }
            }
        }
    }

    @Test(dataProvider = "collectionMethods", expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "Query.*")
    public void givenStorage_whenCallingMethodWithNullQuery_thenNPE(String methodName, Class<?>[] parameterTypes) throws
            Throwable {
        if (!ArrayUtils.contains(parameterTypes, Bson.class)) {
            throw new SkipException("Method does not include a query parameter");
        }

        // Given
        try (MongoClient mongoClient = this.mongo.createMongoClient()) {
            try (BadStorage storage = new BadStorage(mongoClient,MongoTestCluster.DEFAULT_TEST_DB)) {
                Method method = AbstractMongoStorage.class.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                JacksonMongoCollection<User> collection = getUsersCollection(storage);

                // When and Then
                Object[] args = new Object[method.getParameterCount()];
                args[0] = collection;
                try {
                    method.invoke(storage, args);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }
            }
        }
    }

    @Test
    public void givenMockClient_whenDeletesAreNotAcknowledge_thenDeletesReturnFalse() {
        // Given
        try (MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
                                                                              .applyConnectionString(
                                                                                      new ConnectionString(
                                                                                              this.mongo.getConnectionString()))
                                                                              .writeConcern(WriteConcern.UNACKNOWLEDGED)
                                                                              .build())) {
            try (BadStorage storage = new BadStorage(mongoClient,MongoTestCluster.DEFAULT_TEST_DB)) {
                // When
                JacksonMongoCollection<User> users = getUsersCollection(storage);
                boolean oneDeleted = storage.deleteOne(users, storage.findById("test"));
                boolean manyDeleted = storage.deleteMany(users, storage.findAll());

                // Then
                Assert.assertFalse(oneDeleted);
                Assert.assertFalse(manyDeleted);
            }
        }
    }


    private static final class BadStorage extends AbstractMongoStorage {

        /**
         * Creates new Mongo storage
         *
         * @param client   MongoDB Client
         * @param database Database to connect to
         */
        public BadStorage(MongoClient client, String database) {
            super(client, database);
        }

        public BadStorage(MongoClient client) {
            super(client, null);
        }

        public BadStorage(String database) {
            super(null, database);
        }
    }
}
