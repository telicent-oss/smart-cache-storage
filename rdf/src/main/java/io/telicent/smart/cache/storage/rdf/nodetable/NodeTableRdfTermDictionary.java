package io.telicent.smart.cache.storage.rdf;

import org.apache.jena.graph.Node;
import org.apache.jena.tdb2.store.NodeId;
import org.apache.jena.tdb2.store.NodeIdFactory;
import org.apache.jena.tdb2.store.nodetable.NodeTable;
import org.apache.jena.tdb2.store.nodetable.NodeTableTRDF;

import java.io.File;

@AllArgsConstructor
public class NodeTableRdfTermDictionary extends InliningRdfTermDictionary {

    private final NodeTableTransactional nodeTable;

    public NodeTableRdfTermDictionary(File directory) {
        this(NodeTableBuilder.build(directory));
    }

    @Override
    protected long allocateNodeId(Node node) {
        return this.nodeTable.calculateWrite(() -> this.nodeTable.getAllocateNodeId(node).getPtrLocation());
    }

    @Override
    protected Node lookup(long id) {
        NodeId nodeId = NodeIdFactory.createPtr(id);
        return this.nodeTable.calculateRead(() -> this.nodeTable.getNodeForNodeId(nodeId));
    }
}
