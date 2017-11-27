package de.tuberlin.dbpro.ws17.kg_neo4j.analysis;

public class RDFEntity {
    public String subject;
    public String predicate;
    public String object;
    public String source;

    public RDFEntity(String subject, String predicate, String object, String source) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
        this.source = source;
    }

    @Override
    public String toString() {
        return subject + " " + predicate + " " + object + " (information from " + source + ")";
    }
}
