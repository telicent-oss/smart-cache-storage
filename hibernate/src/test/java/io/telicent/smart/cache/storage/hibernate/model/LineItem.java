/**
 * Copyright (C) 2022 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "LINE_ITEMS")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineItem {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private int quantity;
}
