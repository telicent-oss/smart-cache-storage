/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import io.telicent.smart.cache.storage.labels.LabelsStore;
import io.telicent.smart.cache.storage.mongodb.AbstractMongoStorage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;

import java.util.*;

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
    private final Object lock = new Object();

    /**
     * Creates new Mongo labels store
     *
     * @param client   MongoDB Client
     * @param database Database to connect to
     */
    public MongoDBLabelsStore(MongoClient client, String database) {
        super(client, database);
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
        Objects.requireNonNull(label, "label cannot be null");

        JacksonMongoCollection<EncodedLabel> labels = getLabelsCollection();
        String encoded = this.encoder.encodeToString(label);
        return getOrCreateLabel(labels, encoded);
    }

    private Long getOrCreateLabel(JacksonMongoCollection<EncodedLabel> labels, String encoded) {
        return this.getOrCreate(labels, () -> {
            EncodedLabel created = EncodedLabel.of(encoded);
            synchronized (lock) {
                created.setId(System.currentTimeMillis());
            }
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
            if (label == null) {
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
    public long labelSize() {
        ensureNotClosed();
        JacksonMongoCollection<EncodedLabel> labels = getLabelsCollection();
        return labels.countDocuments();
    }

    @Override
    public void setLabel(byte[] key, long labelId) {
        ensureNotClosed();
        if (LabelsStore.isInvalidKey(key)) {
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
            if (LabelsStore.isInvalidKey(entry.getKey()) || entry.getValue() == null) {
                continue;
            }

            this.setLabel(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Long getLabel(byte[] key) {
        ensureNotClosed();
        Objects.requireNonNull(key, "key cannot be null");

        JacksonMongoCollection<AssignedLabel> assignedLabels = getAssignedLabelsCollection();
        String encoded = this.encoder.encodeToString(key);
        AssignedLabel assignment = assignedLabels.findOne(findById(encoded));
        return assignment != null ? assignment.getLabelId() : null;
    }

    @Override
    public long keySize() {
        ensureNotClosed();
        JacksonMongoCollection<AssignedLabel> assignedLabels = getAssignedLabelsCollection();
        return assignedLabels.countDocuments();
    }
}
