/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.benchmarks.states.PerIterationStore;
import org.apache.commons.lang3.RandomUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/**
 * A benchmark that looks at performance of getting IDs for a single very large label (10MB) repeatedly
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
public class VeryLargeLabelBenchmark {

    public static void main(String[] args) {
        BenchmarkUtils.run(VeryLargeLabelBenchmark.class);
    }

    private static final byte[] VERY_LARGE_LABEL;

    static {
        // 10MB label
        VERY_LARGE_LABEL = RandomUtils.insecure().randomBytes(10 * 1024 * 1024);
    }

    @Benchmark
    public void getIdForVeryLargeLabel(PerIterationStore state, Blackhole blackhole) {
        DictionaryLabelsStore store = state.getStore();
        blackhole.consume(store.idForLabel(VERY_LARGE_LABEL));
    }
}
