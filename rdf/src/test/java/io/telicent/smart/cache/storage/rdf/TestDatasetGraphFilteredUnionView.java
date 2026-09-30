/**
 * Copyright (C) Telicent Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.telicent.smart.cache.storage.rdf;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.DatasetGraphFactory;
import org.apache.jena.sparql.core.DatasetGraphFilteredView;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sys.JenaSystem;
import org.apache.jena.system.Txn;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Predicate;

public class TestDatasetGraphFilteredUnionView {


    static {
        JenaSystem.init();
    }

    public static final Node NG1 = NodeFactory.createURI("https://example.org/graphs/1");
    public static final Node NG2 = NodeFactory.createURI("https://example.org/graphs/2");
    public static final Node NG3 = NodeFactory.createURI("https://example.org/graphs/3");

    public static final Node SUBJECT = NodeFactory.createURI("https://example.org/subject");
    public static final Node PREDICATE = NodeFactory.createURI("https://example.org/predicate");
    protected static final Node LITERAL_DFT = NodeFactory.createLiteralString("graph default");
    protected static final Node LITERAL_NG1 = NodeFactory.createLiteralString("graph 1");
    protected static final Node LITERAL_NG2 = NodeFactory.createLiteralString("graph 2");
    protected static final Node LITERAL_NG3 = NodeFactory.createLiteralString("graph 3");
    protected static final Node UNKNOWN_SUBJECT = NodeFactory.createURI("https://unknown.org");

    public static final Quad QUAD_DFT = Quad.create(Quad.defaultGraphIRI, SUBJECT, PREDICATE, LITERAL_DFT);
    public static final Quad QUAD_NG1 = Quad.create(NG1, SUBJECT, PREDICATE, LITERAL_NG1);
    public static final Quad QUAD_NG2 = Quad.create(NG2, SUBJECT, PREDICATE, LITERAL_NG2);
    public static final Quad QUAD_NG3 = Quad.create(NG3, SUBJECT, PREDICATE, LITERAL_NG3);
    // Intentional duplicate of a quad also in another named graph
    public static final Quad QUAD_NG3_DUPLICATE = Quad.create(NG3, SUBJECT, PREDICATE, LITERAL_NG2);

    private static List<Node> namedGraphs() {
        return List.of(NG1, NG2, NG3);
    }

    private static Predicate<Quad> notUnionGraph() {
        return q -> !Quad.isUnionGraph(q.getGraph());
    }

    /**
     * Adjusts the expected count on the basis that if the {@code g} is a wildcard when used with {@code findNG()} it
     * will ignore the default graph.  Thus, the count may need to be adjusted down by 1, but not below zero, if the
     * query would match the default graph.
     *
     * @param g        Graph to match
     * @param expected Expected count
     * @return {@code findNG()} expected count
     */
    private static int findNGExpected(Node g, Node s, Node p, Node o, DatasetGraph dsg, int expected) {
        return Math.max(0, g == Node.ANY && dsg.contains(Quad.defaultGraphIRI, s, p, o) ? expected - 1 : expected);
    }

    /**
     * Creates our toy test dataset
     *
     * @return Dataset
     */
    private DatasetGraph create() {
        DatasetGraph dsg = DatasetGraphFactory.createTxnMem();
        Txn.executeWrite(dsg, () -> {
            dsg.add(QUAD_DFT);
            dsg.add(QUAD_NG1);
            dsg.add(QUAD_NG2);
            dsg.add(QUAD_NG3);
            dsg.add(QUAD_NG3_DUPLICATE);
        });
        return dsg;
    }

    private static Object[] graphFind(Node graph, int expected) {
        return quadFind(graph, Node.ANY, Node.ANY, Node.ANY, expected);
    }

    private static Object[] quadFind(Quad q) {
        return quadFind(q.getGraph(), q.getSubject(), q.getPredicate(), q.getObject(), 1);
    }

    private static Object[] quadFind(Node g, Node s, Node p, Node o, int expected) {
        return new Object[] { g, s, p, o, expected };
    }

    @DataProvider
    private static Object[][] quadQueries() {
        //@formatter:off
        return new Object[][] {
                // Basic quad matches should always work
                quadFind(QUAD_DFT),
                quadFind(QUAD_NG1),
                quadFind(QUAD_NG2),
                quadFind(QUAD_NG3),
                // Named graph matches should work
                graphFind(NG1, 1),
                graphFind(NG2, 1),
                graphFind(NG3, 2),
                // Full wildcard matches everything
                quadFind(Node.ANY, Node.ANY, Node.ANY, Node.ANY, 5),
                // As union graph suppresses duplicates the duplicate triple that appears in NG3 and NG4 should be
                // suppressed.  Also as the union graph ignores the default graph that won't be returned hence expected
                // count of 3
                graphFind(Quad.unionGraph, 3),
                // Here we're querying all graphs for our predicate, including the default, so all 5 unique quads should
                // be returned
                quadFind(Node.ANY, Node.ANY, PREDICATE, Node.ANY, 5),
                // If we query the union graph by predicate then the duplicate triples should be suppressed and default
                // graph ignores, so again expect count of 3
                quadFind(Quad.unionGraph, Node.ANY, PREDICATE, Node.ANY, 3),
                // Unknown subject matches nothing
                quadFind(Node.ANY, UNKNOWN_SUBJECT, Node.ANY, Node.ANY, 0),
                quadFind(Quad.defaultGraphIRI, UNKNOWN_SUBJECT, Node.ANY, Node.ANY, 0),
                quadFind(Quad.unionGraph, UNKNOWN_SUBJECT, Node.ANY, Node.ANY, 0),
                // Duplicate literal matches multiple quads but only a single triple in the union graph due to
                // de-duplication
                quadFind(Node.ANY, Node.ANY, Node.ANY, LITERAL_NG2, 2),
                quadFind(Quad.unionGraph, Node.ANY, Node.ANY, LITERAL_NG2, 1),
                // Default graph quad only matches in default graph and is ignored for union graph
                quadFind(Quad.defaultGraphIRI, QUAD_DFT.getSubject(), QUAD_DFT.getPredicate(), QUAD_DFT.getObject(), 1),
                quadFind(Quad.unionGraph, QUAD_DFT.getSubject(), QUAD_DFT.getPredicate(), QUAD_DFT.getObject(), 0),
                // Triple matches will include duplicate quads except in union graph
                quadFind(Node.ANY, SUBJECT, PREDICATE, LITERAL_NG2, 2),
                quadFind(Quad.unionGraph, SUBJECT, PREDICATE, LITERAL_NG2, 1),
                quadFind(Quad.defaultGraphIRI, SUBJECT, PREDICATE, LITERAL_NG2, 0),
                };
        //@formatter:on
    }

    @Test(dataProvider = "quadQueries")
    public void givenDataset_whenFilteringWithUnionView_thenQuadFindWorks(Node g, Node s, Node p, Node o,
                                                                          int expected) {
        // Given
        DatasetGraph dsg = create();
        DatasetGraphFilteredUnionView unionView =
                new DatasetGraphFilteredUnionView(dsg, notUnionGraph(), namedGraphs());

        // When and Then
        Assert.assertEquals(IteratorUtils.size(unionView.find(g, s, p, o)), expected);
        Assert.assertEquals(IteratorUtils.size(unionView.find(Quad.create(g, s, p, o))), expected);
        int findNGExpected = findNGExpected(g, s, p, o, dsg, expected);
        Assert.assertEquals(IteratorUtils.size(unionView.findNG(g, s, p, o)), findNGExpected);
        Assert.assertEquals(unionView.stream(g, s, p, o).count(), expected);
    }

    @Test(dataProvider = "quadQueries")
    public void givenDataset_whenFilteringWithUnionView_thenQuadContainsWorks(Node g, Node s, Node p, Node o,
                                                                              int expected) {
        // Given
        DatasetGraph dsg = create();
        DatasetGraphFilteredUnionView unionView =
                new DatasetGraphFilteredUnionView(dsg, notUnionGraph(), namedGraphs());

        // When and Then
        if (expected > 0) {
            Assert.assertTrue(unionView.contains(g, s, p, o));
            Assert.assertTrue(unionView.contains(Quad.create(g, s, p, o)));
        } else {
            Assert.assertFalse(unionView.contains(g, s, p, o));
            Assert.assertFalse(unionView.contains(Quad.create(g, s, p, o)));
        }
    }

    /*
    The following tests illustrate a limitation of the DatasetGraphFilteredView we've been using to apply ABAC filtering

    In its implementation the various find(), stream() and contains() methods DO NOT handle the union graph.  It assumes
    that the query engine correctly calls getUnionGraph() and then calls those methods on that.  When that happens the
    union graph mode works fine because GraphUnionRead ultimately calls the find() methods individually for each named
    graph in the union which means the QuadFilter applies on the original quads and correct permits/denies access.

    However, depending on what other DatasetGraph wrappers are placed around the view, queries may instead just call the
    regular methods for the union graph rather than via getUnionGraph().  In this case the inner dataset likely still
    implements union graph mode BUT due to how they implement this when the quads reach the QuadFilter configured in our
    filtered view their graph field is the union graph name.  As this DOES NOT match our filters expectations of seeing
    the original quads access is thus denied to everything.  This is what the DatasetGraphFilteredUnionView fixes, and
    the preceding tests verify that.

    These tests serve as regression tests so should Apache Jena ever fix the DatasetGraphFilteredView class to properly
    honour union graph then we would know because these tests would start failing.  If that happens then we could
    replace our extension of the class with the plain Jena implementation.
     */

    @Test(dataProvider = "quadQueries")
    public void givenDataset_whenFilteringWithView_thenQuadFindWorksExceptForUnionGraph(Node g, Node s, Node p, Node o,
                                                                                        int expected) {
        // Given
        DatasetGraph dsg = create();
        DatasetGraphFilteredView unionView = new DatasetGraphFilteredView(dsg, notUnionGraph(), namedGraphs());

        // When and Then
        if (Quad.isUnionGraph(g)) {
            // If g field explicitly names the union graph then this doesn't work directly with DatasetGraphFilteredView
            // per discussion comment above
            Assert.assertEquals(IteratorUtils.size(unionView.find(g, s, p, o)), 0);
            Assert.assertEquals(IteratorUtils.size(unionView.find(Quad.create(g, s, p, o))), 0);
            Assert.assertEquals(IteratorUtils.size(unionView.findNG(g, s, p, o)), 0);
            Assert.assertEquals(unionView.stream(g, s, p, o).count(), 0);
        } else {
            Assert.assertEquals(IteratorUtils.size(unionView.find(g, s, p, o)), expected);
            Assert.assertEquals(IteratorUtils.size(unionView.find(Quad.create(g, s, p, o))), expected);
            int findNGExpected = findNGExpected(g, s, p, o, dsg, expected);
            Assert.assertEquals(IteratorUtils.size(unionView.findNG(g, s, p, o)), findNGExpected);
            Assert.assertEquals(unionView.stream(g, s, p, o).count(), expected);
        }
    }

    @Test(dataProvider = "quadQueries")
    public void givenDataset_whenFilteringWithView_thenQuadContainsWorksExceptForUnionGraph(Node g, Node s, Node p,
                                                                                            Node o,
                                                                                            int expected) {
        // Given
        DatasetGraph dsg = create();
        DatasetGraphFilteredView unionView =
                new DatasetGraphFilteredView(dsg, notUnionGraph(), namedGraphs());

        // When and Then
        if (expected > 0 && !Quad.isUnionGraph(g)) {
            // If g field explicitly names the union graph then this doesn't work directly with DatasetGraphFilteredView
            // per discussion comment above
            Assert.assertTrue(unionView.contains(g, s, p, o));
            Assert.assertTrue(unionView.contains(Quad.create(g, s, p, o)));
        } else {
            Assert.assertFalse(unionView.contains(g, s, p, o));
            Assert.assertFalse(unionView.contains(Quad.create(g, s, p, o)));
        }
    }
}
