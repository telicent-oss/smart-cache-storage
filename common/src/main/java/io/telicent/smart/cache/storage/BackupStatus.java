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

//TODO
// treat it a bit like details?
// maybe change it to details? Do I really need status?
public class BackupStatus {
    private final boolean success;
    private final String backupId;
    private final long bytesBackedUp;
    private final Instant timestamp;
    private final String errorMessage;

    private BackupStatus(Builder builder) {
        this.success = builder.success;
        this.backupId = builder.backupId;
        this.bytesBackedUp = builder.bytesBackedUp;
        this.timestamp = builder.timestamp != null ? builder.timestamp : Instant.now();
        this.errorMessage = builder.errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getBackupId() {
        return backupId;
    }

    public long getBytesBackedUp() {
        return bytesBackedUp;
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

    public static BackupStatus success(String backupId, long bytesBackedUp) {
        return builder()
                .success(true)
                .backupId(backupId)
                .bytesBackedUp(bytesBackedUp)
                .build();
    }

    public static BackupStatus failure(String errorMessage) {
        return builder()
                .success(false)
                .errorMessage(errorMessage)
                .build();
    }

    public static class Builder {
        private boolean success;
        private String backupId;
        private long bytesBackedUp;
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

        public Builder bytesBackedUp(long bytesBackedUp) {
            this.bytesBackedUp = bytesBackedUp;
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

        public BackupStatus build() {
            return new BackupStatus(this);
        }
    }
}
