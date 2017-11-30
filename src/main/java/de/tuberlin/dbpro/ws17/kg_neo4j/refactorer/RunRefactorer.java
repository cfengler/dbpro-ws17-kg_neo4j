package de.tuberlin.dbpro.ws17.kg_neo4j.refactorer;

public class RunRefactorer {

    static String pathToDataDir = "H:\\dbpro_dataset\\";

    public static void main(String[] args) {
        RefactorerService rs = new RefactorerService();
        rs.createSameAsFile(pathToDataDir);
    }
}
