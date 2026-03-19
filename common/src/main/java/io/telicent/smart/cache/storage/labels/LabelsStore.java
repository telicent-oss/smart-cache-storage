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
package io.telicent.smart.cache.storage.labels;

import java.util.List;
import java.util.Map;

/**
 * Common interface for label stores
 * <p>
 * This extends the low level capabilities of {@link DictionaryLabelsStore} with the ability to associate assigned Label
 * IDs with arbitrary keys.  Details of how the consumer of the API generates and manages these keys are intentionally
 * considered out of scope for this interface.  Therefore, we would expect consumers of this API to wrap this API in
 * higher level APIs that map whatever their actual keys are, e.g. RDF quads, into a suitable byte sequence for use as a
 * key.
 * </p>
 */
public interface LabelsStore extends DictionaryLabelsStore {

    /**
     * Sets the label for the given key
     * <p>
     * For performance reasons we don't expect implementations to validate that the provided Label ID is one previously
     * issued by this store.  If it not then behaviour is undefined and at the consumers risk.  We expect that a
     * consumer will already have translated the label byte sequence to a label ID via the {@link #idForLabel(byte[])}
     * or {@link #idsForLabels(List)} method(s) prior to calling this API.
     * </p>
     *
     * @param key     Key
     * @param labelId Label ID
     * @throws NullPointerException Thrown if the given key was null/empty
     */
    void setLabel(byte[] key, long labelId);

    /**
     * Sets the label for many keys
     * <p>
     * This is a bulk operation intended to allow the underlying implementation to amortize any costs of assigning a
     * label across multiple assignments.  Similar to {@link #setLabel(byte[], long)} we don't expect implementations to
     * validate that the given Label IDs were issued by this store.
     * </p>
     * <p>
     * Any {@code null} keys/values <strong>MUST</strong> be ignored and won't change the state of the labels store.  If
     * the entire map is {@code null} or empty then nothing is changed.
     * </p>
     *
     * @param keysToLabels Map of keys to label IDs
     */
    void setLabels(Map<byte[], Long> keysToLabels);

    /**
     * Gets the label ID associated with the given key
     *
     * @param key Key byte sequence
     * @return Label ID, or {@code null} if invalid key, or no such key in the label store
     */
    Long getLabel(byte[] key);

    /**
     * Gets the label byte sequence associated with the given key
     * <p>
     * As opposed to {@link #getLabel(byte[])} this method both resolves the key to the label ID, and then resolves that
     * Label ID to its label byte sequence.  From the callers perspective this acts as if they had called
     * {@link #getLabel(byte[])} and then passed the return label ID to {@link #labelForId(long)}.  This is primarily a
     * convenience operation
     * </p>
     *
     * @param key Key byte sequence
     * @return Label byte sequence, or {@code null} if invalid key, or the key is not associated with a valid label ID
     * in the label store
     */
    default byte[] getLabelAsBytes(byte[] key) {
        Long id = getLabel(key);
        return id != null ? labelForId(id) : null;
    }

    /**
     * Returns the label store size in terms of number of unique keys mapped to label IDs
     *
     * @return Count of unique keys in the store
     */
    long keyCount();

}
