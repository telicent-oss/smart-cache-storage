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

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/**
 * The result of a backup operation.
 */
public class BackupStatus {
    private final boolean success;
    private final String backupId;
    private final long bytesBackedUp;
    private final Instant startTime;
    private final Instant endTime;
    private final String errorMessage;

    private BackupStatus(Builder builder) {
        this.success = builder.success;
        this.backupId = builder.backupId;
        this.bytesBackedUp = builder.bytesBackedUp;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime != null ? builder.endTime : Instant.now();
        this.errorMessage = builder.errorMessage;
    }

    /**
     * Returns whether the backup operation completed successfully.
     *
     * @return {@code true} if the backup succeeded, {@code false} otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns the unique identifier of the backup.
     *
     * @return the backup ID, or {@code null} if not available (e.g. on failure)
     */
    public String getBackupId() {
        return backupId;
    }

    /**
     * Returns the total number of bytes backed up.
     *
     * @return bytes backed up
     */
    public long getBytesBackedUp() {
        return bytesBackedUp;
    }

    /**
     * Returns the time at which the backup operation started.
     *
     * @return the start time, or {@code null} if not recorded
     */
    public Instant getStartTime() { return startTime; }

    /**
     * Returns the time at which the backup operation ended.
     *
     * @return the end time, or {@code null} if not recorded
     */
    public Instant getEndTime() { return endTime; }

    /**
     * Returns the duration of the backup operation.
     * <p>
     * Returns {@link Duration#ZERO} if either the start or end time is unavailable.
     *
     * @return the duration of the backup
     */
    public Duration getDuration() {
        if (startTime != null && endTime != null) {
            return Duration.between(startTime, endTime);
        }
        return Duration.ZERO;
    }

    /**
     * Returns the error message if the backup failed.
     *
     * @return an {@link Optional} containing the error message, or empty if the backup succeeded
     */
    public Optional<String> getErrorMessage() {
        return Optional.ofNullable(errorMessage);
    }

    /**
     * Returns a new {@link Builder} for constructing a {@code BackupStatus}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a {@code BackupStatus} representing a successful backup.
     *
     * @param backupId      the unique identifier of the backup
     * @param bytesBackedUp the total number of bytes backed up
     * @param startTime     the time the backup started
     * @param endTime       the time the backup completed
     * @return a successful {@code BackupStatus}
     */
    public static BackupStatus success(String backupId, long bytesBackedUp, Instant startTime, Instant endTime) {
        return builder()
                .success(true)
                .backupId(backupId)
                .bytesBackedUp(bytesBackedUp)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    /**
     * Creates a {@code BackupStatus} representing a failed backup.
     *
     * @param errorMessage a description of the failure
     * @return a failed {@code BackupStatus}
     */
    public static BackupStatus failure(String errorMessage) {
        return builder()
                .success(false)
                .errorMessage(errorMessage)
                .build();
    }

    /**
     * Builder for {@link BackupStatus}.
     */
    public static class Builder {
        private boolean success;
        private String backupId;
        private long bytesBackedUp;
        private Instant startTime;
        private Instant endTime;
        private String errorMessage;

        /**
         * Sets whether the backup succeeded.
         *
         * @param success {@code true} if the backup succeeded
         * @return this builder
         */
        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        /**
         * Sets the unique identifier of the backup.
         *
         * @param backupId the backup ID
         * @return this builder
         */
        public Builder backupId(String backupId) {
            this.backupId = backupId;
            return this;
        }

        /**
         * Sets the total number of bytes backed up.
         *
         * @param bytesBackedUp bytes backed up
         * @return this builder
         */
        public Builder bytesBackedUp(long bytesBackedUp) {
            this.bytesBackedUp = bytesBackedUp;
            return this;
        }

        /**
         * Sets the time at which the backup started.
         *
         * @param startTime the start time
         * @return this builder
         */
        public Builder startTime(Instant startTime) {
            this.startTime = startTime;
            return this;
        }

        /**
         * Sets the time at which the backup ended.
         *
         * @param endTime the end time
         * @return this builder
         */
        public Builder endTime(Instant endTime) {
            this.endTime = endTime;
            return this;
        }

        /**
         * Sets the error message describing a backup failure.
         *
         * @param errorMessage the error message
         * @return this builder
         */
        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        /**
         * Builds and returns the {@link BackupStatus}.
         *
         * @return a new {@code BackupStatus}
         */
        public BackupStatus build() {
            return new BackupStatus(this);
        }
    }
}
