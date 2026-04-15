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

import java.util.HashMap;
import java.util.Map;

/**
 * Restore configuration object.
 * Includes the id of the backup to be restored, the backup directory, and storage implementation-specific options.
 */
public class RestoreConfig {
    private final String backupId;
    private final String backupLocation;
    private final Map<String, Object> options;

    private RestoreConfig(Builder builder) {
        this.backupLocation = builder.backupLocation;
        this.options = new HashMap<>(builder.options);
        this.backupId = builder.backupId;
    }

    /**
     * Returns the directory from which backups will be restored.
     *
     * @return the backup directory
     */
    public String getBackupLocation() {
        return backupLocation;
    }

    /**
     * Returns the id of the backup to be restored.
     *
     * @return the backup id
     */
    public String getBackupId() {
        return backupId;
    }

    /**
     * Returns a copy of all storage implementation-specific options.
     *
     * @return a new {@link Map} containing all options; never {@code null}
     */
    public Map<String, Object> getOptions() {
        return new HashMap<>(options);
    }

    /**
     * Returns the value of a single storage implementation-specific option.
     *
     * @param key the option key
     * @return the option value, or {@code null} if not set
     */
    public Object getOption(String key) {
        return options.get(key);
    }

    /**
     * Returns a new {@link RestoreConfig.Builder} for constructing a {@code RestoreConfig}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String backupId;
        private String backupLocation;
        private Map<String, Object> options = new HashMap<>();

        /**
         * Sets the id of the backup the data will be restored from.
         *
         * @param backupId the backup id
         * @return this builder
         */
        public Builder backupId(String backupId) {
            this.backupId = backupId;
            return this;
        }

        /**
         * Sets the directory from which backups will be restored.
         *
         * @param backupLocation the backup directory
         * @return this builder
         */
        public Builder backupLocation(String backupLocation) {
            this.backupLocation = backupLocation;
            return this;
        }

        /**
         * Adds a storage implementation-specific option.
         *
         * @param key   the option key
         * @param value the option value
         * @return this builder
         */
        public Builder option(String key, Object value) {
            this.options.put(key, value);
            return this;
        }

        /**
         * Builds and returns the {@link RestoreConfig}.
         *
         * @return a new {@code RestoreConfig}
         */
        public RestoreConfig build() {
            return new RestoreConfig(this);
        }
    }
}
