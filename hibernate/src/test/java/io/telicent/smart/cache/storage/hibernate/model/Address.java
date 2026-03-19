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
