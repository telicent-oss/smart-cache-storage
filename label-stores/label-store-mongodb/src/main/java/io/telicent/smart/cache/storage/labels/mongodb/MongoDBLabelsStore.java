/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.mongodb.AbstractMongoStorage;
import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;

import java.util.*;

public class MongoDBLabelsStore extends AbstractMongoStorage implements DictionaryLabelsStore {

    /**
     * The collection used within the MongoDB database to store the encoded labels
     */
    public static final String ENCODED_LABELS_COLLECTION = "encoded-labels";

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
                                },
                                Filters.eq("label", encoded)).getId();
    }

    @Override
    public Map<byte[], Long> idsForLabels(List<byte[]> labels) {
        ensureNotClosed();

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

    private JacksonMongoCollection<EncodedLabel> getLabelsCollection() {
        return this.getCollection(ENCODED_LABELS_COLLECTION, EncodedLabel.class, UuidRepresentation.JAVA_LEGACY);
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
}
