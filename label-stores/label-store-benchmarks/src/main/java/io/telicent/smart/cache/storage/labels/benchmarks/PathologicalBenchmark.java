/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.benchmarks.states.PerIterationStore;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        try {
            org.openjdk.jmh.Main.main(new String[] { "Pathological*" });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public void getIdForAlwaysUniqueLabel(PerIterationStore state, Blackhole blackhole) {
        DictionaryLabelsStore store = state.getStore();
        byte[] label = RandomUtils.insecure().randomBytes(this.random.nextInt(10, 250));
        store.idForLabel(label);
    }
}
