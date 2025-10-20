/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.hibernate;

import io.telicent.smart.cache.storage.hibernate.AbstractHibernateStorage;
import io.telicent.smart.cache.storage.hibernate.TransactionContext;
import io.telicent.smart.cache.storage.hibernate.configuration.HibernateConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.JpaConfiguration;
import io.telicent.smart.cache.storage.labels.DictionaryLabelsStore;

import java.util.Base64;
import java.util.Properties;

public class HibernateLabelsStore extends AbstractHibernateStorage implements DictionaryLabelsStore {

    protected static final String PERSISTENCE_UNIT = "hibernate-labels-store";

    /**
     * Creates a new hibernate backed labels store
     *
     * @param dbProperties Database Connection properties, this should contain at least a
     *                     {@value JpaConfiguration#JAKARTA_PERSISTENCE_JDBC_URL} property to provide a JDBC connection
     *                     to the database as well as any other relevant properties e.g.
     *                     {@value JpaConfiguration#JAKARTA_PERSISTENCE_JDBC_USER},
     *                     {@value HibernateConfiguration#HIBERNATE_DIALECT} etc.
     */
    public HibernateLabelsStore(Properties dbProperties) {
        super(dbProperties, PERSISTENCE_UNIT);
    }

    @Override
    public long idForLabel(byte[] label) {
        try (TransactionContext context = this.begin()) {
            long id;
            String encoded = Base64.getEncoder().encodeToString(label);
            id = this.getOrCreateByNaturalId(context, encoded, EncodedLabel.class, () -> EncodedLabel.of(encoded))
                     .getId();
            context.commit();
            return id;
        }
    }

    @Override
    public byte[] labelForId(long id) {
        try (TransactionContext context = this.begin()) {
            EncodedLabel encoded = context.getEntityManager().find(EncodedLabel.class, id);
            return encoded != null ? encoded.getDecodedLabel() : null;
        }
    }
}
