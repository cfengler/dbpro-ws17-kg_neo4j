package de.tuberlin.dbpro.ws17.kg_neo4j.transformator;

public class RunTransformator {

    static String pathToDataDir = "H:\\dbpro_dataset\\";

    public static void main(String[] args) {
        TransformatorService rs = new TransformatorService();
        //rs.createSameAsFile(pathToDataDir);
        rs.createListOfAllAvailableObjects(pathToDataDir);
    }
}
