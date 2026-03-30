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
package io.telicent.smart.cache.storage;

import java.time.Instant;
import java.util.Optional;

public class RestoreStatus {
    private final boolean success;
    private final String backupId;
    private final long bytesRestored;
    private final Instant timestamp;
    private final String errorMessage;

    private RestoreStatus(Builder builder) {
        this.success = builder.success;
        this.backupId = builder.backupId;
        this.bytesRestored = builder.bytesRestored;
        this.timestamp = builder.timestamp != null ? builder.timestamp : Instant.now();
        this.errorMessage = builder.errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getBackupId() {
        return backupId;
    }

    public long getBytesRestored() {
        return bytesRestored;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Optional<String> getErrorMessage() {
        return Optional.ofNullable(errorMessage);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static RestoreStatus success(String backupId, long bytesRestored) {
        return builder()
                .success(true)
                .backupId(backupId)
                .bytesRestored(bytesRestored)
                .build();
    }

    public static RestoreStatus failure(String errorMessage) {
        return builder()
                .success(false)
                .errorMessage(errorMessage)
                .build();
    }

    public static class Builder {
        private boolean success;
        private String backupId;
        private long bytesRestored;
        private Instant timestamp;
        private String errorMessage;

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder backupId(String backupId) {
            this.backupId = backupId;
            return this;
        }

        public Builder bytesRestored(long bytesRestored) {
            this.bytesRestored = bytesRestored;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public RestoreStatus build() {
            return new RestoreStatus(this);
        }
    }
}
