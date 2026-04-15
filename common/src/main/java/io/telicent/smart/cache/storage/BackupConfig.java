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
 * Backup configuration object.
 * Includes the config name, the backup directory, and storage implementation-specific options.
 */
public class BackupConfig {
    private final String name;
    private final String backupLocation;
    private final Map<String, Object> options;

    private BackupConfig(Builder builder) {
        this.name = builder.name;
        this.backupLocation = builder.backupLocation;
        this.options = new HashMap<>(builder.options);
    }

    /**
     * Returns the name of this backup configuration.
     *
     * @return the configuration name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the directory to which backups will be written.
     *
     * @return the backup location
     */
    public String getBackupLocation() {
        return backupLocation;
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
     * Returns a new {@link Builder} for constructing a {@code BackupConfig}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String backupLocation;
        private Map<String, Object> options = new HashMap<>();

        /**
         * Sets the name of the backup configuration.
         *
         * @param name the configuration name
         * @return this builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the directory to which backups will be written.
         *
         * @param backupLocation the backup location
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
         * Builds and returns the {@link BackupConfig}.
         *
         * @return a new {@code BackupConfig}
         */
        public BackupConfig build() {
            return new BackupConfig(this);
        }
    }
}
