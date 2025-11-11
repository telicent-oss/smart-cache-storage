/**
 * Copyright (C) 2024-2025 Telicent Limited
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
