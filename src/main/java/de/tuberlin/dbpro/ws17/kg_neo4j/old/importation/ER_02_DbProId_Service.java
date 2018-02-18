package de.tuberlin.dbpro.ws17.kg_neo4j.old.importation;

import de.tuberlin.dbpro.ws17.kg_neo4j.old.model.EquivalenceRelation;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class ER_02_DbProId_Service {

    private static String filePath_02_ER = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport/ER/02_ER_DbProIds/02_ER.data";

    public static void main(String[] args) {
        Set<String> gcdIds = ER_01_GcdIds_Service.getGcdIds();

        saveGcdIdsWithDbProIds(gcdIds);
    }

    private static void saveGcdIdsWithDbProIds(Set<String> gcdIdsSet) {
        List<String> gcdIds = new ArrayList<>(gcdIdsSet);
        gcdIds.sort(new GcdIdsComparator());

        try {
            try (FileWriter fileWriter = new FileWriter(filePath_02_ER)) {
                fileWriter.write("DbProId;GcdId" + System.lineSeparator());

                for (int i = 0; i < gcdIds.size(); i++) {
                    long dbProId = i + 1;
                    String gcdId = gcdIds.get(i);

                    fileWriter.write(dbProId + ";" + gcdId + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, EquivalenceRelation> getERsByGcdIds() {
        Map<String, EquivalenceRelation> result = new HashMap<>();

        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePath_02_ER))) {
                stream.skip(1).forEach(line -> {
                    String[] lineSplit = line.split(";");
                    long dbProId = Long.parseLong(lineSplit[0]);
                    String gcdId = lineSplit[1];

                    EquivalenceRelation er = new EquivalenceRelation();
                    er.dbProId = dbProId;
                    er.gcdId = gcdId;

                    result.put(gcdId, er);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
