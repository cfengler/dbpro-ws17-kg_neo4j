package de.tuberlin.dbpro.ws17.kg_neo4j.analysis;

public class RdfEntity {
    public String fileName;
    public String subject;
    public String predicate;
    public String object;
    public String source;

    public RdfEntity(String fileNamePluslineOfFileTabSeperated) {

        String[] split = fileNamePluslineOfFileTabSeperated.split("\t");

        this.subject = split[0];
        this.predicate = split[1];
        this.object = split[2];
        this.source = split[3];
    }

    public RdfEntity(String s1, String s2, String s3, String s4) {

    }

    public String toString() {
        return fileName + "\t" + subject + "\t" + predicate + "\t" + object + "\t" + source;
    }
}