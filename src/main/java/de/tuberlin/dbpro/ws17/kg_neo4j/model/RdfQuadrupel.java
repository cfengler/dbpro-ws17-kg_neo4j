package de.tuberlin.dbpro.ws17.kg_neo4j.model;

public class RdfQuadrupel {

    public String subject;
    public String predicate;
    public String object;
    public String source;

    public RdfQuadrupel(String fileNamePluslineOfFileTabSeperated) {

        String[] split = fileNamePluslineOfFileTabSeperated.split("\t");

        this.subject = split[0];
        this.predicate = split[1];
        this.object = split[2];
        this.source = split[3];
    }

    public String toString() {
        return subject + "\t" + predicate + "\t" + object + "\t" + source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RdfQuadrupel that = (RdfQuadrupel) o;

        if (!subject.equals(that.subject)) return false;
        if (!predicate.equals(that.predicate)) return false;
        if (!object.equals(that.object)) return false;
        return source.equals(that.source);
    }

    @Override
    public int hashCode() {
        int result = subject.hashCode();
        result = 31 * result + predicate.hashCode();
        result = 31 * result + object.hashCode();
        result = 31 * result + source.hashCode();
        return result;
    }
}
