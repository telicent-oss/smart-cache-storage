/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rdf;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractRdfTermDictionaryTests {

    protected static final Node SUBJECT = NodeFactory.createURI("https://example.org/subject");

    @BeforeClass
    public void setup() {
        try (RdfTermDictionary dictionary = create()) {
            String printed = dictionary.toString();
            Assert.assertNotNull(printed);
        }
    }

    /**
     * Creates a new fresh instance of an RDF term dictionary
     *
     * @return RDF Term Dictionary
     */
    protected abstract RdfTermDictionary create();

    @Test
    public void givenEmptyDictionary_whenLookingUpId_thenNullReturned() {
        // Given
        try (RdfTermDictionary dictionary = create()) {
            // When
            Node node = dictionary.idToNode(1);

            // Then
            Assert.assertNull(node);
        }
    }

    @Test
    public void givenEmptyDictionary_whenGettingNodeId_thenCanBeRetrieved() {
        // Given
        try (RdfTermDictionary dictionary = create()) {
            // When
            long id = dictionary.nodeToId(SUBJECT);

            // Then
            Node retrieved = dictionary.idToNode(id);
            Assert.assertEquals(retrieved, SUBJECT);
        }
    }

    @DataProvider(name = "nodes")
    public static Object[][] nodes() {
        return new Object[][] {
                { SUBJECT },
                { NodeFactory.createBlankNode("test") },
                { NodeFactory.createBlankNode() },
                { NodeFactory.createLiteralString("a simple string")},
                { NodeFactory.createLiteralLang("bonjour", "fr-fr")},
                { NodeFactory.createLiteralDT("1234", XSDDatatype.XSDinteger)},
                { NodeFactory.createLiteralDT("true", XSDDatatype.XSDboolean)},
                { NodeFactory.createLiteralDT("false", XSDDatatype.XSDboolean)},
                { NodeFactory.createLiteralDT("1.23e4", XSDDatatype.XSDdouble)},
                { NodeFactory.createLiteralDT("NaN", XSDDatatype.XSDdouble)},
        };
    }

    @Test(dataProvider = "nodes", dataProviderClass = AbstractRdfTermDictionaryTests.class)
    public void givenEmptyDictionary_whenGettingNodeIdMoreThanOnce_thenSameIdReturned(Node input) {
        // Given
        try (RdfTermDictionary dictionary = create()) {
            // When
            long a = dictionary.nodeToId(input);
            long b = dictionary.nodeToId(input);

            // Then
            Assert.assertEquals(a, b);
        }
    }

    @DataProvider(name = "sizes")
    public static Object[][] sizes() {
        return new Object[][] {
                { 10 },
                { 100 },
                { 1_000 }
        };
    }

    @Test(dataProvider = "nodes", dataProviderClass = AbstractRdfTermDictionaryTests.class)
    public void givenEmptyDictionary_whenGettingNodeIdManyTimes_thenAlwaysSameIdReturned(Node input) {
        // Given
        try (RdfTermDictionary dictionary = create()) {
            // When
            long id = dictionary.nodeToId(input);
            for (int i = 1; i <= 100; i++) {
                // Then
                Assert.assertEquals(id, dictionary.nodeToId(input));
            }
        }
    }

    @Test(dataProvider = "sizes", dataProviderClass = AbstractRdfTermDictionaryTests.class)
    public void givenEmptyDictionary_whenGettingManyNodeIds_thenAllIdsAreUnique(int size) {
        // Given
        Set<Long> ids = new HashSet<>();
        try (RdfTermDictionary dictionary = create()) {
            // When
            for (int i = 1; i <= size; i++) {
                Node node = NodeFactory.createURI("https://example.org/objects/" + i);
                Assert.assertTrue(ids.add(dictionary.nodeToId(node)), "Each allocated Node ID MUST be unique");
            }

            // Then
            Assert.assertEquals(ids.size(), size);
        }
    }
}
