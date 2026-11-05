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

import io.telicent.smart.cache.storage.RestoreException;

/**
 * Thrown when an operation that must close and reopen the underlying RocksDB database, i.e.
 * {@link AbstractRocksDBStorage#restore(io.telicent.smart.cache.storage.RestoreConfig)}, is attempted while one/more
 * transactions remain open on that storage.
 * <p>
 * A RocksDB {@link org.rocksdb.Transaction} is a native object owned by the {@link org.rocksdb.TransactionDB} it was
 * begun against, and it also holds pointers to the shared {@link org.rocksdb.ReadOptions},
 * {@link org.rocksdb.WriteOptions} and {@link org.rocksdb.ColumnFamilyHandle}s of that database.  Closing the database
 * frees all of those, so any subsequent use of the transaction, <strong>including merely committing, rolling back or
 * closing it</strong>, dereferences freed memory and aborts the JVM, typically inside
 * {@code rocksdb::PessimisticTransactionDB::UnLock()}.  As that is a native abort it cannot be caught, so the storage
 * refuses to close the database in the first place and throws this instead.
 * </p>
 * <p>
 * Callers that see this have held a transaction across a restore, e.g. by calling {@code restore()} from inside a
 * longer lived transaction.  The fix is to commit/close the transaction before restoring, never to retry the restore
 * from within the transaction.
 * </p>
 */
public class ActiveTransactionsException extends RestoreException {

    /**
     * Creates a new exception
     *
     * @param message Message
     */
    public ActiveTransactionsException(String message) {
        super(message);
    }
}
