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
import org.rocksdb.RocksIterator;

import java.util.function.Consumer;

/**
 * A temporary pointer into RocksDB storage used with the
 * {@link TransactionContext#forEach(ColumnFamilyHandle, Consumer)} method.
 * <p>
 * As noted on the corresponding method this represents a temporary pointer into the RocksDB storage that is valid only
 * for a single invocation of the consumer function.  The {@link #key()} and {@link #value()} methods return temporary
 * pointers to the key value pair that the consumer is currently being invoked upon.  On subsequent invocations the
 * consumer will be passed the same instance of this class <strong>BUT</strong> the {@link #key()} and {@link #value()}
 * will now point to the next key value pair. Therefore, consumer functions <strong>MUST</strong> process the key value
 * pair immediately and <strong>MUST NOT</strong> hold references to the key and/or value beyond a single invocation as
 * they will not remain valid references.
 * </p>
 */
public class KeyValue {

    private final RocksIterator iterator;

    /**
     * Creates a new key value instance backed by the given iterator
     *
     * @param iterator Iterator
     * @return Key value
     */
    public static KeyValue of(RocksIterator iterator) {
        return new KeyValue(iterator);
    }

    /**
     * Creates a new key value pointer
     *
     * @param iterator Iterator
     */
    private KeyValue(RocksIterator iterator) {
        this.iterator = iterator;
    }

    /**
     * The current key, this is a temporary pointer
     *
     * @return Current key
     */
    public byte[] key() {
        return this.iterator.key();
    }

    /**
     * The current value, this is a temporary pointer
     *
     * @return Current value
     */
    public byte[] value() {
        return this.iterator.value();
    }
}
