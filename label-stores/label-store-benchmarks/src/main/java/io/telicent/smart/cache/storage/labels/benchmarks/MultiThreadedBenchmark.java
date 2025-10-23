/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.benchmarks.states.PerBenchmarkStore;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static io.telicent.smart.cache.storage.labels.benchmarks.states.PerBenchmarkStore.ASSIGNED_IDS;
import static io.telicent.smart.cache.storage.labels.benchmarks.states.PerBenchmarkStore.RANDOM_LABELS;

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
    public void writer(PerBenchmarkStore state, Blackhole blackhole) {
        DictionaryLabelsStore store = state.getStore();
        blackhole.consume(store.idForLabel(RANDOM_LABELS.get(this.random.nextInt(RANDOM_LABELS.size()))));
    }

    @Benchmark
    @Group("ReadWrite")
    @GroupThreads(4)
    public void reader(PerBenchmarkStore state, Blackhole blackhole) {
        DictionaryLabelsStore store = state.getStore();
        blackhole.consume(store.labelForId(ASSIGNED_IDS.get(this.random.nextInt(ASSIGNED_IDS.size()))));
    }
}
