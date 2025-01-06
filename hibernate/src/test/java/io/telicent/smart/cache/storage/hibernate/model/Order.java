/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.N;

import java.util.List;

@Table(name = "ORDERS")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NaturalId
    @Column(name = "orderId", unique = true, nullable = false)
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billingAdressId")
    private Address billingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shippingAddressId")
    private Address shippingAddress;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "items")
    private List<LineItem> items;
}
