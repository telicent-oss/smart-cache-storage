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

/**
 * The result of a compact operation.
 */
public class CompactStatus {
    private final long sizeBefore;
    private final long sizeAfter;
    private final long reclaimedBytes;
    private final Instant startTime;
    private final Instant endTime;

    /**
     * Creates a {@code CompactStatus} where reclaimed bytes are derived as the difference
     * between {@code sizeBefore} and {@code sizeAfter}.
     *
     * @param sizeBefore the storage size in bytes before compaction
     * @param sizeAfter  the storage size in bytes after compaction
     * @param startTime  the time at which compaction started
     * @param endTime    the time at which compaction completed
     */
    public CompactStatus(long sizeBefore, long sizeAfter, Instant startTime, Instant endTime) {
        this(sizeBefore, sizeAfter, sizeBefore - sizeAfter, startTime, endTime);
    }

    /**
     * Creates a {@code CompactStatus} with an explicitly provided reclaimed byte count.
     * Use this when the storage implementation tracks reclaimed bytes independently
     * rather than deriving them from the size difference.
     *
     * @param sizeBefore     the storage size in bytes before compaction
     * @param sizeAfter      the storage size in bytes after compaction
     * @param reclaimedBytes the number of bytes reclaimed by the compaction
     * @param startTime      the time at which compaction started
     * @param endTime        the time at which compaction completed
     */
    public CompactStatus(long sizeBefore, long sizeAfter, long reclaimedBytes, Instant startTime, Instant endTime) {
        this.sizeBefore = sizeBefore;
        this.sizeAfter = sizeAfter;
        this.reclaimedBytes = reclaimedBytes;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Returns the storage size in bytes before compaction.
     *
     * @return size in bytes before compaction
     */
    public long getSizeBefore() {
        return sizeBefore;
    }

    /**
     * Returns the storage size in bytes after compaction.
     *
     * @return size in bytes after compaction
     */
    public long getSizeAfter() {
        return sizeAfter;
    }

    /**
     * Returns the number of bytes reclaimed by the compaction. When constructed via
     * {@link #CompactStatus(long, long, Instant, Instant)}, this is derived as
     * {@code sizeBefore - sizeAfter}.
     *
     * @return reclaimed bytes
     */
    public long getReclaimedBytes() {
        return reclaimedBytes;
    }

    /**
     * Returns the time at which the compaction started.
     *
     * @return the start time
     */
    public Instant getStartTime() {
        return startTime;
    }

    /**
     * Returns the time at which the compaction completed.
     *
     * @return the end time
     */
    public Instant getEndTime() {
        return endTime;
    }
}
