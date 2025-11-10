/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rdf.memory;

import io.telicent.smart.cache.storage.rdf.InliningRdfTermDictionary;
import io.telicent.smart.cache.storage.rdf.RdfTermDictionary;

public class TestRdfTermDictionaryMemoryWithInlining extends TestRdfTermDictionaryMemory {
    @Override
    protected RdfTermDictionary create() {
        return new InliningRdfTermDictionary(super.create());
    }
}
