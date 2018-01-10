package de.tuberlin.dbpro.ws17.kg_neo4j.old.analysis.statistics;

import de.tuberlin.dbpro.ws17.kg_neo4j.old.model.RdfQuadrupel;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class ObjectStatisticsService {

    /*
    Man kann eine RDF-Datei angeben.
    Dann werden alle Objekte mit der aufgetretenen Anzahl in eine Map gespeichert.
    Am Ende wird die Map, sortiert nach der Anzahlen in eine Datei gespeichert.
     */

    private static String filePathPredicate = "/media/cfengler/Kingston DT HyperX 3.0/Predicates/_http___www.w3.org_1999_02_22-rdf-syntax-ns_type_.predicate";
    private static String filePathStatistics = null;

    public static void main(String[] args) {
        filePathStatistics = filePathPredicate.replace(".predicate", ".statistics");

        Map<String, Integer> statistics = getDistinctObjectsAndCount();
        saveStatisticsSortedByValue(statistics);
    }

    private static Map<String, Integer> getDistinctObjectsAndCount() {
        Map<String, Integer> result = new ConcurrentHashMap<>();

        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePathPredicate))) {
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
            try (FileWriter fileWriter = new FileWriter(filePathStatistics)) {
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
