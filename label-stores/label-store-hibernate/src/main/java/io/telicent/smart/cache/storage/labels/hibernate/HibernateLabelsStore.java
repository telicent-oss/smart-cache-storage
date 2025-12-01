/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.labels.hibernate;

import io.telicent.smart.cache.storage.hibernate.AbstractHibernateStorage;
import io.telicent.smart.cache.storage.hibernate.TransactionContext;
import io.telicent.smart.cache.storage.hibernate.configuration.HibernateConfiguration;
import io.telicent.smart.cache.storage.hibernate.configuration.JpaConfiguration;
import io.telicent.smart.cache.storage.labels.LabelsStore;
import jakarta.persistence.RollbackException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.hibernate.exception.ConstraintViolationException;

import java.util.*;

public class HibernateLabelsStore extends AbstractHibernateStorage implements LabelsStore {

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
        Objects.requireNonNull(label, "label cannot be null");
        try (TransactionContext context = this.begin()) {
            long id = getOrCreateLabel(label, context);
            context.commit();
            return id;
        } catch (RollbackException e) {
            // This can happen if two threads try to insert the same label at the same time
            if (e.getCause() instanceof ConstraintViolationException cv) {
                // Just recurse since the other thread likely already successfully inserted the label and in a fresh
                // transaction we'll successfully retrieve the label
                return idForLabel(label);
            } else {
                throw e;
            }
        }
    }

    /**
     * Gets or creates a label ID within a transaction context
     *
     * @param label   Label
     * @param context Transaction context
     * @return Label ID
     */
    private long getOrCreateLabel(byte[] label, TransactionContext context) {
        long id;
        String encoded = Base64.getEncoder().encodeToString(label);
        id = this.getOrCreateByNaturalId(context, encoded, EncodedLabel.class, () -> EncodedLabel.of(encoded))
                 .getId();
        return id;
    }

    @Override
    public Map<byte[], Long> idsForLabels(List<byte[]> labels) {
        if (CollectionUtils.isEmpty(labels)) {
            return Collections.emptyMap();
        }

        try (TransactionContext context = this.begin()) {
            Map<byte[], Long> ids = new LinkedHashMap<>();
            for (byte[] label : labels) {
                if (label == null) {
                    continue;
                }
                if (ids.containsKey(label)) {
                    continue;
                }
                ids.put(label, getOrCreateLabel(label, context));
            }

            context.commit();
            return ids;
        } catch (RollbackException e) {
            // This can happen if two threads try to insert the same labels at the same time
            if (e.getCause() instanceof ConstraintViolationException cv) {
                // Just recurse since the other thread likely already successfully inserted the label and in a fresh
                // transaction we'll successfully retrieve the label
                return idsForLabels(labels);
            } else {
                throw e;
            }
        }
    }

    @Override
    public byte[] labelForId(long id) {
        try (TransactionContext context = this.begin()) {
            EncodedLabel encoded = context.getEntityManager().find(EncodedLabel.class, id);
            return encoded != null ? encoded.getDecodedLabel() : null;
        }
    }

    @Override
    public Map<Long, byte[]> labelsForIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }

        try (TransactionContext context = this.begin()) {
            Map<Long, byte[]> map = new LinkedHashMap<>();

            // Bulk lookup
            List<EncodedLabel> existing = this.loadByNamedQuery(context, EncodedLabel.class, "findByIds", Map.of("ids",
                                                                                                                 ids.stream()
                                                                                                                    .filter(Objects::nonNull)
                                                                                                                    .distinct()
                                                                                                                    .toList()));
            for (EncodedLabel l : existing) {
                map.put(l.getId(), l.getDecodedLabel());
            }


            for (Long id : ids) {
                // Ignore null IDs
                if (id == null) {
                    continue;
                }
                // NB - The list might contain duplicate IDs
                if (map.containsKey(id)) {
                    continue;
                }

                EncodedLabel encoded = context.getEntityManager().find(EncodedLabel.class, id);
                map.put(id, encoded != null ? encoded.getDecodedLabel() : null);
            }
            return map;
        }
    }

    @Override
    public long labelSize() {
        try (TransactionContext context = this.begin()) {
            return context.getSession().createQuery("SELECT COUNT(*) FROM EncodedLabel", Long.class).uniqueResult();
        }
    }

    @Override
    public void setLabel(byte[] key, long labelId) {
        ensureNotClosed();
        if (LabelsStore.isInvalidKey(key)) {
            throw new NullPointerException("key cannot be null/empty");
        }

        try (TransactionContext context = this.begin()) {
            String encoded = Base64.getEncoder().encodeToString(key);
            AssignedLabel assignment = this.getOrCreateByNaturalId(context, encoded, AssignedLabel.class,
                                        () -> AssignedLabel.of(encoded, labelId));
            assignment.setLabelId(labelId);
            context.getEntityManager().merge(assignment);
            context.commit();
        }
    }

    @Override
    public void setLabels(Map<byte[], Long> keysToLabels) {
        ensureNotClosed();
        if (MapUtils.isEmpty(keysToLabels)) {
            return;
        }

        try (TransactionContext context = this.begin()) {
            int valid = 0;
            for (Map.Entry<byte[], Long> entry : keysToLabels.entrySet()) {
                if (LabelsStore.isInvalidKey(entry.getKey()) || entry.getValue() == null) {
                    continue;
                }
                valid++;

                String encoded = Base64.getEncoder().encodeToString(entry.getKey());
                AssignedLabel assignment = this.getOrCreateByNaturalId(context, encoded, AssignedLabel.class,
                                            () -> AssignedLabel.of(encoded, entry.getValue()));
                assignment.setLabelId(entry.getValue());
                context.getEntityManager().merge(assignment);
            }

            // Only commit if we've set at least one label assignment
            if (valid > 0) {
                context.commit();
            }
        }
    }

    @Override
    public Long getLabel(byte[] key) {
        this.ensureNotClosed();

        try (TransactionContext context = this.begin()) {
            String encoded = Base64.getEncoder().encodeToString(key);
            AssignedLabel assignment =
                    context.getSession().bySimpleNaturalId(AssignedLabel.class).load(encoded);

            return assignment != null ? assignment.getLabelId() : null;
        }
    }

    @Override
    public byte[] getLabelAsBytes(byte[] key) {
        this.ensureNotClosed();

        try (TransactionContext context = this.begin()) {
            // TODO Could maybe simplify via a Named Query using a JOIN?
            String encoded = Base64.getEncoder().encodeToString(key);
            AssignedLabel assignment =
                    context.getSession().bySimpleNaturalId(AssignedLabel.class).load(encoded);

            if (assignment == null) {
                return null;
            }
            EncodedLabel label = context.getEntityManager().find(EncodedLabel.class, assignment.getLabelId());
            return label != null ? label.getDecodedLabel() : null;
        }
    }

    @Override
    public long keySize() {
        this.ensureNotClosed();

        try (TransactionContext context = this.begin()) {
            return context.getSession().createQuery("SELECT COUNT(*) FROM AssignedLabel", Long.class).uniqueResult();
        }
    }
}
