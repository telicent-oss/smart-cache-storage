/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.mongodb.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.*;

import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class SavedData {

    @Getter(onMethod_ = @Id)
    @Setter(onMethod_ = @Id)
    private String id;

    @Getter
    @Setter
    private String name, user;

    @Builder.Default
    private Map<String, Object> data = new HashMap<>();

    @JsonAnySetter
    public void putData(String key, Object value) {
        this.data.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getData() {
        return this.data;
    }
}
