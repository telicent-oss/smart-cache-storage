/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.benchmarks.states.PerIterationStore;
import org.apache.commons.lang3.RandomUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A basic benchmark that looks at performance of getting IDs for labels from a large pool of unique labels versus
 * getting IDs for a single unique label repeatedly
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
public class BasicBenchmark {

    private int counter = 0;

    public static void main(String[] args) {
        BenchmarkUtils.run(BasicBenchmark.class);
    }

    private static final List<byte[]> RANDOM_LABELS;

    static {
        RANDOM_LABELS = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) {
            RANDOM_LABELS.add(RandomUtils.insecure().randomBytes(50));
        }
    }

    @Benchmark
    public void getIdForRepeatedLabels(PerIterationStore state, Blackhole blackhole) {
        DictionaryLabelsStore store = state.getStore();
        blackhole.consume(store.idForLabel(RANDOM_LABELS.get(this.counter++ % RANDOM_LABELS.size())));
    }


    @Benchmark
    public void getIdForSameLabel(PerIterationStore state, Blackhole blackhole) {
        DictionaryLabelsStore store = state.getStore();
        blackhole.consume(store.idForLabel(RANDOM_LABELS.get(0)));
    }
}
