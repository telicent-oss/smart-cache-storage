/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.hibernate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

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

    public static AssignedLabel of(String key, long labelId) {
        AssignedLabel assignment = new AssignedLabel();
        assignment.setKey(key);
        assignment.setLabelId(labelId);
        return assignment;
    }
}
