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

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.DatasetGraphFilteredView;
import org.apache.jena.sparql.core.Quad;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * An extension to the {@link DatasetGraphFilteredView} wrapper provided by Jena that ensures various methods properly
 * honour the union graph
 */
public class DatasetGraphFilteredUnionView extends DatasetGraphFilteredView {
    /**
     * Creates a new filtered view
     *
     * @param dsg           Base dataset to filter
     * @param filter        Quad filter (optional)
     * @param visibleGraphs Visible graphs for named graph access, union graph etc.
     */
    public DatasetGraphFilteredUnionView(DatasetGraph dsg,
                                         Predicate<Quad> filter,
                                         Collection<Node> visibleGraphs) {
        super(dsg, filter, visibleGraphs);
    }

    @Override
    public Iterator<Quad> find(Node g, Node s, Node p, Node o) {
        if (Quad.isUnionGraph(g)) {
            return getUnionGraph().find(s, p, o).mapWith(asUnionGraphQuad());
        }
        return super.find(g, s, p, o);
    }

    private static Function<Triple, Quad> asUnionGraphQuad() {
        return t -> Quad.create(Quad.unionGraph, t);
    }

    @Override
    public Iterator<Quad> find(Quad quad) {
        if (Quad.isUnionGraph(quad.getGraph())) {
            return getUnionGraph().find(quad.asTriple()).mapWith(asUnionGraphQuad());
        }
        return super.find(quad);
    }

    @Override
    public boolean contains(Node g, Node s, Node p, Node o) {
        if (Quad.isUnionGraph(g)) {
            return getUnionGraph().contains(s, p, o);
        }
        return super.contains(g, s, p, o);
    }

    @Override
    public boolean contains(Quad quad) {
        if (Quad.isUnionGraph(quad.getGraph())) {
            return getUnionGraph().contains(quad.asTriple());
        }
        return super.contains(quad);
    }

    @Override
    public Iterator<Quad> findNG(Node g, Node s, Node p, Node o) {
        if (Quad.isUnionGraph(g)) {
            return getUnionGraph().find(s, p, o).mapWith(asUnionGraphQuad());
        }
        return super.findNG(g, s, p, o);
    }

    @Override
    public Stream<Quad> stream(Node g, Node s, Node p, Node o) {
        if (Quad.isUnionGraph(g)) {
            return getUnionGraph().stream(s, p, o).map(asUnionGraphQuad());
        }
        return super.stream(g, s, p, o);
    }
}
