/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

import java.util.Map;

@Table(name = "JSON_DATA")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonHolder {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "uuid", unique = true, length = 36)
    @NaturalId
    private String uuid;

    @Type(JsonType.class)
    @Column(nullable = false, name = "data", columnDefinition = "json")
    private Map<String, Object> json;
}
