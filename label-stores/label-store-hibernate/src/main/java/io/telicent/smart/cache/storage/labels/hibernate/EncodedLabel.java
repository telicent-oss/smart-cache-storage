/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.hibernate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
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

    /**
     * Maximum encoded label size, labels above this size are stored as Blobs instead of Base64 encoded strings
     */
    public static final int MAX_ENCODED_LABEL_SIZE = 1000;
    public static final String HASH_PREFIX = "sha512:";

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NaturalId
    @Column(name = "encodeLabel", length = MAX_ENCODED_LABEL_SIZE, nullable = false)
    private String label;

    @Column(name = "fullLabel")
    @Lob
    private byte[] fullLabel;

    /**
     * Creates a new encoded label
     *
     * @param label Label byte sequence in Base64 encoding (if fits within the max label size OR {@code sha512:<hash>}
     *              for labels that exceed this)
     * @return Encoded label
     */
    public static EncodedLabel of(String label, byte[] rawLabel) {
        EncodedLabel storedLabel = new EncodedLabel();
        storedLabel.setLabel(label);
        if (Strings.CS.startsWith(label, HASH_PREFIX)) {
            storedLabel.setFullLabel(rawLabel);
        }
        return storedLabel;
    }

    /**
     * Gets the decoded label
     *
     * @return Label byte sequence
     */
    public byte[] getDecodedLabel() {
        if (this.fullLabel != null) {
            return this.fullLabel;
        } else {
            return Base64.getDecoder().decode(label);
        }
    }
}
