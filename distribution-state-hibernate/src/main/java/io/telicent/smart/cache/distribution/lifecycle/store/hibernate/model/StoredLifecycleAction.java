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

import java.util.UUID;

@Table(name = "LIFECYCLE_ACTIONS")
@Entity
@NamedQueries({
        @NamedQuery(name = "activeEvents",
        query = """
                SELECT a FROM StoredLifecycleAction a
                LEFT JOIN StoredApplicationState s ON a.eventId=s.id.eventId
                WHERE (s.state IS NULL OR s.state != Completed)
                """)
})
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

    @Type(JsonType.class)
    @Column(nullable = false, name = "action", columnDefinition = "json")
    private LifecycleAction action;
}
