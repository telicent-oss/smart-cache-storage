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

public class CompactStatus {
    private final long sizeBefore;
    private final long sizeAfter;
    private final long reclaimedBytes;
    private final Instant timestamp;

    public CompactStatus(long sizeBefore, long sizeAfter) {
        this(sizeBefore, sizeAfter, sizeBefore - sizeAfter, Instant.now());
    }

    public CompactStatus(long sizeBefore, long sizeAfter, long reclaimedBytes) {
        this(sizeBefore, sizeAfter, reclaimedBytes, Instant.now());
    }

    public CompactStatus(long sizeBefore, long sizeAfter, long reclaimedBytes, Instant timestamp) {
        this.sizeBefore = sizeBefore;
        this.sizeAfter = sizeAfter;
        this.reclaimedBytes = reclaimedBytes;
        this.timestamp = timestamp;
    }

    public long getSizeBefore() {
        return sizeBefore;
    }

    public long getSizeAfter() {
        return sizeAfter;
    }

    public long getReclaimedBytes() {
        return reclaimedBytes;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
