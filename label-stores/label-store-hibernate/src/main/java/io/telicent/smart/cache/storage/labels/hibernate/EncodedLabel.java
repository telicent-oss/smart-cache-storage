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

@Table(name = "ENCODED_LABELS")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncodedLabel {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NaturalId
    @Column(name = "label", length = 1000)
    private String label;

    public static EncodedLabel of(String label) {
        EncodedLabel storedLabel = new EncodedLabel();
        storedLabel.setLabel(label);
        return storedLabel;
    }

    /**
     * Gets the decoded label
     * @return
     */
    public byte[] getDecodedLabel() {
        return Base64.getDecoder().decode(label);
    }
}
