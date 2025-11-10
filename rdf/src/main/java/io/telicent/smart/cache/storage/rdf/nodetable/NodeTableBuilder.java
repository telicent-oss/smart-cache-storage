/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rdf.nodetable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.apache.jena.dboe.base.file.*;
import org.apache.jena.dboe.base.record.RecordFactory;
import org.apache.jena.dboe.index.Index;
import org.apache.jena.dboe.index.RangeIndex;
import org.apache.jena.dboe.sys.Names;
import org.apache.jena.dboe.trans.bplustree.BPlusTree;
import org.apache.jena.dboe.trans.bplustree.BPlusTreeFactory;
import org.apache.jena.dboe.trans.data.TransBinaryDataFile;
import org.apache.jena.dboe.transaction.txn.*;
import org.apache.jena.dboe.transaction.txn.journal.Journal;
import org.apache.jena.tdb2.params.StoreParams;
import org.apache.jena.tdb2.store.nodetable.NodeTable;
import org.apache.jena.tdb2.store.nodetable.NodeTableCache;
import org.apache.jena.tdb2.store.nodetable.NodeTableInline;
import org.apache.jena.tdb2.store.nodetable.NodeTableTRDF;
import org.apache.jena.tdb2.sys.ComponentIdMgr;
import org.apache.jena.tdb2.sys.SystemTDB;

/**
 * Builds Node Table storage
 */
public class NodeTableBuilder {

    protected static final String NODES = "nodes";
    protected static final String NODES_DATA_FILE = NODES + "-data";

    public static NodeTableTransactional build(File directory) {
        Location location = Location.create(directory.getAbsolutePath());

        // Builder pattern for adding components.
        TransactionCoordinator txnCoordinator = buildTransactionCoordinator(location);
        TransactionalSystem txnSystem = new TransactionalBase(txnCoordinator);

        NodeTableBuilder builder = new NodeTableBuilder(location, new ComponentIdMgr(UUID.randomUUID()));
        NodeTable table = builder.buildNodeTable();

        // Finalize.
        builder.components.forEach(txnCoordinator::add);
        builder.listeners.forEach(txnCoordinator::addListener);
        // Freezes the TransactionCoordinator components
        txnCoordinator.start();

        return new NodeTableTransactional(table, txnSystem);
    }

    private static TransactionCoordinator buildTransactionCoordinator(Location location) {
        Journal journal = Journal.create(location);
        return new TransactionCoordinator(journal);
    }

    // ---- Object starts
    private final Location location;
    private final ComponentIdMgr componentIdMgr;
    // Accumulate TransactionalComponents as they are used to build the database.
    private final Collection<TransactionalComponent> components = new ArrayList<>();
    private final Collection<TransactionListener> listeners = new ArrayList<>();

    private NodeTableBuilder(Location location, ComponentIdMgr componentIdMgr) {
        this.location = location;
        this.componentIdMgr = componentIdMgr;
    }

    private RangeIndex makeRangeIndex(RecordFactory recordFactory) {
        ComponentId cid = componentIdMgr.getComponentId(NodeTableBuilder.NODES);
        FileSet fs = new FileSet(location, NodeTableBuilder.NODES);
        BPlusTree bpt = BPlusTreeFactory.createBPTree(cid, fs, recordFactory);
        components.add(bpt);
        return bpt;
    }

    private NodeTable buildNodeTable() {
        NodeTable nodeTable = buildBaseNodeTable();

        // Add cache to speed up lookups
        nodeTable = addNodeTableCache(nodeTable, StoreParams.getDftStoreParams());
        if (nodeTable instanceof NodeTableCache nodeTableCache) {
            listeners.add(nodeTableCache);
        }

        // Add inlining wrapper so values that can be inlined don't need the underlying node table at all
        nodeTable = NodeTableInline.create(nodeTable);

        return nodeTable;
    }

    private static NodeTable addNodeTableCache(NodeTable nodeTable, StoreParams params) {
        int nodeToIdCacheSize = params.getNode2NodeIdCacheSize();
        int idToNodeCacheSize = params.getNodeId2NodeCacheSize();
        int missCacheSize = params.getNodeMissCacheSize();
        double nodeCacheInitialCapacityFactor = params.getNodeCacheInitialCapacityFactor();
        nodeTable = NodeTableCache.create(nodeTable, nodeToIdCacheSize, idToNodeCacheSize, missCacheSize,
                                          nodeCacheInitialCapacityFactor);
        return nodeTable;
    }

    private NodeTable buildBaseNodeTable() {
        RecordFactory recordFactory = new RecordFactory(SystemTDB.LenNodeHash, SystemTDB.SizeOfNodeId);
        Index index = makeRangeIndex(recordFactory);

        TransBinaryDataFile transBinFile = makeBinaryDataFile();
        components.add(transBinFile);
        return new NodeTableTRDF(index, transBinFile);
    }

    private TransBinaryDataFile makeBinaryDataFile() {
        ComponentId cid = componentIdMgr.getComponentId(NodeTableBuilder.NODES_DATA_FILE);
        FileSet fs = new FileSet(location, NodeTableBuilder.NODES_DATA_FILE);
        BinaryDataFile binFile = FileFactory.createBinaryDataFile(fs, Names.extObjNodeData);
        BufferChannel pState = FileFactory.createBufferChannel(fs, Names.extBdfState);
        // ComponentId mgt.
        return new TransBinaryDataFile(binFile, cid, pState);
    }
}
