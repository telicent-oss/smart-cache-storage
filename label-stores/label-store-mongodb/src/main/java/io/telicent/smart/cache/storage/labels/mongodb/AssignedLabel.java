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
package io.telicent.smart.cache.storage.labels.mongodb;

import lombok.*;

import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
@EqualsAndHashCode
public class AssignedLabel {
    @Id
    @Getter(onMethod_ = @Id)
    @Setter(onMethod_ = @Id)
    private String id;

    private long labelId;

    /**
     * Creates a new instance from a Base64 encoded label byte sequence
     *
     * @param key     Base64 encoded key byte sequence
     * @param labelId Label ID
     * @return Encoded label
     */
    public static AssignedLabel of(String key, long labelId) {
        AssignedLabel assignment = new AssignedLabel();
        assignment.setId(key);
        assignment.setLabelId(labelId);
        return assignment;
    }
}
