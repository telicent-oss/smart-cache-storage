/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.benchmarks.states;

import io.telicent.smart.cache.storage.CachingLabelsStore;
import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;
import io.telicent.smart.cache.storage.labels.benchmarks.stores.StoreImplementation;
import io.telicent.smart.cache.storage.mongodb.cluster.ClusterUtils;

import java.lang.reflect.InvocationTargetException;

public class AbstractStoresState {
    private StoreImplementation storeImpl;
    private DictionaryLabelsStore store;

    static {
        ClusterUtils.SYSOUT_LOGGING_ENABLED = false;
    }

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

    protected void teardownInternal() {
        if (this.store != null) {
            this.store.close();
            this.store = null;
        }
        if (this.storeImpl != null) {
            this.storeImpl.teardown();
        }
    }

    public DictionaryLabelsStore getStore() {
        return this.store;
    }
}
