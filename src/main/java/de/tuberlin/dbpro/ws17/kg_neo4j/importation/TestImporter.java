package de.tuberlin.dbpro.ws17.kg_neo4j.importation;

public class TestImporter {
    public static void main(String[] args) {
        SmartDataWebImporter importer = new SmartDataWebImporter();
        importer.Connect();
        importer.createData();
        importer.getAllLabels();
        importer.Disconnect();
    }
}
