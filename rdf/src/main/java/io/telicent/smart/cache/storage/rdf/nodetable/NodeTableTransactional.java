/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rdf.nodetable;

import org.apache.jena.dboe.transaction.Transactional;
import org.apache.jena.dboe.transaction.txn.TransactionalSystem;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.TxnType;
import org.apache.jena.tdb2.store.nodetable.NodeTable;
import org.apache.jena.tdb2.store.nodetable.NodeTableWrapper;

import java.util.function.Supplier;

public class NodeTableTransactional extends NodeTableWrapper implements Transactional {

    private final TransactionalSystem txnSystem;

    protected NodeTableTransactional(NodeTable nodeTable, TransactionalSystem txnSystem) {
        super(nodeTable);
        this.txnSystem = txnSystem;
    }

    @Override
    public void begin() {
        this.txnSystem.begin();
    }

    @Override
    public void begin(TxnType type) {
        this.txnSystem.begin(type);
    }

    @Override
    public void begin(ReadWrite readWrite) {
        this.txnSystem.begin(readWrite);
    }

    @Override
    public boolean promote() {
        return this.txnSystem.promote();
    }

    @Override
    public boolean promote(Promote mode) {
        return this.txnSystem.promote(mode);
    }

    @Override
    public void commit() {
        this.txnSystem.commit();
    }

    @Override
    public void abort() {
        this.txnSystem.abort();
    }

    @Override
    public void end() {
        this.txnSystem.end();
    }

    @Override
    public ReadWrite transactionMode() {
        return this.txnSystem.transactionMode();
    }

    @Override
    public TxnType transactionType() {
        return this.txnSystem.transactionType();
    }

    @Override
    public boolean isInTransaction() {
        return this.txnSystem.isInTransaction();
    }

    @Override
    public void exec(TxnType txnType, Runnable action) {
        this.txnSystem.exec(txnType, action);
    }

    @Override
    public <T> T calc(TxnType txnType, Supplier<T> action) {
        return this.txnSystem.calc(txnType, action);
    }

    @Override
    public void execute(Runnable r) {
        this.txnSystem.execute(r);
    }

    @Override
    public <X> X calculate(Supplier<X> r) {
        return this.txnSystem.calculate(r);
    }

    @Override
    public <T extends org.apache.jena.sparql.core.Transactional> void executeRead(Runnable r) {
        this.txnSystem.executeRead(r);
    }

    @Override
    public <X> X calculateRead(Supplier<X> r) {
        return this.txnSystem.calculateRead(r);
    }

    @Override
    public void executeWrite(Runnable r) {
        this.txnSystem.executeWrite(r);
    }

    @Override
    public <X> X calculateWrite(Supplier<X> r) {
        return this.txnSystem.calculateWrite(r);
    }
}
