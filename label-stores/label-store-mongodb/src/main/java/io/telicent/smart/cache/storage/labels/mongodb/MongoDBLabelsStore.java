/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.LabelsStore;
import io.telicent.smart.cache.storage.mongodb.AbstractMongoStorage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static com.mongodb.client.model.Sorts.descending;

/**
 * A labels store backed by a MongoDB database
 * <p>
 * Two collections are used, one to store the mapping of label byte sequences to IDs, and another to store mapping of
 * keys to label IDs.
 * </p>
 */
public class MongoDBLabelsStore extends AbstractMongoStorage implements LabelsStore {

    /**
     * The collection used within the MongoDB database to store the encoded labels
     */
    public static final String ENCODED_LABELS_COLLECTION = "encoded-labels";
    /**
     * The collection used within the MongoDB database to store assignments from keys to label IDs
     */
    public static final String ASSIGNED_LABELS_COLLECTION = "assigned-labels";

    private final Base64.Encoder encoder = Base64.getEncoder();
    private final AtomicLong nextId = new AtomicLong(1);

    /**
     * Creates new Mongo labels store
     *
     * @param client   MongoDB Client
     * @param database Database to connect to
     */
    public MongoDBLabelsStore(MongoClient client, String database) {
        super(client, database);

        // Determine the next available ID to issue
        syncNextId();
    }

    protected void syncNextId() {
        JacksonMongoCollection<EncodedLabel> collection = getLabelsCollection();
        EncodedLabel maxExisting = collection.find()
                                             .sort(descending(MONGO_ID_FIELD))
                                             .limit(1).first();

        if (maxExisting != null) {
            this.nextId.set(maxExisting.getId() + 1);
        }
    }

    /**
     * Gets the labels collection
     *
     * @return Labels collection
     */
    private JacksonMongoCollection<EncodedLabel> getLabelsCollection() {
        return this.getCollection(ENCODED_LABELS_COLLECTION, EncodedLabel.class, UuidRepresentation.JAVA_LEGACY);
    }

    /**
     * Gets the label assignments collection
     *
     * @return Label assignments collection
     */
    private JacksonMongoCollection<AssignedLabel> getAssignedLabelsCollection() {
        return this.getCollection(ASSIGNED_LABELS_COLLECTION, AssignedLabel.class, UuidRepresentation.JAVA_LEGACY);
    }

    @Override
    public long idForLabel(byte[] label) {
        ensureNotClosed();
        if (DictionaryLabelsStore.isInvalidByteSequence(label)) {
            throw new NullPointerException("label cannot be null/empty");
        }

        JacksonMongoCollection<EncodedLabel> labels = getLabelsCollection();
        String encoded = this.encoder.encodeToString(label);
        return getOrCreateLabel(labels, encoded);
    }

    private Long getOrCreateLabel(JacksonMongoCollection<EncodedLabel> labels, String encoded) {
        return this.getOrCreate(labels, () -> {
            EncodedLabel created = EncodedLabel.of(encoded);
            created.setId(this.nextId.getAndIncrement());
            return created;
        }, Filters.eq("label", encoded)).getId();
    }

    @Override
    public Map<byte[], Long> idsForLabels(List<byte[]> labels) {
        ensureNotClosed();
        if (CollectionUtils.isEmpty(labels)) {
            return Collections.emptyMap();
        }

        JacksonMongoCollection<EncodedLabel> collection = getLabelsCollection();
        Map<byte[], Long> ids = new LinkedHashMap<>();
        for (byte[] label : labels) {
            if (DictionaryLabelsStore.isInvalidByteSequence(label)) {
                continue;
            }
            if (ids.containsKey(label)) {
                continue;
            }
            String encoded = this.encoder.encodeToString(label);
            ids.put(label, getOrCreateLabel(collection, encoded));
        }
        return ids;
    }

    @Override
    public byte[] labelForId(long id) {
        ensureNotClosed();

        JacksonMongoCollection<EncodedLabel> labels = getLabelsCollection();
        EncodedLabel label = this.get(labels, Filters.eq(MONGO_ID_FIELD, id));
        return label != null ? label.getDecodedLabel() : null;
    }

    @Override
    public Map<Long, byte[]> labelsForIds(List<Long> ids) {
        ensureNotClosed();
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }

        JacksonMongoCollection<EncodedLabel> labels = getLabelsCollection();
        List<EncodedLabel> encodedLabels = this.getAll(labels, Filters.in(MONGO_ID_FIELD, ids.stream()
                                                                                             .filter(Objects::nonNull)
                                                                                             .distinct()
                                                                                             .toList()));
        Map<Long, byte[]> result = new LinkedHashMap<>();
        for (EncodedLabel label : encodedLabels) {
            result.put(label.getId(), label.getDecodedLabel());
        }
        for (Long id : ids) {
            if (id != null && !result.containsKey(id)) {
                result.put(id, null);
            }
        }
        return result;
    }

    @Override
    public long labelCount() {
        ensureNotClosed();
        JacksonMongoCollection<EncodedLabel> labels = getLabelsCollection();
        return labels.countDocuments();
    }

    @Override
    public void setLabel(byte[] key, long labelId) {
        ensureNotClosed();
        if (DictionaryLabelsStore.isInvalidByteSequence(key)) {
            throw new NullPointerException("key cannot be null/empty");
        }

        String encoded = this.encoder.encodeToString(key);
        JacksonMongoCollection<AssignedLabel> assignedLabels = getAssignedLabelsCollection();
        this.createOrUpdate(assignedLabels, AssignedLabel.of(encoded, labelId), findById(encoded));
    }

    @Override
    public void setLabels(Map<byte[], Long> keysToLabels) {
        ensureNotClosed();
        if (MapUtils.isEmpty(keysToLabels)) {
            return;
        }

        for (Map.Entry<byte[], Long> entry : keysToLabels.entrySet()) {
            if (DictionaryLabelsStore.isInvalidByteSequence(entry.getKey()) || entry.getValue() == null) {
                continue;
            }

            this.setLabel(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Long getLabel(byte[] key) {
        ensureNotClosed();
        if (DictionaryLabelsStore.isInvalidByteSequence(key)) {
            throw new NullPointerException("key cannot be null/empty");
        }

        JacksonMongoCollection<AssignedLabel> assignedLabels = getAssignedLabelsCollection();
        String encoded = this.encoder.encodeToString(key);
        AssignedLabel assignment = assignedLabels.findOne(findById(encoded));
        return assignment != null ? assignment.getLabelId() : null;
    }

    @Override
    public long keyCount() {
        ensureNotClosed();
        JacksonMongoCollection<AssignedLabel> assignedLabels = getAssignedLabelsCollection();
        return assignedLabels.countDocuments();
    }
}
