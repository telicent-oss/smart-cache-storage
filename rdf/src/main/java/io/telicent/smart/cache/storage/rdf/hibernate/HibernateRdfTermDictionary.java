/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rdf.hibernate;

import io.telicent.smart.cache.storage.hibernate.AbstractHibernateStorage;
import io.telicent.smart.cache.storage.hibernate.TransactionContext;
import io.telicent.smart.cache.storage.hibernate.configuration.HibernateConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.JpaConfiguration;
import io.telicent.smart.cache.storage.rdf.RdfTermDictionary;
import lombok.ToString;
import org.apache.jena.graph.Node;

import java.util.Properties;

@ToString(onlyExplicitlyIncluded = true)
public class HibernateRdfTermDictionary extends AbstractHibernateStorage implements RdfTermDictionary {
    /**
     * Creates a new hibernate backed store
     *
     * @param dbProperties Database Connection properties, this should contain at least a
     *                     {@value JpaConfiguration#JAKARTA_PERSISTENCE_JDBC_URL} property to provide a JDBC connection
     *                     to the database as well as any other relevant properties e.g.
     *                     {@value JpaConfiguration#JAKARTA_PERSISTENCE_JDBC_USER},
     *                     {@value HibernateConfiguration#HIBERNATE_DIALECT} etc.
     */
    public HibernateRdfTermDictionary(Properties dbProperties) {
        super(dbProperties, "rdf-term-dictionary");
    }

    @Override
    public long nodeToId(Node node) {
        try (TransactionContext context = this.begin()) {
            if (node.isTripleTerm()) {
                throw new IllegalArgumentException("Not yet supported");
            }
            StoredRdfTerm term = StoredRdfTerm.fromNode(node);
            StoredRdfTerm stored = this.getOrCreateByNamedQuery(context, StoredRdfTerm.class, term.getFindQueryName(),
                                                                term.asQueryParameters(), () -> term);
            context.commit();
            return stored.getId();
        }
    }

    @Override
    public Node idToNode(long id) {
        try (TransactionContext context = this.begin()) {
            StoredRdfTerm term = context.getEntityManager().find(StoredRdfTerm.class, id);
            if (term == null) {
                return null;
            } else {
                return term.asNode();
            }
        }
    }
}
