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
import java.util.List;

public interface BackupRestoreCapable {
    BackupStatus backup(BackupConfig config) throws BackupException;
    RestoreStatus restore(RestoreConfig config) throws RestoreException;
    default List<BackupDetails> listBackups(File backupDir) throws BackupException {
        throw new UnsupportedOperationException("Backup listing not supported by this storage implementation");
    }
    default void deleteBackup(File backupDir, String backupId) throws BackupException {
        throw new UnsupportedOperationException("Backup deletion not supported by this storage implementation");
    }
}
