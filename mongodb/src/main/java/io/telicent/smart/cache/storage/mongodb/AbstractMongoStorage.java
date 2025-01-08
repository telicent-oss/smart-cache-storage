/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.mongodb;

import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.result.DeleteResult;
import io.telicent.smart.cache.storage.AbstractStorage;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.conversions.Bson;
import org.mongojack.JacksonMongoCollection;

import javax.persistence.Id;
import java.util.*;
import java.util.function.Supplier;

import static com.mongodb.client.model.Filters.eq;

/**
 * Abstract Mongo storage backend based upon the official MongoDB Java Client and MongoJack libraries
 * <p>
 * Derived storage classes will typically extend this class and implement an appropriate storage interface for the
 * service they are implementing storage for.  Derived classes have two main ways to interact with the underlying
 * storage:
 * </p>
 * <ol>
 *     <li>
 *     The {@link #getCollection(String, Class, UuidRepresentation)} method to obtain a reference to a specific Mongo
 *     collection within the database.
 *     </li>
 *     <li>
 *     The various helper methods that take the {@link JacksonMongoCollection} returned by
 *     {@link #getCollection(String, Class, UuidRepresentation)} and use it to carry out common operations e.g.
 *     {@link #createOrUpdate(JacksonMongoCollection, Object, Bson)} and {@link #getAll(JacksonMongoCollection, Bson)}
 *     </li>
 * </ol>
 * <p>
 * Most methods require callers to supply a {@link Bson} query to identify the entities to act against.  There is a
 * {@link #findById(String)} helper method for generating a simple ID based query, but we expect most derived
 * implementations will need to construct more complex queries according to their needs.
 * </p>
 * <p>
 * Please see the Javadoc on individual methods for more details of each methods functionality.
 * </p>
 */
public class AbstractMongoStorage extends AbstractStorage {
    private static final String MONGO_ID_FIELD = "_id";
    private final MongoClient mongo;
    private final MongoDatabase db;
    private final Map<String, JacksonMongoCollection<?>> collections = new HashMap<>();

    /**
     * Creates new Mongo storage
     *
     * @param client   MongoDB Client
     * @param database Database to connect to
     */
    public AbstractMongoStorage(MongoClient client, String database) {
        this.mongo = Objects.requireNonNull(client, "Mongo Client cannot be null");
        if (StringUtils.isBlank(database)) {
            throw new IllegalArgumentException("Database cannot be null or empty");
        }
        this.db = this.mongo.getDatabase(database);
    }

    protected String createIndex(JacksonMongoCollection<?> collection, IndexOptions options, String... fields) {
        ensureNotClosed();
        return collection.createIndex(Indexes.ascending(fields), options);
    }

    /**
     * Gets a Jackson Mongo Collection
     *
     * @param collectionName     Collection Name
     * @param entityClass        Entity Class
     * @param uuidRepresentation UUID representation
     * @param <T>                Entity type
     * @return Jackson Mongo Collection
     */
    @SuppressWarnings("unchecked")
    protected <T> JacksonMongoCollection<T> getCollection(String collectionName, Class<T> entityClass,
                                                          UuidRepresentation uuidRepresentation) {
        ensureNotClosed();

        return (JacksonMongoCollection<T>) this.collections.computeIfAbsent(collectionName,
                                                                            n -> JacksonMongoCollection.builder()
                                                                                                       .build(this.db,
                                                                                                              n,
                                                                                                              entityClass,
                                                                                                              uuidRepresentation));
    }

    /**
     * Gets all results from a collection that match the given query
     *
     * @param collection Collection
     * @param query      Query
     * @param <T>        Entity type
     * @return Results
     */
    protected <T> List<T> getAll(JacksonMongoCollection<T> collection, Bson query) {
        ensureNotClosed();

        MongoCursor<T> cursor = collection.find(query).iterator();
        List<T> results = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                results.add(cursor.next());
            }
        } finally {
            cursor.close();
        }
        return results;
    }

    /**
     * Gets the first entity from the collection that matches the query
     * <p>
     * See {@link #getAll(JacksonMongoCollection, Bson)} if you want to retrieve all possible results
     * </p>
     *
     * @param collection Collection
     * @param query      Query used to identify the entity
     * @param <T>        Entity type
     * @return Entity
     */
    protected <T> T get(JacksonMongoCollection<T> collection, Bson query) {
        ensureNotClosed();

        return collection.findOne(query);
    }

    /**
     * Creates/updates an entity in the collection
     *
     * @param collection Collection
     * @param entity     Entity
     * @param query      Query used to identify the existing entity to replace, if this returns nothing then a new
     *                   entity is created
     * @param <T>        Entity type
     * @return Entity, possibly updated
     */
    protected <T> T createOrUpdate(JacksonMongoCollection<T> collection, T entity, Bson query) {
        ensureNotClosed();

        if (collection.findOne(query) != null) {
            collection.replaceOne(query, entity);
        } else {
            collection.save(entity);
        }
        return entity;
    }

    /**
     * Deletes a single entity from the collection
     *
     * @param collection Collection
     * @param query      Query used to identify the entity to delete
     * @return True if an entity was deleted, false if nothing was deleted or the write operation wasn't acknowledged
     */
    protected boolean deleteOne(JacksonMongoCollection<?> collection, Bson query) {
        ensureNotClosed();

        DeleteResult result = collection.deleteOne(query);
        return result.wasAcknowledged() && result.getDeletedCount() > 0;
    }

    /**
     * Deletes many entities from the collection
     *
     * @param collection Collection
     * @param query      Query used to identify the entities to delete
     * @return True if any entities were deleted, false if nothing was deleted or the write operation wasn't
     * acknowledged
     */
    protected boolean deleteMany(JacksonMongoCollection<?> collection, Bson query) {
        ensureNotClosed();

        DeleteResult result = collection.deleteMany(query);
        return result.wasAcknowledged() && result.getDeletedCount() > 0;
    }

    /**
     * Creates a BSON query that queries based upon the ID field
     * <p>
     * This assumes that entities in the collection have a {@value MONGO_ID_FIELD} present, this is the default Mongo ID
     * field so should be the case unless you have constructed your documents in some other way.
     * </p>
     * <p>
     * For seamless integration with Mongo Jack the entity class should have the {@link Id} annotation added to the
     * field you are using as the entity ID.
     * </p>
     *
     * @param id ID
     * @return A basic BSON query against the {@value MONGO_ID_FIELD}
     */
    protected Bson findById(String id) {
        return eq(MONGO_ID_FIELD, id);
    }

    @Override
    protected void closeInternal() {
        this.mongo.close();
    }
}
