package io.telicent.smart.cache.distribution.lifecycle.store.hibernate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppStateId implements Serializable {
    @Column(name = "eventId", nullable = false)
    private UUID eventId;

    @Column(name = "application", nullable = false)
    private String application;
}
