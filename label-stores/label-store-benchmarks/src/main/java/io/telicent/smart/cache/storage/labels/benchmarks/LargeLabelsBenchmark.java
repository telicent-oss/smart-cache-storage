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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A benchmark that looks at performance of getting IDs for labels from a large pool of large labels (where each label
 * is 10k in size)
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
public class LargeLabelsBenchmark {

    private int counter = 0;

    public static void main(String[] args) {
        BenchmarkUtils.run(LargeLabelsBenchmark.class);
    }

    private static final List<byte[]> RANDOM_LABELS;

    static {
        RANDOM_LABELS = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) {
            RANDOM_LABELS.add(RandomUtils.insecure().randomBytes(10 * 1024));
        }
    }

    @Benchmark
    public void getIdForLargeLabel(PerIterationStore state, Blackhole blackhole) {
        DictionaryLabelsStore store = state.getStore();
        blackhole.consume(store.idForLabel(RANDOM_LABELS.get(this.counter++ % RANDOM_LABELS.size())));
    }

}
