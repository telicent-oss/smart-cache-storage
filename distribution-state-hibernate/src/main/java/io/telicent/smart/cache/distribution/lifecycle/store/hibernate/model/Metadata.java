/**
 * Copyright (C) Telicent Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.telicent.smart.cache.distribution.lifecycle.store.hibernate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

/**
 * Stores arbitrary key-value metadata associated with the state store
 */
@Table(name = "METADATA")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NaturalId
    @Column(name = "metadataKey", nullable = false, length = 100, unique = true)
    private String key;

    @Column(name = "metadataValue", length = 1000)
    private String value;

    /**
     * Creates new metadata for the given key value pair
     *
     * @param key   Key
     * @param value Value
     * @return Metadata
     */
    public static Metadata of(String key, String value) {
        return new Metadata(null, key, value);
    }
}
