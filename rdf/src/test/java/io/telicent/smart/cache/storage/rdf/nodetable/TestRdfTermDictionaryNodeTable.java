/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rdf;

import io.telicent.smart.cache.storage.rdf.nodetable.NodeTableRdfTermDictionary;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestRdfTermDictionaryNodeTable extends AbstractRdfTermDictionaryTests {
    @Override
    protected RdfTermDictionary create() {
        try {
            File dir = Files.createTempDirectory("rdf-terms").toFile();
            return new NodeTableRdfTermDictionary(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
