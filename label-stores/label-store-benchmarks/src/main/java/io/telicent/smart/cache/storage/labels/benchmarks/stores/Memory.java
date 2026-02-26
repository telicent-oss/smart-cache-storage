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
import io.telicent.smart.cache.storage.labels.MemoryLabelsStore;

/**
 * Tests the non-persistent {@link MemoryLabelsStore} which provides an upper limit for maximum performance
 */
public class Memory implements StoreImplementation{
    @Override
    public LabelsStore newStore() {
        return new MemoryLabelsStore();
    }

    @Override
    public void setup() {

    }

    @Override
    public void teardown() {

    }
}
