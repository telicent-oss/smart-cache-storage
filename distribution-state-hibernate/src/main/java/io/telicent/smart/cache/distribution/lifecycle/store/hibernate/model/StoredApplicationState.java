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
        @NamedQuery(name = "findAppStateForEvent",
                    query = """
                    SELECT a FROM StoredApplicationState a
                    WHERE a.id.eventId = :eventId AND a.id.application = :application
                    """),
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
