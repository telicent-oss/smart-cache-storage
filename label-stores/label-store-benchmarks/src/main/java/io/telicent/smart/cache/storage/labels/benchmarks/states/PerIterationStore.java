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
package io.telicent.smart.cache.storage.labels.benchmarks.states;

import org.openjdk.jmh.annotations.*;

/**
 * JMH state where an empty store is created for each iteration of a benchmark
 */
@State(Scope.Thread)
public class PerIterationStore extends AbstractStoresState {

    /**
     * Provides the simple class names of the backend implementations to test, see
     * {@link AbstractStoresState#setupInternal(String, int)} for details
     */
    @Param({ "Memory", /*"H2Memory", "H2File",*/ "Postgres", "MongoDB", "RocksDB"/*, "MapDB", "LMDB"*/ })
    private String implementation;

    /**
     * Provides the different cache sizes to test with each backend
     * <p>
     * <strong>NB:</strong> Testing cache sizes should reflect the pool of labels used for benchmarks with this state
     * class
     * </p>
     */
    @Param({ "0", "500", "10000" })
    private int cacheSize;

    @Setup(Level.Iteration)
    public void setup() {
        setupInternal(this.implementation, this.cacheSize);
    }

    @TearDown(Level.Iteration)
    public void teardown() {
        teardownInternal();
    }

}
