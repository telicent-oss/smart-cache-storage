/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate.model;

import jakarta.persistence.*;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.math.BigDecimal;

@Table(name = "PRODUCTS")
@NamedQueries(
        {
                @NamedQuery(name = "inStock", query = """
                        SELECT p FROM Product p WHERE p.available>0
                        """),
                @NamedQuery(name = "outOfStock", query = """
                        SELECT p FROM Product p WHERE p.available=0
                        """)
        }
)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NaturalId
    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "available")
    private long available;
}
