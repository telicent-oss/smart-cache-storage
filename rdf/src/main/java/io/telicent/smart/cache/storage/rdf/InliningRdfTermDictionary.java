/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rdf;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.apache.jena.atlas.lib.BitsLong;
import org.apache.jena.graph.Node;
import org.apache.jena.tdb2.store.NodeId;
import org.apache.jena.tdb2.store.NodeIdFactory;
import org.apache.jena.tdb2.store.value.DoubleNode62;

/**
 * A decorator for RDF Term dictionaries that adds inlining support using {@link NodeId#inline(Node)}
 * <p>
 * Inlining support means that some common literal types, e.g. boolean, numerics etc., are not stored in the underlying
 * dictionary and instead are given special IDs that denote they are inlined.  This avoids more costly storage lookups
 * for very commonly used datatypes, and ensures that we
 * </p>
 */
@AllArgsConstructor
@ToString
public final class InliningRdfTermDictionary implements RdfTermDictionary {
    private final RdfTermDictionary delegate;

    @Override
    public long nodeToId(Node node) {
        NodeId inline = NodeId.inline(node);
        if (inline != null) {
            return encode(inline);
        }
        return this.delegate.nodeToId(node);
    }

    @Override
    public void close() {
        this.delegate.close();
    }

    @Override
    public Node idToNode(long id) {
        NodeId nodeId = NodeIdFactory.createPtr(id);
        if (NodeId.isInline(nodeId)) {
            return NodeId.extract(nodeId);
        }
        return this.delegate.idToNode(id);
    }

    public static long encode(NodeId nodeId) {
        long x = nodeId.getPtrLocation();
        switch (nodeId.type()) {
            case PTR:
                return x;
            case XSD_DOUBLE:
                // XSD_DOUBLE is special.
                // Set value bit (63) and bit 62
                x = DoubleNode62.insertType(x);
                return x;
            default:
                // Bit 62 is zero - tag is for doubles.
                x = BitsLong.pack(x, nodeId.getTypeValue(), 56, 62);
                // Set the high, value bit.
                x = BitsLong.set(x, 63);
                return x;
        }
    }
}
