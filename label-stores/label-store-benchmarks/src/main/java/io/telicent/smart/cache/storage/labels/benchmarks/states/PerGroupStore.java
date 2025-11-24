/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks.states;

import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * JMH state where the store is shared across a group of threads and has a pool of 500 random labels pre-populated into
 * the store
 */
@State(Scope.Group)
public class PerGroupStore extends AbstractStoresState {

    public static final List<byte[]> RANDOM_LABELS = new ArrayList<>();
    @Getter
    private final List<Long> assignedIds = new ArrayList<>();

    static {
        Random random = new Random();

        // Generate the pool of labels
        for (int i = 0; i < 500; i++) {
            RANDOM_LABELS.add(RandomUtils.insecure().randomBytes(random.nextInt(10, 150)));
        }
    }

    /**
     * Provides the simple class names of the backend implementations to test, see
     * {@link AbstractStoresState#setupInternal(String, int)} for details
     */
    @Param({ "Memory", "H2Memory", "H2File", "Postgres", "MongoDB", "RocksDB", "MapDB", "LMDB" })
    private String implementation;

    /**
     * Provides the different cache sizes to test with each backend
     * <p>
     * <strong>NB:</strong> Testing cache sizes should reflect the pool of labels used for benchmarks with this state
     * class
     * </p>
     */
    @Param({ "0", "100", "500" })
    private int cacheSize;

    @Setup(Level.Trial)
    public void setup() {
        setupInternal(this.implementation, this.cacheSize);

        DictionaryLabelsStore store = this.getStore();
        for (byte[] label : RANDOM_LABELS) {
            this.assignedIds.add(store.idForLabel(label));
        }
    }

    @TearDown(Level.Trial)
    public void teardown() {
        teardownInternal();
    }
}
