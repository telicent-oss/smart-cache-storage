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

import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDBException;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestTransactionContextDefaults {

    @Test
    public void givenContextWithoutUntrackedSupport_whenPuttingUntracked_thenDelegatesToPut() throws
            RocksDBException {
        // Given
        TransactionContext context = mock(TransactionContext.class);
        doCallRealMethod().when(context).putUntracked(any(), any(), any());
        ColumnFamilyHandle handle = mock(ColumnFamilyHandle.class);
        byte[] key = "key".getBytes();
        byte[] value = "value".getBytes();

        // When
        context.putUntracked(handle, key, value);

        // Then
        verify(context, times(1)).put(handle, key, value);
    }
}
