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
import io.telicent.smart.cache.distribution.lifecycle.events.LifecycleAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

@Table(name = "LIFECYCLE_ACTIONS")
@Entity
@NamedQuery(name = "activeEvents",
query = """
        SELECT a FROM StoredLifecycleAction a
        LEFT JOIN StoredApplicationState s ON a.eventId=s.id.eventId
        WHERE (s.state IS NULL OR s.state != Completed)
        """)
@NamedQuery(name = "distributionEvents",
query = """
        SELECT a FROM StoredLifecycleAction a
        WHERE a.distributionId=:distributionId
        ORDER BY a.lastUpdated ASC, a.id ASC
        """)
@NamedQuery(name = "latestDistributionEvent",
query = """
        SELECT a FROM StoredLifecycleAction a
        WHERE a.distributionId=:distributionId
        ORDER BY a.lastUpdated DESC, a.id DESC
        LIMIT 1
        """
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoredLifecycleAction {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NaturalId
    @Column(name = "eventId")
    private UUID eventId;

    @Column(name = "distributionId", nullable = false, length = 500)
    private String distributionId;

    @Column(name = "lastUpdated", nullable = false)
    private Instant lastUpdated;

    @Type(JsonType.class)
    @Column(nullable = false, name = "action", columnDefinition = "json")
    private LifecycleAction action;
}
