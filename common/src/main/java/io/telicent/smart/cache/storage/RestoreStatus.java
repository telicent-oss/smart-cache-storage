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

import java.util.Optional;

/**
 * The result of a restore operation.
 */
public class RestoreStatus {
    private final boolean success;
    private final String backupId;
    private final long bytesRestored;
    private final String errorMessage;

    private RestoreStatus(Builder builder) {
        this.success = builder.success;
        this.backupId = builder.backupId;
        this.bytesRestored = builder.bytesRestored;
        this.errorMessage = builder.errorMessage;
    }

    /**
     * Returns whether the restore operation completed successfully.
     *
     * @return {@code true} if the restore succeeded, {@code false} otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns the unique identifier of the backup that was restored.
     *
     * @return the backup ID, or {@code null} if not available (e.g. on failure)
     */
    public String getBackupId() {
        return backupId;
    }

    /**
     * Returns the total number of bytes restored.
     *
     * @return bytes restored
     */
    public long getBytesRestored() {
        return bytesRestored;
    }

    /**
     * Returns the error message if the restore failed.
     *
     * @return an {@link Optional} containing the error message, or empty if the restore succeeded
     */
    public Optional<String> getErrorMessage() {
        return Optional.ofNullable(errorMessage);
    }

    /**
     * Returns a new {@link Builder} for constructing a {@code RestoreStatus}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a {@code RestoreStatus} representing a successful restore.
     *
     * @param backupId      the unique identifier of the backup that was restored
     * @param bytesRestored the total number of bytes restored
     * @return a successful {@code RestoreStatus}
     */
    public static RestoreStatus success(String backupId, long bytesRestored) {
        return builder()
                .success(true)
                .backupId(backupId)
                .bytesRestored(bytesRestored)
                .build();
    }

    /**
     * Creates a {@code RestoreStatus} representing a failed restore.
     *
     * @param errorMessage a description of the failure
     * @return a failed {@code RestoreStatus}
     */
    public static RestoreStatus failure(String errorMessage) {
        return builder()
                .success(false)
                .errorMessage(errorMessage)
                .build();
    }

    /**
     * Builder for {@link RestoreStatus}.
     */
    public static class Builder {
        private boolean success;
        private String backupId;
        private long bytesRestored;
        private String errorMessage;

        /**
         * Sets whether the restore succeeded.
         *
         * @param success {@code true} if the restore succeeded
         * @return this builder
         */
        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        /**
         * Sets the unique identifier of the backup that was restored.
         *
         * @param backupId the backup ID
         * @return this builder
         */
        public Builder backupId(String backupId) {
            this.backupId = backupId;
            return this;
        }

        /**
         * Sets the total number of bytes restored.
         *
         * @param bytesRestored bytes restored
         * @return this builder
         */
        public Builder bytesRestored(long bytesRestored) {
            this.bytesRestored = bytesRestored;
            return this;
        }

        /**
         * Sets the error message describing a restore failure.
         *
         * @param errorMessage the error message
         * @return this builder
         */
        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        /**
         * Builds and returns the {@link RestoreStatus}.
         *
         * @return a new {@code RestoreStatus}
         */
        public RestoreStatus build() {
            return new RestoreStatus(this);
        }
    }
}
