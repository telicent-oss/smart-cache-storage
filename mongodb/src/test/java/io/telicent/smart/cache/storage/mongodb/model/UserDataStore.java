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
package io.telicent.smart.cache.storage.mongodb.model;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import io.telicent.smart.cache.storage.mongodb.AbstractMongoStorage;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;
import org.mongojack.JacksonMongoCollection;

import java.util.List;
import java.util.UUID;

/**
 * A toy MongoDB storage backend with two collections, one for users and one for their saved data
 */
public class UserDataStore extends AbstractMongoStorage {

    public static final String USERS_COLLECTION = "users";
    public static final String DATA_COLLECTION = "data";
    public static final String BY_USER = "byUser";
    public static final String BY_USER_AND_ID = "byUserAndId";
    public static final String BY_NAME = "byName";

    /**
     * Creates new user data store
     *
     * @param client   MongoDB Client
     * @param database Database to connect to
     */
    public UserDataStore(MongoClient client, String database) {
        super(client, database);

        JacksonMongoCollection<SavedData> savedData = this.getData();
        this.createIndexIfNotExist(savedData, new IndexOptions().name(BY_USER), "user");
        this.createIndexIfNotExist(savedData, new IndexOptions().name(BY_USER_AND_ID), "user", MONGO_ID_FIELD);

        JacksonMongoCollection<User> users = this.getUsers();
        this.createIndexIfNotExist(users, new IndexOptions().name(BY_NAME), "name");
    }

    public List<User> listUsers() {
        ensureNotClosed();
        return this.getAll(getUsers(), new Document());
    }

    public JacksonMongoCollection<User> getUsers() {
        return this.getCollection(USERS_COLLECTION, User.class, UuidRepresentation.JAVA_LEGACY);
    }

    public User getUserById(String id) {
        ensureNotClosed();
        return this.get(getUsers(), this.findById(id));
    }

    public User getUserByName(String name) {
        ensureNotClosed();
        return this.get(getUsers(), Filters.eq("name", name));
    }

    public User createUser(User user) {
        ensureNotClosed();
        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }
        return this.createOrUpdate(this.getUsers(), user, this.findById(user.getId()));
    }

    public List<SavedData> getSavedData() {
        ensureNotClosed();
        return this.getAll(this.getData());
    }

    public List<SavedData> getSavedData(User user) {
        ensureNotClosed();
        return this.getAll(getData(), findByUser(user.getName()));
    }

    public SavedData getSavedDataById(String id) {
        ensureNotClosed();
        return this.get(getData(), this.findById(id));
    }

    public boolean deleteSavedDataById(String id) {
        ensureNotClosed();
        return this.deleteOne(getData(), this.findById(id));
    }

    public boolean deleteSavedData(User user) {
        ensureNotClosed();
        return this.deleteMany(getData(), findByUser(user.getName()));
    }

    private static Bson findByUser(String name) {
        return Filters.eq("user", name);
    }

    public JacksonMongoCollection<SavedData> getData() {
        return this.getCollection(DATA_COLLECTION, SavedData.class, UuidRepresentation.JAVA_LEGACY);
    }

    public SavedData createSavedData(SavedData data) {
        ensureNotClosed();
        if (data.getId() == null) {
            data.setId(UUID.randomUUID().toString());
        }
        return this.createOrUpdate(this.getData(), data,
                                   findSavedData(data));
    }

    private @NotNull Bson findSavedData(SavedData data) {
        return Filters.and(findByUser(data.getUser()), this.findById(data.getId()));
    }

    public boolean deleteSavedData(SavedData data) {
        ensureNotClosed();
        return this.deleteOne(this.getData(), findSavedData(data));
    }
}
