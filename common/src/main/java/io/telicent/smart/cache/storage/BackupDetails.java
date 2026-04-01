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

public class BackupDetails {
    private final Optional<String> name;
    private final String backupId;
    private final Instant timestamp;
    private final long sizeBytes;

    public BackupDetails(Optional<String> name, String backupId, Instant timestamp, long sizeBytes) {
        this.name = name;
        this.backupId = Objects.requireNonNull(backupId, "Backup ID cannot be null");
        this.timestamp = timestamp;
        this.sizeBytes = sizeBytes;
    }

    public Optional<String> getName() {
        return name;
    }

    public String getBackupId() {
        return backupId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    @Override
    public String toString() {
        return String.format("BackupDetails{name=%s, id=%s, start-time=%s, end-time=%s, size=%d}",
                             name, backupId, timestamp, sizeBytes);
    }
}
