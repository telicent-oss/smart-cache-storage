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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Id;
import java.util.Base64;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class EncodedLabel {
    @Id
    @Getter(onMethod_ = @Id)
    @Setter(onMethod_ = @Id)
    private Long id;

    @Getter
    @Setter
    private String label;

    /**
     * Creates a new instance from a Base64 encoded label byte sequence
     *
     * @param label Base64 encoded label byte sequence
     * @return Encoded label
     */
    public static EncodedLabel of(String label) {
        EncodedLabel encodedLabel = new EncodedLabel();
        encodedLabel.setLabel(label);
        return encodedLabel;
    }

    /**
     * Gets the original label byte sequence
     *
     * @return Decoded label byte sequence
     */
    @JsonIgnore
    public byte[] getDecodedLabel() {
        return Base64.getDecoder().decode(this.label);
    }
}
