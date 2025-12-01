/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.mongodb;

import lombok.*;

import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
@EqualsAndHashCode
public class AssignedLabel {
    @Id
    @Getter(onMethod_ = @Id)
    @Setter(onMethod_ = @Id)
    private String id;

    private long labelId;

    /**
     * Creates a new instance from a Base64 encoded label byte sequence
     *
     * @param key     Base64 encoded key byte sequence
     * @param labelId Label ID
     * @return Encoded label
     */
    public static AssignedLabel of(String key, long labelId) {
        AssignedLabel assignment = new AssignedLabel();
        assignment.setId(key);
        assignment.setLabelId(labelId);
        return assignment;
    }
}
