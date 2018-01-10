package de.tuberlin.dbpro.ws17.kg_neo4j.old.analysis;

import de.tuberlin.dbpro.ws17.kg_neo4j.old.model.RdfQuadrupel;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class StatisticsService {
    private static String filePathToAnalyse = "/media/cfengler/MyPassport_1TB/Transformation Christian/predicates_min_5000_entries/rdf-syntax-ns#type/_http___www.w3.org_1999_02_22-rdf-syntax-ns_type_.predicate";
    private static String statisticsFilePath = "/media/cfengler/MyPassport_1TB/Transformation Christian/predicates_min_5000_entries/rdf-syntax-ns#type/objectsWithRdfQuadrupelCount";

    public static void main(String[] args) {
        Map<String, Integer> statistics = getDistinctStringsAndCount();
        saveStatisticsSortedByValue(statistics);
    }

    private static Map<String, Integer> getDistinctStringsAndCount() {
        Map<String, Integer> result = new ConcurrentHashMap<>();

        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePathToAnalyse))) {
                stream.forEach(line -> {
                    RdfQuadrupel rdfQuadrupel = new RdfQuadrupel(line);
                    //TODO: ändere object zu was auch immer untersucht werden soll
                    if (result.containsKey(rdfQuadrupel.object)) {
                        result.put(rdfQuadrupel.object, result.get(rdfQuadrupel.object) + 1);
                    }
                    else {
                        result.put(rdfQuadrupel.object, 1);
                    }
                });
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static void saveStatisticsSortedByValue(Map<String, Integer> statistics) {
        try {
            try (FileWriter fileWriter = new FileWriter(statisticsFilePath)) {
                statistics.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .forEach(e -> {
                        try {
                            fileWriter.write(String.format("%09d", e.getValue()) + "_" + e.getKey() + System.lineSeparator());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
