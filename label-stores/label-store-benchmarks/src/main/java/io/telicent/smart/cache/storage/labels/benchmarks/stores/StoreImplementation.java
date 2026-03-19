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
package io.telicent.smart.cache.storage.labels.benchmarks.stores;

import io.telicent.smart.cache.storage.labels.LabelsStore;

/**
 * A simple facade implementation to facilitate easy testing of different label storage backends
 */
public interface StoreImplementation {

    /**
     * Creates a new fresh empty instance of the store
     *
     * @return New fresh empty store
     */
    LabelsStore newStore();

    /**
     * Does any necessary setup for benchmarking e.g. starting/stopping test containers for the storage backend
     */
    void setup();

    /**
     * Does any necessary teardown for benchmarking e.g. stopping/destroying test containers
     */
    void teardown();
}
