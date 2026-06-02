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
