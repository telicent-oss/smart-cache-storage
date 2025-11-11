/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks.states;

import io.telicent.smart.cache.storage.labels.CachingLabelsStore;
import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.benchmarks.stores.StoreImplementation;
import io.telicent.smart.cache.storage.mongodb.cluster.ClusterUtils;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;

/**
 * Abstract JMH benchmarking state which holds a single instance of a {@link DictionaryLabelsStore} that is setup and
 * torn down as appropriate for the benchmark design
 */
public class AbstractStoresState {
    private StoreImplementation storeImpl;
    @Getter
    private DictionaryLabelsStore store;

    static {
        ClusterUtils.SYSOUT_LOGGING_ENABLED = false;
    }

    /**
     * Given the name of a store implementation (the simple name of a class in the {@code stores} package e.g.
     * {@code Memory}) dynamically creates a new instance of that class and uses that to set up, create and tear down
     * instance of {@link DictionaryLabelsStore} as needed.  Also, if a positive cache size is specified wraps the
     * underlying store with the {@link CachingLabelsStore} decorator.
     *
     * @param impl      Simple implementation class
     * @param cacheSize Cache size, if positive then {@link CachingLabelsStore} wraps the underlying store
     */
    protected void setupInternal(String impl, int cacheSize) {
        if (this.storeImpl == null) {
            try {
                Class<?> storeImpl =
                        Class.forName(
                                "io.telicent.smart.cache.storage.labels.benchmarks.stores." + impl);
                this.storeImpl = (StoreImplementation) storeImpl.getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException e) {
                System.err.println("Failed to find store implementation " + impl);
                throw new RuntimeException("Failed to find store implementation " + impl, e);
            } catch (InstantiationException | IllegalAccessException |
                     InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        this.storeImpl.setup();
        this.store = this.storeImpl.newStore();

        if (cacheSize > 0) {
            this.store = new CachingLabelsStore(this.store, cacheSize);
        }
    }

    /**
     * Tears down the store
     */
    protected void teardownInternal() {
        if (this.store != null) {
            this.store.close();
            this.store = null;
        }
        if (this.storeImpl != null) {
            this.storeImpl.teardown();
        }
    }

}
