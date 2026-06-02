package io.telicent.smart.cache.distribution.lifecycle.store.hibernate.model;

import io.telicent.smart.cache.distribution.lifecycle.DistributionLifecycleState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Table(name = "DISTRIBUTION_STATES")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoredDistributionState {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NaturalId
    @Column(name = "distributionId", nullable = false, length = 500)
    private String distributionId;

    @Column(name = "state", nullable = false)
    private DistributionLifecycleState state;
}
