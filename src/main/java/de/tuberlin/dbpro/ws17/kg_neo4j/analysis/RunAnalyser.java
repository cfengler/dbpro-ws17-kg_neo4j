package de.tuberlin.dbpro.ws17.kg_neo4j.analysis;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RunAnalyser {

    public static Map<String, Set<String>> nameToIDMap = new HashMap<>();

    static String pathToDataDir = "H:\\dbpro_dataset\\";

    public static void main(String[] args) {

        AnalysisEntity testEntity = new AnalysisEntity();
        testEntity.name = "Telekom";
        testEntity.equalIds.add("permid_4295870332");


        fillInTestValues();
        AnalyserService analservice = new AnalyserService();

        int preSize = testEntity.equalIds.size();
        int deep = 1;
        do {

            System.out.println("Neuer Durchlauf, es wurden " + (testEntity.equalIds.size() - preSize) + " neue IDs gefunden.");
            System.out.println(testEntity.equalIds + "\n");
            preSize = testEntity.equalIds.size();
            testEntity.equalIds.addAll(analservice.getIdentifiersFromFile(testEntity.equalIds, pathToDataDir));

            deep++;

        } while( testEntity.equalIds.size() > preSize && deep <= 3 );
        System.out.println("Keine neuen IDs gefunden. Suche nach Eigenschaften beginnt.");

        String analysisFile = pathToDataDir + "equalids_" + testEntity.equalIds.iterator().next() + ".txt";

        try {
            FileWriter fileWriter = new FileWriter(analysisFile);
            fileWriter.write(String.join(System.lineSeparator(), testEntity.equalIds));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



        analservice.getLinesContainingEntityIds(pathToDataDir, testEntity.equalIds);


    }

    public static void fillInTestValues() {
        nameToIDMap.put("Lufthansa", new HashSet<String>());
        nameToIDMap.get("Lufthansa").add("permid_4295868979");

    }
}
