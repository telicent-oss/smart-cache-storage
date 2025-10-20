package io.telicent.smart.cache.storage.labels.benchmarks.states;

import org.openjdk.jmh.annotations.*;

@State(Scope.Thread)
public class PerIterationStore extends AbstractStoresState {

    @Param({ "Memory", "H2Memory", "H2File", "Postgres", "MongoDB" })
    private String implementation;

    // NB - Cache sizes tested represent no, small and full cache for the pool of labels tested with this state class
    @Param({ "0", "500", "10000"})
    private int cacheSize;

    @Setup(Level.Iteration)
    public void setup() {
        setupInternal(this.implementation, this.cacheSize);
    }

    @TearDown(Level.Iteration)
    public void teardown() {
        teardownInternal();
    }

}
