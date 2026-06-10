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


import java.util.List;

/**
 * Marker interface indicating that a storage implementation supports backup and restore operations.
 * <p>
 * Flexible configuration via {@link BackupConfig} and {@link RestoreConfig}
 * so that different storage implementations are supported
 * </p>
 */
public interface BackupRestoreCapable {

    /**
     * Creates a backup of the storage using the provided configuration
     *
     * @param config backup configuration
     * @return status of the backup operation
     * @throws BackupException if the backup operation fails
     */
    BackupStatus backup(BackupConfig config) throws BackupException;

    /**
     * Restores the storage from a backup using the provided configuration
     *
     * @param config restore configuration
     * @return status of the restore operation
     * @throws RestoreException if the restore operation fails
     */
    RestoreStatus restore(RestoreConfig config) throws RestoreException;

    /**
     * Lists all backups in a directory
     * @param backupLocation backup directory
     * @return a list of all backups in a directory
     * @throws BackupException if list operation fails
     */
    default List<BackupDetails> listBackups(String backupLocation) throws BackupException {
        throw new UnsupportedOperationException("Backup listing not supported by this storage implementation");
    }

    /**
     * Deletes a backup given its backupId
     * @param backupLocation backup directory
     * @param backupId ID of the backup
     * @throws BackupException if delete operation fails
     */
    default void deleteBackup(String backupLocation, String backupId) throws BackupException {
        throw new UnsupportedOperationException("Backup deletion not supported by this storage implementation");
    }
}
