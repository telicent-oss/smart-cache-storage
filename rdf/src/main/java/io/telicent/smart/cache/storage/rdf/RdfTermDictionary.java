/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rdf;

import org.apache.jena.graph.Node;

import java.io.Closeable;

public interface RdfTermDictionary extends Closeable {

    /**
     * Given a Node returns a deterministically allocated unique ID for it
     * <p>
     * Given the same Node more than once <strong>MUST</strong> always yield the same ID
     * </p>
     *
     * @param node Node
     * @return Node ID
     */
    long nodeToId(Node node);

    /**
     * Given a Node ID translates it back into a Node
     *
     * @param id Node ID
     * @return Node
     */
    Node idToNode(long id);

    @Override
    void close();
}
