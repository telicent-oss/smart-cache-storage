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

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.TransactionDB;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class TestInducedFailures extends AbstractRocksDBTests {

    @Test(expectedExceptions = RocksDBException.class, expectedExceptionsMessageRegExp = "failed")
    public void givenOpenDbFails_whenCreatingStorage_thenFails() throws RocksDBException, IOException {
        // Given
        try (MockedStatic<TransactionDB> mock = Mockito.mockStatic(TransactionDB.class)) {
            mock.when(() -> TransactionDB.open(any(), any(), any(), any(), any()))
                .thenThrow(new RocksDBException("failed"));

            try (Nestable nestable = new Nestable(this.dbDir)) {
                Assert.fail("Should throw error during constructor");
            }
        }
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Failed to close.*")
    public void givenCloseDbFails_whenClosingStorage_thenFails() throws RocksDBException, IOException {
        // Given
        try (MockedStatic<TransactionDB> mock = Mockito.mockStatic(TransactionDB.class)) {
            TransactionDB db = Mockito.mock(TransactionDB.class);
            doThrow(new RocksDBException("failed")).when(db).close();
            mock.when(() -> TransactionDB.open(any(), any(), any(), any(), any())).thenAnswer(
                    (Answer<TransactionDB>) invocationOnMock -> {
                        List<ColumnFamilyHandle> handles = invocationOnMock.getArgument(4);
                        ColumnFamilyHandle handle = Mockito.mock(ColumnFamilyHandle.class);
                        when(handle.getName()).thenReturn(RocksDB.DEFAULT_COLUMN_FAMILY);
                        handles.add(handle);
                        return db;
                    });

            // When and Then
            try (Nestable nestable = new Nestable(this.dbDir)) {
                nestable.close();
            }
        }
    }
}
