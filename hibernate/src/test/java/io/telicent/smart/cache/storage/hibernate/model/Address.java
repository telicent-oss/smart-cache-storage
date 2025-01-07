/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.hibernate.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "ADDRESSES")
@NamedQueries(
        {
                @NamedQuery(name = "findByPostalCode", query = """
SELECT a FROM Address a WHERE a.postalCode = :postalCode
"""),
                @NamedQuery(name = "findByDetails", query = """
SELECT a FROM Address a
WHERE
  a.recipient = :recipient AND
  a.nameOrNumber = :nameOrNumber AND
  a.street = :street AND
  a.city = :city AND
  a.postalCode = :postalCode
"""),
                @NamedQuery(name = "findByStreet", query = """
SELECT a FROM Address a WHERE a.street = :street
""")
        }
)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "recipient")
    private String recipient;

    @Column(name = "nameOrNumber")
    private String nameOrNumber;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "postalCode")
    private String postalCode;
}
