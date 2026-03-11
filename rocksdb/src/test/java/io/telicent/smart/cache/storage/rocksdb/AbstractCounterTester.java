/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rocksdb;

import org.rocksdb.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class AbstractCounterTester extends AbstractRocksDBStorage {

    public AbstractCounterTester(File dbDir) throws IOException, RocksDBException {
        super(dbDir);
    }

    @Override
    protected List<ColumnFamilyDescriptor> prepareColumnFamilyDescriptors(ColumnFamilyOptions cfOptions) {
        return List.of(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOptions));
    }

    @Override
    protected abstract Map<String, RocksDBCounter> prepareCounters() throws RocksDBException;

    /**
     * Moves the given counter to the next value and returns it
     *
     * @param counterName    Counter name
     * @param useTransaction Whether to increment the counter transactionally
     * @return Next counter value
     * @throws RocksDBException         Thrown if the counter cannot be accessed
     * @throws IllegalArgumentException Thrown if the given counter does not exist
     */
    public long next(String counterName, boolean useTransaction) throws RocksDBException {
        RocksDBCounter counter = this.getCounter(counterName);
        if (counter != null) {
            if (useTransaction) {
                try (TransactionContext context = this.begin()) {
                    long next = counter.next(context);
                    context.commit();
                    return next;
                }
            } else {
                return counter.next();
            }
        } else {
            throw invalidCounter();
        }
    }

    /**
     * Gets the current value of the given counter without increment it
     *
     * @param counterName Counter name
     * @return Current value
     * @throws IllegalArgumentException Thrown if the given counter does not exist
     */
    public long get(String counterName) {
        RocksDBCounter counter = this.getCounter(counterName);
        if (counter != null) {
            return counter.get();
        } else {
            throw invalidCounter();
        }
    }

    /**
     * Gets the current value of the given counter without increment it
     *
     * @param counterName Counter name
     * @return Current value
     * @throws IllegalArgumentException Thrown if the given counter does not exist
     */
    public long get(byte[] counterName) {
        RocksDBCounter counter = this.getCounter(counterName);
        if (counter != null) {
            return counter.get();
        } else {
            throw invalidCounter();
        }
    }

    private static IllegalArgumentException invalidCounter() {
        return new IllegalArgumentException("Not a valid counter");
    }

    /**
     * Synchronise the counter value with the database
     *
     * @param counterName Counter name
     * @throws RocksDBException         Thrown if the counter cannot be sync'd
     * @throws IllegalArgumentException Thrown if the given counter does not exist
     */
    public void sync(String counterName) throws RocksDBException {
        RocksDBCounter counter = this.getCounter(counterName);
        if (counter != null) {
            counter.sync();
        } else {
            throw invalidCounter();
        }
    }

    public long count() {
        try (TransactionContext context = this.begin()) {
            return context.count(getDefaultHandle());
        }
    }

    public boolean isEmpty() {
        try (TransactionContext context = this.begin()) {
            return context.isEmpty(getDefaultHandle());
        }
    }
}
