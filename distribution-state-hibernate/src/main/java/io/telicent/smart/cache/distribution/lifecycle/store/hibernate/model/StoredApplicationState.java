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

import io.telicent.smart.cache.distribution.lifecycle.ApplicationState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "LIFECYCLE_APPLICATION_STATES", uniqueConstraints = {
        @UniqueConstraint(name = "eventAndAppConstraint", columnNames = {
                "eventId", "application"
        })
})
@Entity
//@formatter:off
@NamedQueries({
        @NamedQuery(name = "findForEvent",
                    query = """
                    SELECT a FROM StoredApplicationState a
                    WHERE a.id.eventId = :eventId
                    """)
})
//@formatter:on
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoredApplicationState {

    @EmbeddedId
    private AppStateId id;

    @Column(name = "state")
    private ApplicationState state;
}
