/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rdf.hibernate;

import io.telicent.smart.cache.storage.rdf.InliningRdfTermDictionary;
import io.telicent.smart.cache.storage.rdf.RdfTermDictionary;

public class TestRdfTermDictionaryH2MemWithInlining extends TestRdfTermDictionaryH2Mem {

    @Override
    protected RdfTermDictionary create() {
        return new InliningRdfTermDictionary(super.create());
    }
}
