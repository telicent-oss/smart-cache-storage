/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels;

import java.io.Closeable;
import java.util.List;
import java.util.Map;

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
 * <p>
 * Anyone implementing this interface should ensure that they use the abstract tests provided in the {@code tests}
 * classifier module that accompanies this API to validate their implementation conforms to the API contract fully.
 * </p>
 */
public interface DictionaryLabelsStore extends Closeable {

    /**
     * Checks whether the given byte sequence is considered invalid
     *
     * @param sequence Byte sequence
     * @return True if the byte sequence is invalid i.e. it is {@code null} or is empty (has zero length), false if a
     * valid sequence
     */
    static boolean isInvalidByteSequence(byte[] sequence) {
        return sequence == null || sequence.length == 0;
    }

    /**
     * Given a label byte sequence provide the unique ID for that label
     * <p>
     * If this is a previously seen label byte sequence then an implementation <strong>MUST</strong> always return the
     * pre-existing unique ID assigned to that label.
     * </p>
     * <p>
     * If the given label is {@code null} or empty (has zero length) then a {@link NullPointerException}
     * <strong>MUST</strong> be raised.
     * </p>
     *
     * @param label Label byte sequence
     * @return Label ID
     * @throws NullPointerException Thrown if the label is {@code null} or empty (has zero length)
     */
    long idForLabel(byte[] label);

    /**
     * Given a list of label byte sequences return the unique IDs for all the unique provided labels
     * <p>
     * This is intended as a bulk operation, by providing multiple labels in a single request the intent is that
     * implementations can amortize any overheads of a single lookup across the many lookups.
     * </p>
     * <p>
     * Note that any {@code null} or empty (zero length) labels <strong>MUST</strong> be ignored by the bulk
     * implementation and have no corresponding ID in the returned IDs list.
     * </p>
     *
     * @param labels Labels
     * @return Label IDs
     */
    Map<byte[], Long> idsForLabels(List<byte[]> labels);

    /**
     * Given a Label ID returns the label byte sequence associated with it
     *
     * @param id Label ID
     * @return Label byte sequence, or {@code null} if not a valid Label ID for the store
     */
    byte[] labelForId(long id);

    /**
     * Given a list of Label IDs return the label byte sequences associated with those
     * <p>
     * Note that any {@code null} IDs passed in the IDs list <strong>MUST</strong> be ignored and not included in the
     * returned map.
     * </p>
     *
     * @param ids Label IDs
     * @return Label byte sequences, if the value for an entry is {@code null} then no label with the given ID exists
     */
    Map<Long, byte[]> labelsForIds(List<Long> ids);

    /**
     * Returns the label store size in terms of number of unique label IDs assigned
     *
     * @return Count of unique label IDs in the store
     */
    long labelCount();

    /**
     * Closes the labels store releasing any resources it might be holding
     */
    void close();
}
