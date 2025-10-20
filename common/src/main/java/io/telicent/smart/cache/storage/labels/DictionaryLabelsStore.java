/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels;

import java.io.Closeable;

/**
 * Common interface for dictionary label stores.
 * <p>
 * These are stores that provide a dictionary encoding for labels allowing a short unique ID for a label to be stored
 * alongside the data to which it applies to, and that ID resolved back to the original label as required.
 * </p>
 * <p>
 * A store <strong>MUST</strong> satisfy the following constraints:
 * </p>
 * <ul>
 *     <li>It is consistent, i.e. given the same label it <strong>MUST</strong> always return the same ID</li>
 *     <li>It is <strong>thread-safe</strong>, i.e. multiple threads <strong>MUST</strong> be able to safely obtain IDs for labels</li>
 * </ul>
 */
public interface DictionaryLabelsStore extends Closeable {

    /**
     * Given a label byte sequence provide the unique ID for that label
     * <p>
     * If this is a previously seen label byte sequence then an implementation <strong>MUST</strong> always return the
     * pre-existing unique ID assigned to that label.
     * </p>
     *
     * @param label Label byte sequence
     * @return Label ID
     */
    long idForLabel(byte[] label);

    /**
     * Given a Label ID returns the label byte sequence associated with it
     *
     * @param id Label ID
     * @return Label byte sequence, or {@code null} if not a valid Label ID for the store
     */
    byte[] labelForId(long id);

    /**
     * Closes the labels store releasing any resources it might be holding
     */
    void close();
}
