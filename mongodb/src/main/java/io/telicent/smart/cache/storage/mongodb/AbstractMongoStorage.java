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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Id;
import java.util.*;
import java.util.function.Consumer;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMongoStorage.class);

    /**
     * The default MongoDB ID field populated from the {@link Id} or {@link org.mongojack.ObjectId} annotated field of
     * your entity class
     */
    protected static final String MONGO_ID_FIELD = "_id";
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

    /**
     * Creates a new index on the collection, MongoDB may treat this as a no-op if the index already exists.
     * <p>
     * Use {@link #createIndexIfNotExist(JacksonMongoCollection, IndexOptions, String...)} if you want to conditionally
     * create the index based on whether it already exists.
     * </p>
     *
     * @param collection Collection
     * @param options    Index Options
     * @param fields     Fields to index on
     * @return Index name
     */
    protected String createIndex(JacksonMongoCollection<?> collection, IndexOptions options, String... fields) {
        ensureNotClosed();
        ensureCollection(collection);
        if (options == null) {
            options = new IndexOptions();
            LOGGER.warn("No index options provided so using default options");
        }
        if (StringUtils.isBlank(options.getName())) {
            LOGGER.warn("Index name not provided, MongoDB will assign a name automatically");
        }
        return collection.createIndex(Indexes.ascending(fields), options);
    }

    /**
     * Ensures that a non-null collection has been provided
     *
     * @param collection Collection
     */
    protected final void ensureCollection(JacksonMongoCollection<?> collection) {
        ensureNotClosed();
        Objects.requireNonNull(collection, "Collection cannot be null");
    }

    /**
     * Ensures that a non-null query has been provided
     *
     * @param query Query
     */
    protected final void ensureQuery(Bson query) {
        Objects.requireNonNull(query, "Query cannot be null");
    }

    /**
     * Ensures that the standard preconditions are met
     *
     * @param collection Collection
     * @param query      Query
     */
    protected final void ensurePreconditionsMet(JacksonMongoCollection<?> collection, Bson query) {
        ensureNotClosed();
        ensureCollection(collection);
        ensureQuery(query);
    }

    /**
     * Checks whether an index with the given name exists on the collection
     *
     * @param collection Collection
     * @param name       Index Name
     * @return True if exists on the index, false otherwise
     */
    protected boolean hasIndex(JacksonMongoCollection<?> collection, String name) {
        ensureNotClosed();
        ensureCollection(collection);

        // MongoDB indices always have a name
        if (StringUtils.isBlank(name)) {
            return false;
        }

        // Check whether an index exists with the name
        ListIndexesIterable<Document> indices = collection.listIndexes();
        for (Document index : indices) {
            if (Objects.equals(index.getString("name"), name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create a named index on the collection if one with the given name does not yet exist
     * <p>
     * NB: This <strong>DOES NOT</strong> check whether the pre-existing index matches the options and fields specified
     * by the caller.  If you are looking to redefine an index with different options and/or fields, then you should
     * first call {@link #dropIndex(JacksonMongoCollection, String)} to ensure the index does not exist.
     * </p>
     *
     * @param collection Collection
     * @param options    Index Options, this <strong>MUST</strong> include a name
     * @param fields     Fields to use for the index
     * @return Whether the index was created
     */
    protected boolean createIndexIfNotExist(JacksonMongoCollection<?> collection, IndexOptions options,
                                            String... fields) {
        ensureNotClosed();
        ensureCollection(collection);
        Objects.requireNonNull(options, "Index options cannot be null");

        // Must supply a name in the index options
        if (StringUtils.isBlank(options.getName())) {
            throw new IllegalArgumentException("Index Options must include a non-empty name");
        }

        // Check whether the index already exists, if not create it
        if (hasIndex(collection, options.getName())) {
            LOGGER.debug("Index {} already exists on collection {}", options.getName(), collection.getName());
            return false;
        } else {
            String created = this.createIndex(collection, options, fields);
            LOGGER.info("Created index {} on collection {}", created, collection.getName());
            return true;
        }
    }

    /**
     * Drops an index from the collection
     *
     * @param collection Collection
     * @param name       Index name
     * @throws IllegalArgumentException Thrown if index name is not provided, or no such index exists on the collection
     */
    protected void dropIndex(JacksonMongoCollection<?> collection, String name) {
        ensureNotClosed();
        ensureCollection(collection);
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Cannot drop index if no name provided");
        }
        if (this.hasIndex(collection, name)) {
            collection.dropIndex(name);
        } else {
            throw new IllegalArgumentException(
                    "No index " + name + " exists to drop on collection " + collection.getName());
        }
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
        if (StringUtils.isBlank(collectionName)) {
            throw new IllegalArgumentException("Collection name cannot be null or empty");
        }
        Objects.requireNonNull(entityClass, "Entity class cannot be null");
        Objects.requireNonNull(uuidRepresentation, "uuid representation cannot be null");

        return (JacksonMongoCollection<T>) this.collections.computeIfAbsent(collectionName,
                                                                            n -> JacksonMongoCollection.builder()
                                                                                                       .build(this.db,
                                                                                                              n,
                                                                                                              entityClass,
                                                                                                              uuidRepresentation));
    }

    /**
     * Gets all entities from a collection
     *
     * @param collection Collection
     * @param <T>        Entity type
     * @return Entities
     */
    protected <T> List<T> getAll(JacksonMongoCollection<T> collection) {
        return getAll(collection, findAll());
    }

    /**
     * Gets all entities from a collection that match the given query
     *
     * @param collection Collection
     * @param query      Query
     * @param <T>        Entity type
     * @return Entities
     */
    protected <T> List<T> getAll(JacksonMongoCollection<T> collection, Bson query) {
        ensurePreconditionsMet(collection, query);

        List<T> results = new ArrayList<>();
        processCursor(collection, query, results::add);
        return results;
    }

    /**
     * Processes a Mongo cursor sending each entity to a consumer function
     *
     * @param collection Collection
     * @param query      Query
     * @param consumer   Consumer function
     * @param <T>        Entity type
     */
    protected final <T> void processCursor(JacksonMongoCollection<T> collection, Bson query, Consumer<T> consumer) {
        ensurePreconditionsMet(collection, query);
        try (MongoCursor<T> cursor = collection.find(query).iterator()) {
            while (cursor.hasNext()) {
                consumer.accept(cursor.next());
            }
        }
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
        ensurePreconditionsMet(collection, query);

        return collection.findOne(query);
    }

    /**
     * Gets/creates an entity in the collection
     *
     * @param collection Collection
     * @param creator    Supplier that creates the entity to save if a matching entity does not exist
     * @param query      Query used to identify the existing entity to return, if this returns nothing then a new entity
     *                   is created
     * @param <T>        Entity type
     * @return Entity, possibly updated
     */
    protected <T> T getOrCreate(JacksonMongoCollection<T> collection, Supplier<T> creator, Bson query) {
        ensurePreconditionsMet(collection, query);

        T found = collection.findOne(query);
        if (found != null) {
            return found;
        } else {
            T created = creator.get();
            collection.save(created);
            return created;
        }
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
        ensurePreconditionsMet(collection, query);

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
        ensurePreconditionsMet(collection, query);

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
        ensurePreconditionsMet(collection, query);

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
        Objects.requireNonNull(id, "ID cannot be null");
        return eq(MONGO_ID_FIELD, id);
    }

    /**
     * Creates a BSON query that matches all entities
     *
     * @return Find all query
     */
    protected final Bson findAll() {
        return new Document();
    }

    @Override
    protected void closeInternal() {
        this.mongo.close();
    }
}
