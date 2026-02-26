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
package io.telicent.smart.cache.storage.labels.benchmarks;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.benchmarks.states.PerBenchmarkStore;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/**
 * A benchmark that looks at read-only performance i.e. what happens when a data store is already populated and merely
 * needs to retrieve labels by their IDs as part of its security filtering
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Warmup(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
public class ReadOnlyBenchmark {

    private int counter = 0;

    public static void main(String[] args) {
        BenchmarkUtils.run(ReadOnlyBenchmark.class);
    }

    @Benchmark
    public void readOnly(PerBenchmarkStore state, Blackhole blackhole) {
        DictionaryLabelsStore store = state.getStore();

        long id = store.idForLabel(
                PerBenchmarkStore.RANDOM_LABELS.get(this.counter++ % PerBenchmarkStore.RANDOM_LABELS.size()));
        blackhole.consume(store.labelForId(id));
    }
}
