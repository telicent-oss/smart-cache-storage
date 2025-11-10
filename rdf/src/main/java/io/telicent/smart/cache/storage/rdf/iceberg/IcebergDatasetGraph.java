/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rdf.iceberg;

import io.telicent.smart.cache.storage.rdf.RdfTermDictionary;
import org.apache.iceberg.*;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.types.Types;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class IcebergDatasetGraph {

    private static final Schema QUADS_SCHEMA
            = new Schema(Types.NestedField.required(1, IcebergFields.GRAPH, Types.LongType.get()),
                         Types.NestedField.required(2, IcebergFields.SUBJECT, Types.LongType.get()),
                         Types.NestedField.required(3, IcebergFields.PREDICATE, Types.LongType.get()),
                         Types.NestedField.required(4, IcebergFields.OBJECT, Types.LongType.get()),
                         Types.NestedField.optional(5, IcebergFields.LABEL, Types.LongType.get()));
    //@formatter:on

    private static final PartitionSpec QUADS_PARTITIONING =
            PartitionSpec.builderFor(QUADS_SCHEMA)
                         .identity(IcebergFields.GRAPH)
                         .identity(IcebergFields.PREDICATE)
                         .identity(IcebergFields.SUBJECT)
                         .identity(IcebergFields.OBJECT)
                         .build();

    private static final TableIdentifier QUADS_ID = TableIdentifier.of("quads");

    private final Catalog catalog;
    private final Table quadsTable;
    private final RdfTermDictionary termDictionary;
    private final AtomicInteger taskCounter = new AtomicInteger(0);
    private IcebergWriteTransaction transaction;

    public IcebergDatasetGraph(RdfTermDictionary termDictionary, Catalog catalog) {
        this.catalog = Objects.requireNonNull(catalog);
        this.termDictionary = Objects.requireNonNull(termDictionary);
        this.quadsTable = this.catalog.createTable(QUADS_ID, QUADS_SCHEMA, QUADS_PARTITIONING);
    }

    public void beginWrite() {
        if (this.transaction != null) {
            throw new IllegalStateException("Already in a write transaction");
        }
        this.transaction = new IcebergWriteTransaction(this.quadsTable, 50 * 1024 * 1024, 1,
                                                       this.taskCounter.incrementAndGet());
    }

    public void commit() {
        if (this.transaction == null) {

        }
    }
}
