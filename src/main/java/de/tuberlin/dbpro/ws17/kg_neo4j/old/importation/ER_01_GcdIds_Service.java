package de.tuberlin.dbpro.ws17.kg_neo4j.old.importation;

import de.tuberlin.dbpro.ws17.kg_neo4j.common.ParseHelper;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class ER_01_GcdIds_Service {

    private static String filePathTypeCompany = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport/ER/01_ER_GcdIds/_http___www.w3.org_1999_02_22-rdf-syntax-ns_type__http___dbpedia.org_ontology_Company_.extraction";
    private static String filePathGcdIds = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport/ER/01_ER_GcdIds/01_ER.data";

    private static String gcdPrefix = "<http://corp.dbpedia.org/resource/gcd_";

    public static void main(String[] args) {
        Set<String> uniqueGcdIds = getGcdIdsFromPredicateFile();

        saveGcdIds(uniqueGcdIds);
    }

    private static Set<String> getGcdIdsFromPredicateFile() {
        Set<String> result = new HashSet<>();

        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePathTypeCompany))) {
                stream.forEach(line -> {
                    if (line.startsWith(gcdPrefix)) {
                        String[] lineSplit = line.split("\t");
                        String gcdId = ParseHelper.getGcdIdFromText(lineSplit[0]);
                        if (gcdId != null) {
                            result.add(gcdId);
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static void saveGcdIds(Set<String> gcdIdsSet) {
        List<String> gcdIds = new ArrayList<>(gcdIdsSet);
        gcdIds.sort(new GcdIdsComparator());

        try {
            try (FileWriter fileWriter = new FileWriter(filePathGcdIds)) {
                fileWriter.write("GcdId" + System.lineSeparator());
                gcdIds.stream().forEach(gcdId -> {
                    try {
                        fileWriter.write(gcdId + System.lineSeparator());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<String> getGcdIds() {
        Set<String> result = new HashSet<>();

        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePathGcdIds))) {
                stream.skip(1).forEach(line -> {
                    result.add(line);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
