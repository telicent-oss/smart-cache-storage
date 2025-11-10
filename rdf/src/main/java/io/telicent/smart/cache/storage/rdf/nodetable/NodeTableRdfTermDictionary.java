/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rdf.nodetable;

import io.telicent.smart.cache.storage.AbstractStorage;
import io.telicent.smart.cache.storage.rdf.RdfTermDictionary;
import lombok.AllArgsConstructor;
import org.apache.jena.atlas.RuntimeIOException;
import org.apache.jena.graph.Node;
import org.apache.jena.tdb2.store.NodeId;
import org.apache.jena.tdb2.store.NodeIdFactory;

import java.io.File;

@AllArgsConstructor
public class NodeTableRdfTermDictionary extends AbstractStorage implements RdfTermDictionary {

    private final NodeTableTransactional nodeTable;

    public NodeTableRdfTermDictionary(File directory) {
        this(NodeTableBuilder.build(directory));
    }

    @Override
    public long nodeToId(Node node) {
        NodeId id = this.nodeTable.calculateRead(() -> this.nodeTable.getNodeIdForNode(node));
        if (id != NodeId.NodeDoesNotExist) {
            return id.getPtrLocation();
        }
        return this.nodeTable.calculateWrite(() -> this.nodeTable.getAllocateNodeId(node).getPtrLocation());
    }

    @Override
    public Node idToNode(long id) {
        NodeId nodeId = NodeIdFactory.createPtr(id);
        try {
            return this.nodeTable.calculateRead(() -> this.nodeTable.getNodeForNodeId(nodeId));
        } catch (RuntimeIOException e) {
            // This will be thrown if the provided ID is not in the node table
            return null;
        }
    }

    @Override
    protected void closeInternal() {
        this.nodeTable.close();
    }
}
