/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.mongodb.model;

import lombok.*;

import javax.persistence.Id;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class User {
    @Id
    @Getter(onMethod_ = @Id)
    @Setter(onMethod_ = @Id)
    private String id;

    @Getter
    @Setter
    private String name;
}
