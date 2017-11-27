package de.tuberlin.dbpro.ws17.kg_neo4j.analysis;

import java.util.HashMap;
import java.util.Map;

public class RunAnalyser {

    public static Map<String, String> nameToIDMap = new HashMap<>();

    public static void main(String[] args) {

        AnalysisEntity lufthansa = new AnalysisEntity();
        lufthansa.name = "Lufthansa";

        fillInTestValues();
        AnalyserService analservice = new AnalyserService();
        lufthansa.equalIds = analservice.getIdentifiersFromFile(nameToIDMap.get(lufthansa.name), "H:\\dbpro_dataset\\");

        analservice.getLinesContainingEntityIds("H:\\dbpro_dataset\\", lufthansa.equalIds);


    }

    public static void fillInTestValues() {
        nameToIDMap.put("Lufthansa", "permid_4295868979");
    }
}
