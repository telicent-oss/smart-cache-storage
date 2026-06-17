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
package io.telicent.smart.cache.storage.labels.rocksdb;

import io.telicent.smart.cache.storage.*;
import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.LabelsStore;
import io.telicent.smart.cache.storage.rocksdb.AbstractRocksDBStorage;
import io.telicent.smart.cache.storage.rocksdb.RocksDBCounter;
import io.telicent.smart.cache.storage.rocksdb.TransactionContext;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.rocksdb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.rocksdb.RocksDB.DEFAULT_COLUMN_FAMILY;

/**
 * RocksDB implementation of a labels store
 * <p>
 * Uses different column families to store the mapping from labels to IDs, IDs to labels, and keys to IDs.  IDs are
 * assigned based upon a persistent counter that is stored in another column family.  All operations are done
 * transactionally to ensure thread safety.
 * </p>
 */
public class RocksDbLabelsStore extends AbstractRocksDBStorage implements LabelsStore {

    // Map labels to IDs
    protected static final byte[] LABELS_TO_IDS_CF = "labels_to_ids".getBytes(StandardCharsets.UTF_8);
    // Map IDs to labels
    protected static final byte[] IDS_TO_LABELS_CF = "ids_to_labels".getBytes(StandardCharsets.UTF_8);
    // Counters
    protected static final byte[] COUNTERS_CF = "counters".getBytes(StandardCharsets.UTF_8);
    // Map Keys to Label IDs
    protected static final byte[] KEYS_TO_LABELS_CF = "keys_to_labels".getBytes(StandardCharsets.UTF_8);
    // Next Label ID counter key
    protected static final byte[] ID_COUNTER_KEY = "next_label_id".getBytes(StandardCharsets.UTF_8);

    /**
     * Creates a new RocksDB labels store
     *
     * @param dbDir Path on disk where the labels store will live
     * @throws IOException      Thrown if the path cannot be prepared
     * @throws RocksDBException Thrown if the database cannot be opened
     */
    public RocksDbLabelsStore(File dbDir) throws IOException, RocksDBException {
        super(dbDir);
    }

    @Override
    protected List<ColumnFamilyDescriptor> prepareColumnFamilyDescriptors(ColumnFamilyOptions cfOptions) {
        ColumnFamilyDescriptor defaultCFD = new ColumnFamilyDescriptor(DEFAULT_COLUMN_FAMILY, cfOptions);
        ColumnFamilyDescriptor labelsToIdsCFD = new ColumnFamilyDescriptor(LABELS_TO_IDS_CF, cfOptions);
        ColumnFamilyDescriptor idsToLabelsCFD = new ColumnFamilyDescriptor(IDS_TO_LABELS_CF, cfOptions);
        ColumnFamilyDescriptor keysToLabelsCFD = new ColumnFamilyDescriptor(KEYS_TO_LABELS_CF, cfOptions);
        ColumnFamilyDescriptor idCounterCFD = new ColumnFamilyDescriptor(COUNTERS_CF, cfOptions);

        return List.of(defaultCFD, labelsToIdsCFD, idsToLabelsCFD, keysToLabelsCFD, idCounterCFD);
    }

    @Override
    protected Map<String, RocksDBCounter> prepareCounters() throws RocksDBException {
        String idCounterKey = new String(ID_COUNTER_KEY, StandardCharsets.UTF_8);
        return Map.of(idCounterKey, createCounter(COUNTERS_CF, idCounterKey));
    }

    @Override
    public long idForLabel(byte[] labelBytes) {
        this.ensureNotClosed();
        if (DictionaryLabelsStore.isInvalidByteSequence(labelBytes)) {
            throw new NullPointerException("label cannot be null/empty");
        }

        try (TransactionContext transaction = this.begin()) {
            ColumnFamilyHandle labelToIdHandle = getHandle(LABELS_TO_IDS_CF);
            RocksDBCounter counter = getCounter(ID_COUNTER_KEY);

            byte[] idBytes = transaction.get(labelToIdHandle, labelBytes);
            if (idBytes != null) {
                return bytesToLong(idBytes);
            }

            long newId = counter.next();
            byte[] newIdBytes = longToBytes(newId);

            ColumnFamilyHandle idToLabelHandle = getHandle(IDS_TO_LABELS_CF);

            transaction.put(labelToIdHandle, labelBytes, newIdBytes);
            transaction.put(idToLabelHandle, newIdBytes, labelBytes);
            counter.update(transaction);

            transaction.commit();

            return newId;
        } catch (RocksDBException e) {
            throw new RuntimeException("Error accessing RocksDB", e);
        }

    }

