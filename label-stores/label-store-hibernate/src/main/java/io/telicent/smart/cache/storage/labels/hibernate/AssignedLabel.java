/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.hibernate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

/**
 * Represents an assignment from a key to a label ID
 * <p>
 * Similar to {@link EncodedLabel} we don't store the key byte sequence as a {@link Lob} field but instead as a Base64
 * encoded {@link String} as this allows us to index on that field and do efficent key to label ID lookups.
 * </p>
 * <p>
 * Another key design choice is that we don't make the {@code labelId} field a foreign key to the {@link EncodedLabel}
 * table.  This is done both to satisfy the
 * {@link io.telicent.smart.cache.storage.labels.LabelsStore#setLabel(byte[], long)} contract which states that an
 * implementation need not verify that the provided IDs are valid, and for performance as it avoids the database having
 * to make extra constraint checks on label assignment insert/update.
 * </p>
 */
@Table(name = "ASSIGNED_LABELS")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignedLabel {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    // NB - Some database backends treat 'key' as a reserved word so avoid naming the column exactly that!
    @NaturalId
    @Column(name = "encodedKey", length = 1000)
    private String key;

    // NB - Intentionally not making this an explicit foreign key to EncodedLabel for performance reasons
    @Column(name = "labelId", nullable = false)
    private long labelId;

    /**
     * Creates a new label assignment
     *
     * @param key     Key byte sequence encoded as Base64 string
     * @param labelId Label ID
     * @return Label assignment
     */
    public static AssignedLabel of(String key, long labelId) {
        AssignedLabel assignment = new AssignedLabel();
        assignment.setKey(key);
        assignment.setLabelId(labelId);
        return assignment;
    }
}
