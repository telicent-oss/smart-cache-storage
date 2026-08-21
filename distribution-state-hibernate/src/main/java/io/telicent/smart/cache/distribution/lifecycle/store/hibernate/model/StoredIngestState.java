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

import io.hypersistence.utils.hibernate.type.json.JsonType;
import io.telicent.smart.cache.distribution.lifecycle.events.utils.PartitionOffsets;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Table(name = "LIFECYCLE_INGEST_STATES")
@Entity
//@formatter:off
@NamedQuery(name = "findForApplication",
            query = """
            SELECT i FROM StoredIngestState i
            WHERE i.id.application = :application
            """)
//@formatter:on
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoredIngestState {

    @EmbeddedId
    private IngestStateId id;

    @Type(JsonType.class)
    @Column(nullable = false, name = "offsets", columnDefinition = "json")
    private PartitionOffsets offsets;
}
