/**
 * Copyright (C) 2024-2025 Telicent Limited
 */
package io.telicent.smart.cache.storage.rdf.hibernate;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "RDF_TERMS")
@NamedQueries({
        @NamedQuery(name = "findUri", query = """
                SELECT t
                FROM StoredRdfTerm t
                WHERE 
                  t.nodeType=0 AND 
                  t.lexicalForm=:lexicalForm
                """),
        @NamedQuery(name = "findBlank", query = """
                SELECT t 
                FROM StoredRdfTerm t
                WHERE 
                  t.nodeType=1 AND 
                  t.lexicalForm=:lexicalForm
                """),
        @NamedQuery(name = "findSimpleLiteral", query = """
                SELECT t
                FROM StoredRdfTerm t
                WHERE
                  t.nodeType=2 AND
                  t.lexicalForm=:lexicalForm AND
                  t.datatype IS NULL AND
                  t.language IS NULL
                """),
        @NamedQuery(name = "findLanguageLiteral", query = """
                SELECT t
                FROM StoredRdfTerm t
                WHERE
                  t.nodeType=2 AND
                  t.lexicalForm=:lexicalForm AND
                  t.datatype IS NULL AND
                  t.language=:language
                """),
        @NamedQuery(name = "findDatatypeLiteral", query = """
                SELECT t
                FROM StoredRdfTerm t
                WHERE
                  t.nodeType=2 AND
                  t.lexicalForm=:lexicalForm AND
                  t.datatype=:datatype AND
                  t.language IS NULL
                """),
        @NamedQuery(name = "findTripleTerm", query = """
                SELECT t
                FROM StoredRdfTerm t
                WHERE
                  t.nodeType=3 AND
                  t.subject=:subject AND
                  t.predicate=:predicate AND 
                  t.object=:object
                """)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class StoredRdfTerm {

    public static final byte URI = 0, BLANK = 1, LITERAL = 2, TRIPLE_TERM = 3;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private byte nodeType;

    @Column
    @Lob
    private String lexicalForm;

    @Column(length = 500)
    private String datatype;

    @Column(length = 20)
    private String language;

    @ManyToOne(fetch = FetchType.LAZY)
    private StoredRdfTerm subject, predicate, object;

    public boolean isUri() {
        return this.nodeType == URI;
    }

    public boolean isBlank() {
        return this.nodeType == BLANK;
    }

    public boolean isLiteral() {
        return this.nodeType == LITERAL;
    }

    public boolean isTripleTerm() {
        return this.nodeType == TRIPLE_TERM;
    }

    public boolean hasLanguage() {
        return StringUtils.isNotBlank(this.language);
    }

    public boolean hasDatatype() {
        return StringUtils.isNotBlank(this.datatype);
    }

    public static StoredRdfTerm fromNode(Node node) {
        if (node.isURI()) {
            return StoredRdfTerm.builder().nodeType(URI).lexicalForm(node.getURI()).build();
        } else if (node.isBlank()) {
            return StoredRdfTerm.builder().nodeType(BLANK).lexicalForm(node.getBlankNodeLabel()).build();
        } else if (node.isLiteral()) {
            if (StringUtils.isNotBlank(node.getLiteralLanguage())) {
                return StoredRdfTerm.builder()
                                    .nodeType(LITERAL)
                                    .lexicalForm(node.getLiteralLexicalForm())
                                    .language(node.getLiteralLanguage())
                                    .build();
            } else if (StringUtils.isNotBlank(node.getLiteralDatatypeURI())) {
                return StoredRdfTerm.builder()
                                    .nodeType(LITERAL)
                                    .lexicalForm(node.getLiteralLexicalForm())
                                    .datatype(node.getLiteralDatatypeURI())
                                    .build();
            } else {
                return StoredRdfTerm.builder().nodeType(LITERAL).lexicalForm(node.getLiteralLexicalForm()).build();
            }
        } else if (node.isTripleTerm()) {
            throw new IllegalArgumentException("Can't use this overload for triple terms");
        } else {
            throw new IllegalArgumentException("Can't store unrecognised node types");
        }
    }

    public StoredRdfTerm fromTripleTerm(StoredRdfTerm subject, StoredRdfTerm predicate, StoredRdfTerm object) {
        return StoredRdfTerm.builder()
                            .nodeType(TRIPLE_TERM)
                            .subject(subject)
                            .predicate(predicate)
                            .object(object)
                            .build();
    }

    public Node asNode() {
        switch (this.nodeType) {
            case URI:
                return NodeFactory.createURI(this.lexicalForm);
            case BLANK:
                return NodeFactory.createBlankNode(this.lexicalForm);
            case LITERAL:
                if (this.hasLanguage()) {
                    return NodeFactory.createLiteralLang(this.lexicalForm, this.language);
                } else if (this.hasDatatype()) {
                    return NodeFactory.createLiteralDT(this.lexicalForm,
                                                       TypeMapper.getInstance().getSafeTypeByName(this.datatype));
                } else {
                    return NodeFactory.createLiteralString(this.lexicalForm);
                }
            case TRIPLE_TERM:
                return NodeFactory.createTripleTerm(this.subject.asNode(), this.predicate.asNode(),
                                                    this.object.asNode());
            default:
                throw new IllegalArgumentException(String.format("Unsupported RDF term type: %s", this.nodeType));
        }
    }

    public String getFindQueryName() {
        return switch (this.nodeType) {
            case URI -> "findUri";
            case BLANK -> "findBlank";
            case LITERAL -> {
                if (this.hasLanguage()) {
                    yield "findLanguageLiteral";
                } else if (this.hasDatatype()) {
                    yield "findDatatypeLiteral";
                } else {
                    yield "findSimpleLiteral";
                }
            }
            case TRIPLE_TERM -> "findTripleTerm";
            default ->
                    throw new IllegalArgumentException(String.format("Unsupported RDF term type: %s", this.nodeType));
        };
    }

    public Map<String, Object> asQueryParameters() {
        return switch (this.nodeType) {
            case URI, BLANK -> Map.of("lexicalForm", this.lexicalForm);
            case LITERAL -> {
                if (this.hasLanguage()) {
                    yield Map.of("lexicalForm", this.lexicalForm, "language", this.language);
                } else if (this.hasDatatype()) {
                    yield Map.of("lexicalForm", this.lexicalForm, "datatype", this.datatype);
                } else {
                    yield Map.of("lexicalForm", this.lexicalForm);
                }
            }
            case TRIPLE_TERM -> Map.of("subject", this.subject.asNode(), "predicate", this.predicate.asNode(), "object",
                                       this.object);
            default ->
                    throw new IllegalArgumentException(String.format("Unsupported RDF term type: %s", this.nodeType));
        };
    }
}
