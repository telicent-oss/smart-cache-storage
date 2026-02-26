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
import io.telicent.smart.cache.storage.labels.benchmarks.states.PerIterationStore;
import org.apache.commons.lang3.RandomUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * A benchmark that looks at pathological case of every single label being unique by generating a sequence of completely
 * random labels
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Warmup(iterations = 1, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 5, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
public class PathologicalBenchmark {

    private final Random random = new Random(334455);

    public static void main(String[] args) {
        BenchmarkUtils.run(PathologicalBenchmark.class);
    }

    @Benchmark
    public void getIdForAlwaysUniqueLabel(PerIterationStore state, Blackhole blackhole) {
        DictionaryLabelsStore store = state.getStore();
        byte[] label = RandomUtils.insecure().randomBytes(this.random.nextInt(10, 250));
        blackhole.consume(store.idForLabel(label));
    }
}
