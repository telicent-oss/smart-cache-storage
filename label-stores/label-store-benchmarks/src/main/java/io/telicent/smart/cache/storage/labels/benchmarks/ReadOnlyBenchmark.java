package io.telicent.smart.cache.storage.labels.benchmarks;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.benchmarks.states.PerBenchmarkStore;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Warmup(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
public class ReadOnlyBenchmark {

    private int counter = 0;

    public static void main(String[] args) {
        try {
            org.openjdk.jmh.Main.main(new String[] { "ReadOnly*" });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public void readOnly(PerBenchmarkStore state, Blackhole blackhole) {
        DictionaryLabelsStore store = state.getStore();

        long id = store.idForLabel(
                PerBenchmarkStore.RANDOM_LABELS.get(this.counter++ % PerBenchmarkStore.RANDOM_LABELS.size()));
        blackhole.consume(store.labelForId(id));
    }
}
