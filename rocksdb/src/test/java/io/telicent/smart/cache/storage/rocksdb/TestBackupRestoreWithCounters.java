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
package io.telicent.smart.cache.storage.rocksdb;

import io.telicent.smart.cache.storage.BackupConfig;
import io.telicent.smart.cache.storage.BackupStatus;
import io.telicent.smart.cache.storage.RestoreConfig;
import io.telicent.smart.cache.storage.RestoreStatus;
import io.telicent.smart.cache.storage.rocksdb.counters.AlphabetCounters;
import org.apache.commons.lang3.RandomUtils;
import org.rocksdb.RocksDBException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestBackupRestoreWithCounters extends AbstractRocksDBTests {

    private Map<String, Long> getCounters(AbstractCounterTester tester, List<String> names) throws RocksDBException {
        Map<String, Long> values = new LinkedHashMap<>();

        for (String name : names) {
            values.put(name, tester.get(name));
        }

        return values;
    }

    private Map<String, Long> incrementCounters(AbstractCounterTester tester, List<String> names) throws
            RocksDBException {
        Map<String, Long> values = new LinkedHashMap<>();

        for (String name : names) {
            int increments = RandomUtils.insecure().randomInt(1, 50);
            for (int i = 1; i <= increments; i++) {
                tester.next(name, true);
            }
            values.put(name, tester.get(name));
        }

        return values;
    }

    private void verifyCounterValues(AbstractCounterTester tester, Map<String, Long> expected) {
        for (String name : expected.keySet()) {
            Assert.assertEquals(tester.get(name), expected.get(name));
        }
    }

    private void verifyNotCounterValues(AbstractCounterTester tester, Map<String, Long> unexpected) {
        for (String name : unexpected.keySet()) {
            Assert.assertNotEquals(tester.get(name), unexpected.get(name));
        }
    }

    @Test(invocationCount = 5)
    public void givenStoreWithCounters_whenBackupAndIncrement_thenRestoreReturnsCountersToOriginalValues() throws
            RocksDBException,
            IOException {
        // Given
        File backupDir = Files.createTempDirectory("backups").toFile();
        try (AlphabetCounters counters = new AlphabetCounters(this.dbDir)) {
            Map<String, Long> original = incrementCounters(counters, AlphabetCounters.NAMES);
            verifyCounterValues(counters, original);

            // When
            BackupStatus status =
                    counters.backup(BackupConfig.builder().backupLocation(backupDir.getAbsolutePath()).build());
            Assert.assertTrue(status.isSuccess());
            Map<String, Long> incremented = incrementCounters(counters, AlphabetCounters.NAMES);
            verifyCounterValues(counters, incremented);

            // Then
            RestoreStatus restoreStatus =
                    counters.restore(RestoreConfig.builder().backupLocation(backupDir.getAbsolutePath()).build());
            Assert.assertTrue(restoreStatus.isSuccess());
            verifyCounterValues(counters, original);
            verifyNotCounterValues(counters, incremented);
        }
    }

    @Test
    public void givenStoreWithCounters_whenBackingUp_thenRestoreResetsCountersToInitialValues() throws IOException,
            RocksDBException {
        // Given
        File backupDir = Files.createTempDirectory("backups").toFile();
        try (AlphabetCounters counters = new AlphabetCounters(this.dbDir)) {
            Map<String, Long> initial = getCounters(counters, AlphabetCounters.NAMES);
            initial.values().forEach(v -> Assert.assertEquals(v, RocksDBCounter.INITIAL_ID));

            // When
            BackupStatus status =
                    counters.backup(BackupConfig.builder().backupLocation(backupDir.getAbsolutePath()).build());
            Assert.assertTrue(status.isSuccess());
            Map<String, Long> incremented = incrementCounters(counters, AlphabetCounters.NAMES);
            verifyCounterValues(counters, incremented);
            verifyNotCounterValues(counters, initial);

            // Then
            RestoreStatus restoreStatus = counters.restore(RestoreConfig.builder().backupLocation(
                    backupDir.getAbsolutePath()).build());
            Assert.assertTrue(restoreStatus.isSuccess());
            verifyCounterValues(counters, initial);
            verifyNotCounterValues(counters, incremented);

        }
    }
}
