package io.telicent.smart.cache.storage.labels.benchmarks;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.benchmarks.states.PerIterationStore;
import org.apache.commons.lang3.RandomUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * A toy benchmark purely to check that the store implementations to be tested are all available and working
 */
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Warmup(iterations = 1, batchSize = 1)
@Measurement(iterations = 1, batchSize = 1)
public class ToyBenchmark {

    public static void main(String[] args) {
        try {
            org.openjdk.jmh.Main.main(new String[] { "Toy*"});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final byte[] RANDOM_LABEL = RandomUtils.insecure().randomBytes(50);

    @Benchmark
    public void getIdForUniqueLabel_toy(PerIterationStore state, Blackhole blackhole) {
        DictionaryLabelsStore store = state.getStore();
        blackhole.consume(store.idForLabel(RANDOM_LABEL));
    }
}