    @Override
    public Map<byte[], Long> idsForLabels(List<byte[]> labels) {
        this.ensureNotClosed();
        if (CollectionUtils.isEmpty(labels)) {
            return Collections.emptyMap();
        }

        ColumnFamilyHandle labelToIdHandle = getHandle(LABELS_TO_IDS_CF);
        ColumnFamilyHandle idToLabelHandle = getHandle(IDS_TO_LABELS_CF);
        RocksDBCounter counter = getCounter(ID_COUNTER_KEY);

        List<byte[]> queryLabels = new ArrayList<>();
        List<ColumnFamilyHandle> cfHandles = new ArrayList<>();

        for (byte[] label : labels) {
            if (DictionaryLabelsStore.isInvalidByteSequence(label)) {
                continue;
            }
            queryLabels.add(label);
            cfHandles.add(labelToIdHandle);
        }

        if (queryLabels.isEmpty()) {
            return Collections.emptyMap();
        }

        try (TransactionContext transaction = this.begin()) {
            // 1) Bulk lookup existing IDs
            List<byte[]> existing;
            existing = transaction.multiGetAsList(cfHandles, queryLabels);
            Map<byte[], Long> result = new HashMap<>();
            List<byte[]> newLabels = new ArrayList<>();

            for (int i = 0; i < queryLabels.size(); i++) {
                byte[] idBytes = existing.get(i);
                byte[] label = queryLabels.get(i);

                if (idBytes != null) {
                    // already had an id
                    result.put(label, bytesToLong(idBytes));
                } else {
                    // needs a new id
                    newLabels.add(label);
                }
            }

            // 2) Allocate IDs for any labels we haven't previously seen
            if (!newLabels.isEmpty()) {
                for (byte[] label : newLabels) {
                    long newId = counter.next();
                    byte[] newIdBytes = longToBytes(newId);

                    result.put(label, newId);
                    transaction.put(labelToIdHandle, label, newIdBytes);
                    transaction.put(idToLabelHandle, newIdBytes, label);
                }

                // persist updated counter
                counter.update(transaction);
                transaction.commit();
            }
            return result;
        } catch (RocksDBException e) {
            throw new RuntimeException("Error writing to RocksDB in bulk idsForLabels", e);
        }
    }

    @Override
    public byte[] labelForId(long id) {
        this.ensureNotClosed();

        if (id <= 0) return null;

        byte[] idBytes = longToBytes(id);
        ColumnFamilyHandle idToLabelHandle = getHandle(IDS_TO_LABELS_CF);
        try (TransactionContext transaction = this.beginReadOnly()) {
            return transaction.get(idToLabelHandle, idBytes);
        } catch (RocksDBException e) {
            throw new RuntimeException("Error accessing RocksDB", e);
        }
    }

    @Override
    public Map<Long, byte[]> labelsForIds(List<Long> ids) {
        this.ensureNotClosed();
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }

        List<Long> validIds = new ArrayList<>();
        List<byte[]> keys = new ArrayList<>();

        ColumnFamilyHandle idToLabelHandle = getHandle(IDS_TO_LABELS_CF);
        List<ColumnFamilyHandle> cfHandles = new ArrayList<>();

        for (Long id : ids) {
            if (id != null && id > 0) {
                validIds.add(id);
                keys.add(longToBytes(id));
                cfHandles.add(idToLabelHandle);
            }
        }

        if (keys.isEmpty()) {
            return Map.of();
        }

