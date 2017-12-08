package de.tuberlin.dbpro.ws17.kg_neo4j.analysis;

public class RdfEntity_cf {
    public String fileName;
    public String subject;
    public String predicate;
    public String object;
    public String source;

    public RdfEntity_cf(String fileNamePluslineOfFileTabSeperated) {

        String[] split = fileNamePluslineOfFileTabSeperated.split("\t");

        //this.fileName = split[0];
        //this.subject = split[1];
        //this.predicate = split[2];
        //this.object = split[3];
        //this.source = split[4];

        this.subject = split[0];
        this.predicate = split[1];
        this.object = split[2];
        this.source = split[3];
    }

    public String toString() {
        return fileName + "\t" + subject + "\t" + predicate + "\t" + object + "\t" + source;
    }
}