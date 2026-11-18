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

import java.util.ArrayList;
import java.util.List;

/**
 * A tracker for active transactions, used by {@link AbstractRocksDBStorage} and the {@link TransactionContext}
 * implementations to track transactions for safety.  As RocksDB objects are backed by native code if those objects are
 * destroyed and Java code continues to try and use them various crashes can occur.
 */
public class ActiveTransactions {

    private final List<TransactionContext> active = new ArrayList<>();

    /**
     * Current count of active transactions
     *
     * @return Active transactions count
     */
    public synchronized int count() {
        return this.active.size();
    }

    /**
     * Used by a transaction to register itself for tracking
     *
     * @param transaction Transaction
     */
    public synchronized void track(TransactionContext transaction) {
        if (transaction == null) {
            return;
        }
        this.active.add(transaction);
    }

    /**
     * Used by a transaction to release itself from tracking, typically once {@link TransactionContext#commit()} or
     * {@link TransactionContext#close()} have been called
     *
     * @param transaction Transaction
     */
    public synchronized void release(TransactionContext transaction) {
        if (transaction == null) {
            return;
        }
        this.active.remove(transaction);
    }

    /**
     * Invalidates all active transactions by calling {@link TransactionContext#invalidate()} on them
     */
    public synchronized void invalidateAll() {
        // NB - We take an explicit copy of the list before calling forEach() because a TransactionContext's
        //      invalidate() implementation is supposed to call release() which removes the transaction from the list.
        //      So without a copy we'd get a ConcurrentModificationException
        new ArrayList<>(this.active).forEach(TransactionContext::invalidate);
        this.active.clear()
    }
}
