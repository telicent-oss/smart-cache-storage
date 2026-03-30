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

public class RestoreConfig {
    private final String name;
    private final File backupDir;
    private final Map<String, Object> options;

    private RestoreConfig(Builder builder) {
        this.name = Objects.requireNonNull(builder.name, "Backup name cannot be null");
        this.backupDir = builder.backupDir;
        this.options = new HashMap<>(builder.options);
    }

    public String getName() {
        return name;
    }

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
        private File backupDir;
        private Map<String, Object> options = new HashMap<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder backupDir(File backupDir) {
            this.backupDir = backupDir;
            return this;
        }

        public Builder option(String key, Object value) {
            this.options.put(key, value);
            return this;
        }

        public RestoreConfig build() {
            return new RestoreConfig(this);
        }
    }
}
