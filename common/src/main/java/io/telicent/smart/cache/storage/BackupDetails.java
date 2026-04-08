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
import java.util.Objects;
import java.util.Optional;

/**
 * Metadata about existing backups.
 */
public class BackupDetails {
    private final String name;
    private final String backupId;
    private final Instant startTime;
    private final Instant endTime;
    private final long sizeBytes;

    /**
     * Creates a new {@code BackupDetails} instance.
     *
     * @param name      the human-readable name of the backup, or {@code null} if not available
     * @param backupId  the unique identifier of the backup; must not be {@code null}
     * @param startTime the time at which the backup started, or {@code null} if not recorded
     * @param endTime   the time at which the backup completed, or {@code null} if not recorded
     * @param sizeBytes the total size of the backup in bytes
     */
    public BackupDetails(String name, String backupId, Instant startTime, Instant endTime, long sizeBytes) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.backupId = Objects.requireNonNull(backupId, "Backup ID cannot be null");
        this.sizeBytes = sizeBytes;
    }

    /**
     * Returns the human-readable name of the backup, if available.
     *
     * @return an {@link Optional} containing the name, or empty if not set
     */
    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    /**
     * Returns the unique identifier of the backup.
     *
     * @return the backup ID; never {@code null}
     */
    public String getBackupId() {
        return backupId;
    }

    /**
     * Returns the time at which the backup started, if recorded.
     *
     * @return an {@link Optional} containing the start time, or empty if not recorded
     */
    public Optional<Instant> getStartTime() { return Optional.ofNullable(startTime); }

    /**
     * Returns the time at which the backup completed, if recorded.
     *
     * @return an {@link Optional} containing the end time, or empty if not recorded
     */
    public Optional<Instant> getEndTime() { return Optional.ofNullable(endTime); }

    /**
     * Returns the total size of the backup in bytes.
     *
     * @return the size in bytes
     */
    public long getSizeBytes() {
        return sizeBytes;
    }

    @Override
    public String toString() {
        return String.format("BackupDetails{name=%s, id=%s, start-time=%s, end-time=%s, size=%d}",
                             name, backupId, startTime, endTime, sizeBytes);
    }
}
