/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rdf;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Builder;
import lombok.ToString;
import org.apache.jena.graph.Node;

import java.util.Objects;

/**
 * A caching decorator for RDF Term dictionaries that caches the lookup results independently for both Node to ID, and
 * ID to Node, conversions
 * <p>
 * Intended to reduce access to real storage where unnecessary and thus boost performance.
 * </p>
 */
@ToString(onlyExplicitlyIncluded = true)
public class CachingRdfTermDictionary implements RdfTermDictionary {
    @ToString.Include
    private final RdfTermDictionary delegate;
    private final Cache<Node, Long> nodesToIds;
    private final Cache<Long, Node> idsToNodes;

    @Builder
    public CachingRdfTermDictionary(RdfTermDictionary delegate, int cacheSize) {
        this.delegate = Objects.requireNonNull(delegate);
        if (cacheSize <= 0) {
            throw new IllegalArgumentException("Cache size must be greater than zero");
        }
        this.nodesToIds = Caffeine.newBuilder().initialCapacity(cacheSize / 4).maximumSize(cacheSize).build();
        this.idsToNodes = Caffeine.newBuilder().initialCapacity(cacheSize / 4).maximumSize(cacheSize).build();
    }

    @Override
    public long nodeToId(Node node) {
        return this.nodesToIds.get(node, this.delegate::nodeToId);
    }

    @Override
    public Node idToNode(long id) {
        return this.idsToNodes.get(id, this.delegate::idToNode);
    }

    @Override
    public void close() {

    }
}
