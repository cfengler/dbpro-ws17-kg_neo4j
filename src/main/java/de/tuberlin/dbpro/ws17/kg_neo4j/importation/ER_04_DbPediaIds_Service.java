package de.tuberlin.dbpro.ws17.kg_neo4j.importation;

import de.tuberlin.dbpro.ws17.kg_neo4j.common.ParseHelper;
import de.tuberlin.dbpro.ws17.kg_neo4j.old.model.EquivalenceRelation;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class ER_04_DbPediaIds_Service {

    private static String filePathSameAs = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport/ER/04_ER_DbPediaIds/_http___www.w3.org_2002_07_owl_sameAs_.predicate";
    private static String filePath_04_ER = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport/ER/04_ER_DbPediaIds/04_ER.data";

    private static String gcdIdPrefix = "<http://corp.dbpedia.org/resource/gcd_";
    private static String deDbPediaPrefix = "<http://de.dbpedia.org/resource/";
    private static String dbPediaPrefix = "<http://dbpedia.org/resource/";

    public static void main(String[] args) {
        Map<String, EquivalenceRelation> ers = ER_03_PermIds_Service.getERsByGcdIds();

        addDeDbPediaIdsToErs(ers);
        addDbPediaIdsToErs(ers);

        saveErs(ers);
    }

    private static void addDeDbPediaIdsToErs(Map<String, EquivalenceRelation> ers) {
        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePathSameAs))) {
                stream.forEach(line -> {
                    String[] lineSplit = line.split("\t");

                    if (lineSplit[0].startsWith(gcdIdPrefix) && lineSplit[2].startsWith(deDbPediaPrefix)) {
                        String gcdId = ParseHelper.getGcdIdFromText(lineSplit[0]);
                        String deDbPediaId = ParseHelper.getDeDbPediaIdFromText(lineSplit[2]);

                        if (gcdId != null && deDbPediaId != null) {
                            if (ers.containsKey(gcdId)) {
                                ers.get(gcdId).deDbPediaIds.add(deDbPediaId);
                            }
                        }
                    }

                });
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addDbPediaIdsToErs(Map<String, EquivalenceRelation> ers) {
        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePathSameAs))) {
                stream.forEach(line -> {
                    String[] lineSplit = line.split("\t");

                    if (lineSplit[0].startsWith(gcdIdPrefix) && lineSplit[2].startsWith(dbPediaPrefix)) {
                        String gcdId = ParseHelper.getGcdIdFromText(lineSplit[0]);
                        String dbPediaId = ParseHelper.getDbPediaIdFromText(lineSplit[2]);

                        if (gcdId != null && dbPediaId != null) {
                            if (ers.containsKey(gcdId)) {
                                ers.get(gcdId).dbPediaIds.add(dbPediaId);
                            }
                        }
                    }

                });
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveErs(Map<String, EquivalenceRelation> equivalenceRelationMap) {
        List<EquivalenceRelation> equivalenceRelations = new ArrayList<>(equivalenceRelationMap.values());

        try {
            try (FileWriter fileWriter = new FileWriter(filePath_04_ER)) {
                fileWriter.write("DbProId;GcdId;PermIds;DeDbPediaIds;DbPediaIds" + System.lineSeparator());
                for (int i = 0; i < equivalenceRelations.size(); i++) {
                    EquivalenceRelation eq = equivalenceRelations.get(i);
                    fileWriter.write(eq.getDbProId_GcdId_PermIds_DeDbPediaIds_DbPediaIdsString() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, EquivalenceRelation> getERsByGcdIds() {
        Map<String, EquivalenceRelation> result = new HashMap<>();

        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePath_04_ER))) {
                stream.skip(1).forEach(line -> {
                    String[] lineSplit = line.split(";", -1);
                    long dbProId = Long.parseLong(lineSplit[0]);
                    String gcdId = lineSplit[1];

                    EquivalenceRelation er = new EquivalenceRelation();
                    er.dbProId = dbProId;
                    er.gcdId = gcdId;

                    if (lineSplit[2].length() != 0) {
                        for (String s:lineSplit[2].split("\\|", -1)) {
                            er.permIds.add(Long.parseLong(s));
                        }
                    }

                    if (lineSplit[3].length() != 0) {
                        for (String s:lineSplit[3].split("\\|", -1)) {
                            er.deDbPediaIds.add(s);
                        }
                    }

                    if (lineSplit[4].length() != 0) {
                        for (String s:lineSplit[4].split("\\|", -1)) {
                            er.dbPediaIds.add(s);
                        }
                    }

                    result.put(gcdId, er);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Set<String> getGcdIds() {
        Map<String, EquivalenceRelation> ers = getERsByGcdIds();

        return ers.keySet();
    }

    public static Set<Long> getPermIds() {
        Map<String, EquivalenceRelation> ers = getERsByGcdIds();
        Set<Long> permIds = new HashSet<>();

        for (EquivalenceRelation er:ers.values()) {
            permIds.addAll(er.permIds);
        }

        return permIds;
    }

}
