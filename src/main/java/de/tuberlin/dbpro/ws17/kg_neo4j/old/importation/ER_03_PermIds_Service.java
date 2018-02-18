package de.tuberlin.dbpro.ws17.kg_neo4j.old.importation;

import de.tuberlin.dbpro.ws17.kg_neo4j.common.ParseHelper;
import de.tuberlin.dbpro.ws17.kg_neo4j.old.model.EquivalenceRelation;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class ER_03_PermIds_Service {

    private static String filePathSameAs = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport/ER/03_ER_PermIds/_http___www.w3.org_2002_07_owl_sameAs_.predicate";
    private static String filePath_03_ER = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport/ER/03_ER_PermIds/03_ER_PermIds.data";

    private static String gcdIdPrefix = "<http://corp.dbpedia.org/resource/gcd_";
    private static String permIdPrefix = "<http://corp.dbpedia.org/resource/permid_";

    public static void main(String[] args) {
        Map<String, EquivalenceRelation> equivalenceRelations = ER_02_DbProId_Service.getERsByGcdIds();

        AddPermIdsToEquivalenceRelations(equivalenceRelations);

        saveEquivalenceRelations(equivalenceRelations);
    }

    private static void AddPermIdsToEquivalenceRelations(Map<String, EquivalenceRelation> equivalenceRelations) {
        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePathSameAs))) {
                stream.forEach(line -> {
                    String[] lineSplit = line.split("\t");
                    if (lineSplit[0].startsWith(gcdIdPrefix) && lineSplit[2].startsWith(permIdPrefix)) {
                        String gcdId = ParseHelper.getGcdIdFromText(lineSplit[0]);
                        Long permId = ParseHelper.getPermIdFromText(lineSplit[2]);

                        if (gcdId != null && permId != 0) {
                            if (equivalenceRelations.containsKey(gcdId)) {
                                equivalenceRelations.get(gcdId).permIds.add(permId);
                            }
                        }
                    } else if (lineSplit[0].startsWith(permIdPrefix) && lineSplit[2].startsWith(gcdIdPrefix)) {
                        String gcdId = ParseHelper.getGcdIdFromText(lineSplit[2]);
                        Long permId = ParseHelper.getPermIdFromText(lineSplit[0]);

                        if (gcdId != null && permId != 0) {
                            if (equivalenceRelations.containsKey(gcdId)) {
                                equivalenceRelations.get(gcdId).permIds.add(permId);
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

    private static void saveEquivalenceRelations(Map<String, EquivalenceRelation> equivalenceRelationMap) {
        List<EquivalenceRelation> ers = new ArrayList<>(equivalenceRelationMap.values());

        ers.sort(new EquivalenceRelationsComparator());

        try {
            try (FileWriter fileWriter = new FileWriter(filePath_03_ER)) {
                fileWriter.write("DbProId;GcdId;PermIds" + System.lineSeparator());
                for (int i = 0; i < ers.size(); i++) {
                    EquivalenceRelation eq = ers.get(i);
                    fileWriter.write(eq.getDbProId_GcdId_PermIdsString() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, EquivalenceRelation> getERsByGcdIds() {
        Map<String, EquivalenceRelation> result = new HashMap<>();

        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePath_03_ER))) {
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

                    result.put(gcdId, er);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
