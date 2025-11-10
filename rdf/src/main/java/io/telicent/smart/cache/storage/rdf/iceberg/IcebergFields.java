package io.telicent.smart.cache.storage.rdf.iceberg;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IcebergFields {
    protected static final String GRAPH = "graph";
    protected static final String SUBJECT = "subject";
    protected static final String PREDICATE = "predicate";
    protected static final String OBJECT = "object";
    protected static final String LABEL = "label";
}
