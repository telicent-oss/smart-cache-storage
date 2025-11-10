/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rdf.memory;

import io.telicent.smart.cache.storage.rdf.RdfTermDictionary;
import org.apache.jena.graph.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryRdfTermDictionary implements RdfTermDictionary {
    private final Map<Node, Long> nodesToIds = new HashMap<Node, Long>();
    private final Map<Long, Node> idsToNodes = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    @Override
    public long nodeToId(Node node) {
        long id = this.nodesToIds.computeIfAbsent(node, k -> this.idCounter.incrementAndGet());
        this.idsToNodes.computeIfAbsent(id, k -> node);
        return id;
    }

    @Override
    public Node idToNode(long id) {
        return this.idsToNodes.get(id);
    }

    @Override
    public void close() {
        this.nodesToIds.clear();
        this.idsToNodes.clear();
    }
}