        try (TransactionContext transaction = this.beginReadOnly()) {
            // 1) Bulk lookup any known labels
            List<byte[]> results = transaction.multiGetAsList(cfHandles, keys);

            // 2) Build the results map
            Map<Long, byte[]> labels = new HashMap<>();
            for (int i = 0; i < validIds.size(); i++) {
                byte[] labelBytes = results.get(i);
                if (labelBytes != null) {
                    labels.put(validIds.get(i), labelBytes);
                }
            }
            return labels;
        } catch (RocksDBException e) {
            throw new RuntimeException("Error accessing RocksDB", e);
        }
    }

    @Override
    public long labelCount() {
        this.ensureNotClosed();
        try (TransactionContext transaction = this.beginReadOnly()) {
            return transaction.count(this.getHandle(LABELS_TO_IDS_CF));
        }
    }

    @Override
    public void setLabel(byte[] key, long labelId) {
        this.ensureNotClosed();
        if (DictionaryLabelsStore.isInvalidByteSequence(key)) {
            throw new NullPointerException("key cannot be null/empty");
        }

        try (TransactionContext transaction = this.begin()) {
            transaction.put(this.getHandle(KEYS_TO_LABELS_CF), key, longToBytes(labelId));
            transaction.commit();
        } catch (RocksDBException e) {
            throw new RuntimeException("Error writing to RocksDB", e);
        }
    }

    @Override
    public void setLabels(Map<byte[], Long> keysToLabels) {
        this.ensureNotClosed();
        if (MapUtils.isEmpty(keysToLabels)) {
            return;
        }

        try (TransactionContext transaction = this.begin()) {
            for (Map.Entry<byte[], Long> entry : keysToLabels.entrySet()) {
                if (DictionaryLabelsStore.isInvalidByteSequence(entry.getKey()) || entry.getValue() == null) {
                    continue;
                }

                transaction.put(this.getHandle(KEYS_TO_LABELS_CF), entry.getKey(), longToBytes(entry.getValue()));
            }

            transaction.commit();
        } catch (RocksDBException e) {
            throw new RuntimeException("Error writing to RocksDB", e);
        }
    }

    @Override
    public Long getLabel(byte[] key) {
        this.ensureNotClosed();
        if (DictionaryLabelsStore.isInvalidByteSequence(key)) {
            throw new NullPointerException("key cannot be null/empty");
        }

        try (TransactionContext transaction = this.beginReadOnly()) {
            byte[] labelId = transaction.get(this.getHandle(KEYS_TO_LABELS_CF), key);
            return labelId != null ? bytesToLong(labelId) : null;
        } catch (RocksDBException e) {
            throw new RuntimeException("Error accessing RocksDB", e);
        }
    }

    @Override
    public byte[] getLabelAsBytes(byte[] key) {
        this.ensureNotClosed();
        if (DictionaryLabelsStore.isInvalidByteSequence(key)) {
            throw new NullPointerException("key cannot be null/empty");
        }

        try (TransactionContext transaction = this.beginReadOnly()) {
            byte[] labelId = transaction.get(this.getHandle(KEYS_TO_LABELS_CF), key);
            if (labelId == null) {
                return null;
            }
            return transaction.get(this.getHandle(IDS_TO_LABELS_CF), labelId);
        } catch (RocksDBException e) {
            throw new RuntimeException("Error accessing RocksDB", e);
        }
    }

    @Override
    public long keyCount() {
        this.ensureNotClosed();
        try (TransactionContext transaction = this.beginReadOnly()) {
            return transaction.count(this.getHandle(KEYS_TO_LABELS_CF));
        }
    }


    /**
     * Remove a label given its key. For testing purposes.
     * @param key key
     */
    void removeLabel(byte[] key) {
        ensureNotClosed();
        if (DictionaryLabelsStore.isInvalidByteSequence(key)) {
            throw new NullPointerException("key cannot be null/empty");
        }
        try (TransactionContext transaction = begin()) {
            transaction.delete(getHandle(KEYS_TO_LABELS_CF), key);
            transaction.commit();
        } catch (RocksDBException e) {
            throw new RuntimeException("Error deleting from RocksDB", e);
        }
    }

    /**
     * Flushes the column families to disk explicitly blocking until that has occurred
     * <p>
     * This package private method is intended only for testing
     * </p>
     *
     * @throws RocksDBException Thrown if the column families cannot be flushed for any reason
     */
    void flushForTesting() throws RocksDBException {
        this.flush();
    }

}
