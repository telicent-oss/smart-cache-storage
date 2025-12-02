/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.hibernate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.Base64;

/**
 * Represents a label byte sequence stored as encoded Base64 in the database
 * <p>
 * Note that while we could in principal use a {@link Lob} typed field this has performance implications because it
 * means that the label wouldn't be stored inline with other row data, and in many database backends it can't have an
 * index built over it which slows down our ability to determine whether we already have an ID assigned for a given
 * label byte sequence.  Therefore, we instead store it as a {@link String} typed field using Base64 encoding to store
 * the label byte sequence, allowing us to index on this field for fast lookups.
 * </p>
 */
@Table(name = "ENCODED_LABELS")
@Entity
@NamedQueries(
        value = {
                @NamedQuery(name = "findByIds", query = """
                        SELECT l FROM EncodedLabel l
                        WHERE
                          l.id IN :ids
                        """)
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncodedLabel {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NaturalId
    @Column(name = "encodeLabel", length = 1000)
    private String label;

    /**
     * Creates a new encoded label
     *
     * @param label Label byte sequence in Base64 encoding
     * @return Encoded label
     */
    public static EncodedLabel of(String label) {
        EncodedLabel storedLabel = new EncodedLabel();
        storedLabel.setLabel(label);
        return storedLabel;
    }

    /**
     * Gets the decoded label
     *
     * @return Label byte sequence
     */
    public byte[] getDecodedLabel() {
        return Base64.getDecoder().decode(label);
    }
}
