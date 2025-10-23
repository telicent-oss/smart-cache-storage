/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks.states;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import org.apache.commons.lang3.RandomUtils;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@State(Scope.Thread)
public class PerBenchmarkStore extends AbstractStoresState {

    public static final List<byte[]> RANDOM_LABELS = new ArrayList<>();
    public static final List<Long> ASSIGNED_IDS = new ArrayList<>();

    static {
        Random random = new Random();

        // Generate the pool of labels
        for (int i = 0; i < 500; i++) {
            RANDOM_LABELS.add(RandomUtils.insecure().randomBytes(random.nextInt(10, 150)));
        }
    }

    @Param({ "Memory", "H2Memory", "H2File", "Postgres", "MongoDB" })
    private String implementation;

    // NB - Testing cache sizes reflect the pool of labels used for benchmarks with this state class
    @Param({ "0", "100", "500"})
    private int cacheSize;

    @Setup(Level.Trial)
    public void setup() {
        setupInternal(this.implementation, this.cacheSize);

        DictionaryLabelsStore store = this.getStore();
        for (byte[] label : RANDOM_LABELS) {
            ASSIGNED_IDS.add(store.idForLabel(label));
        }
    }

    @TearDown(Level.Trial)
    public void teardown() {
        teardownInternal();
    }

}
