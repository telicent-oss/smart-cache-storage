package io.telicent.smart.cache.storage.labels.benchmarks;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.benchmarks.states.PerIterationStore;
import org.apache.commons.lang3.RandomUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
public class RealWorldBenchmark {

    public static void main(String[] args) {
        try {
            org.openjdk.jmh.Main.main(new String[] { "RealWorld*" });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final List<byte[]> RANDOM_LABELS;

    static {
        RANDOM_LABELS = new ArrayList<>();
        Random random = new Random();

        // Generate the pool of repeated labels
        List<byte[]> pool = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            pool.add(RandomUtils.insecure().randomBytes(random.nextInt(10, 150)));
        }

        // Generate the actual list of repeated labels
        for (int i = 0; i < 10_000; i++) {
            RANDOM_LABELS.add(pool.get(random.nextInt(pool.size())));
        }
    }

    private int counter = 0;

    @Setup(Level.Iteration)
    public void setup() {
        this.counter = 0;
    }

    private void nextLabelToStore(Blackhole blackhole, DictionaryLabelsStore store) {
        blackhole.consume(store.idForLabel(RANDOM_LABELS.get(this.counter++ % RANDOM_LABELS.size())));
    }

    @Benchmark
    public void getIdForLabel(PerIterationStore state, Blackhole blackhole) {
        DictionaryLabelsStore store = state.getStore();
        nextLabelToStore(blackhole, store);
    }

    @Benchmark
    public void getIdForLabel_andResolveIdToLabel(PerIterationStore state, Blackhole blackhole) {
        DictionaryLabelsStore store = state.getStore();
        byte[] label = RANDOM_LABELS.get(this.counter++ % RANDOM_LABELS.size());
        long id = store.idForLabel(label);
        blackhole.consume(store.labelForId(id));
    }
}
