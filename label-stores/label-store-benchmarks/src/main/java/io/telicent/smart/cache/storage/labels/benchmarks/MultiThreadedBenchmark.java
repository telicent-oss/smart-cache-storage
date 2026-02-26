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
import io.telicent.smart.cache.storage.labels.benchmarks.states.PerGroupStore;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static io.telicent.smart.cache.storage.labels.benchmarks.states.PerGroupStore.RANDOM_LABELS;

/**
 * A benchmark that looks at assumed "real-world" performance, a relatively small pool of 500 unique labels of varying
 * sizes which are randomly reused across 10,000 items.  Tests both being written into the label store and being read
 * out of the label store.
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Warmup(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
public class MultiThreadedBenchmark {

    public static void main(String[] args) {
        BenchmarkUtils.run(MultiThreadedBenchmark.class);
    }

    private final Random random = new Random();

    @Benchmark
    @Group("ReadWrite")
    @GroupThreads(1)
    public void writer(PerGroupStore state, Blackhole blackhole) {
        DictionaryLabelsStore store = state.getStore();
        blackhole.consume(store.idForLabel(RANDOM_LABELS.get(this.random.nextInt(RANDOM_LABELS.size()))));
    }

    @Benchmark
    @Group("ReadWrite")
    @GroupThreads(4)
    public void reader(PerGroupStore state, Blackhole blackhole) {
        DictionaryLabelsStore store = state.getStore();
        List<Long> assignedIds = state.getAssignedIds();
        blackhole.consume(store.labelForId(assignedIds.get(this.random.nextInt(assignedIds.size()))));
    }

    @Benchmark
    @Group("ReadWrite")
    @GroupThreads(4)
    public void bulkReader(PerGroupStore state, Blackhole blackhole) {
        DictionaryLabelsStore store = state.getStore();
        blackhole.consume(store.labelsForIds(state.getAssignedIds()));
    }
}
