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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//TODO
// should there be backupId here too? Should name be optional? What does config even really do?
public class BackupConfig {
    private final String name;
//    private final String backupId;
    private final File backupDir;  // for filesystem-based backups (RocksDB)
    private final Map<String, Object> options;  // for implementation-specific options

    private BackupConfig(Builder builder) {
        //TODO
        // depending on what config does, the name should be nullable and the ID not.
        // is the name the config name, or the backup name, or what?
        this.name = Objects.requireNonNull(builder.name, "Backup name cannot be null");
//        this.backupId = builder.backupId;
        this.backupDir = builder.backupDir;
        this.options = new HashMap<>(builder.options);
    }

    public String getName() {
        return name;
    }

//    public String getBackupId() { return backupId; }

    public File getBackupDir() {
        return backupDir;
    }

    public Map<String, Object> getOptions() {
        return new HashMap<>(options);
    }

    public Object getOption(String key) {
        return options.get(key);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
//        private String backupId;
        private File backupDir;
        private Map<String, Object> options = new HashMap<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

//        public Builder backupId(String backupId) {
//            this.backupId = backupId;
//            return this;
//        }

        /**
         * Sets the backup directory for filesystem-based backups
         */
        public Builder backupDir(File backupDir) {
            this.backupDir = backupDir;
            return this;
        }

        /**
         * Adds an implementation-specific option
         */
        public Builder option(String key, Object value) {
            this.options.put(key, value);
            return this;
        }

        public BackupConfig build() {
            return new BackupConfig(this);
        }
    }
}
