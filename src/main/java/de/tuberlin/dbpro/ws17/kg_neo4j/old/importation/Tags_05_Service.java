package de.tuberlin.dbpro.ws17.kg_neo4j.old.importation;

import de.tuberlin.dbpro.ws17.kg_neo4j.common.ParseHelper;
import de.tuberlin.dbpro.ws17.kg_neo4j.old.model.EquivalenceRelation;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tags_05_Service {

    private static String filePathPrefLabels = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport/ER/05_Tags/_http___www.w3.org_2004_02_skos_core_prefLabel_.predicate";
    private static String filePath_05_ER_Tags = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport/ER/05_Tags/05_ER_Tags.data";
    private static String filePath_05_TagsByGcdId = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport/ER/05_Tags/05_TagsByGcdId.data";
    private static String filePath_05_TagsByPermId = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport/ER/05_Tags/05_TagsByPermId.data";

    private static String gcdIdPrefix = "<http://corp.dbpedia.org/resource/gcd_";
    private static String permIdPrefix = "<http://corp.dbpedia.org/resource/permid_";

    public static void main(String[] args) {
        Map<String, EquivalenceRelation> ers = ER_04_DbPediaIds_Service.getERsByGcdIds();
        Map<String, Set<String>> prefLabelsByGcdId = getPrefLabelsByGcdId();
        Map<Long, Set<String>> prefLabelsByPermId = getPrefLabelsByPermId();

        for (EquivalenceRelation er:ers.values()) {
            if (prefLabelsByGcdId.containsKey(er.gcdId)) {
                er.tags.addAll(prefLabelsByGcdId.get(er.gcdId));
            }
            for (long permId:er.permIds) {
                if (prefLabelsByPermId.containsKey(permId)) {
                    er.tags.addAll(prefLabelsByPermId.get(permId));
                }
            }

        }

        savePrefLabelsByGcdId(prefLabelsByGcdId);
        savePrefLabelsByPermId(prefLabelsByPermId);
        saveErsWithTags(ers);
    }

    private static void addTagsToErs(Map<String, EquivalenceRelation> ers) {
        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePathPrefLabels))) {
                int i = 0;
                stream.forEach(new Consumer<String>() {
                    int i = 0;
                    @Override
                    public void accept(String line) {
                        i++;
                        System.out.println(i + "/");
                        String[] lineSplit = line.split("\t");

                        if (lineSplit[0].startsWith(gcdIdPrefix)) {
                            String gcdId = ParseHelper.getGcdIdFromText(lineSplit[0]);
                            String prefLabel = StringEscapeUtils.unescapeJava(lineSplit[2]);

                            if (gcdId != null && prefLabel != null) {
                                if (ers.containsKey(gcdId)) {
                                    ers.get(gcdId).tags.add(prefLabel);
                                }
                            }
                        } else if (lineSplit[0].startsWith(permIdPrefix)) {
                            long permId = ParseHelper.getPermIdFromText(lineSplit[0]);
                            String prefLabel = StringEscapeUtils.unescapeJava(lineSplit[2]);

                            for (EquivalenceRelation er : ers.values()) {
                                if (er.permIds.contains(permId)) {
                                    er.tags.add(prefLabel);
                                }
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

    private static void saveErsWithTags(Map<String, EquivalenceRelation> ersMap) {
        List<EquivalenceRelation> ers = new ArrayList<>(ersMap.values());

        try {
            try (FileWriter fileWriter = new FileWriter(filePath_05_ER_Tags)) {
                fileWriter.write("DbProId;GcdId;PermIds;DeDbPediaIds;DbPediaIds;Tags" + System.lineSeparator());
                for (int i = 0; i < ers.size(); i++) {
                    EquivalenceRelation eq = ers.get(i);
                    fileWriter.write(eq.getEr_TagsString() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Set<String>> getPrefLabelsByGcdId() {
        Set<String> gcdIds = ER_04_DbPediaIds_Service.getGcdIds();

        Map<String, Set<String>> result = new HashMap<>();
        for (String gcdId:gcdIds) {
            result.put(gcdId, new HashSet<>());
        }

        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePathPrefLabels))) {
                stream.forEach(line -> {
                    if (line.startsWith(gcdIdPrefix)) {
                        String[] lineSplit = line.split("\t");
                        String gcdId = ParseHelper.getGcdIdFromText(lineSplit[0]);
                        String prefLabel = StringEscapeUtils.unescapeJava(lineSplit[2]);

                        if (result.containsKey(gcdId)) {
                            //result.put(gcdId, new HashSet<>());
                            result.get(gcdId).add(prefLabel);
                        }
                        else {
                            System.out.println(line);
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static Map<Long, Set<String>> getPrefLabelsByPermId() {
        Set<Long> permIds = ER_04_DbPediaIds_Service.getPermIds();

        Map<Long, Set<String>> result = new HashMap<>();
        for (long permId:permIds) {
            result.put(permId, new HashSet<>());
        }

        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePathPrefLabels))) {
                stream.forEach(line -> {
                    if (line.startsWith(permIdPrefix)) {
                        String[] lineSplit = line.split("\t");
                        long permId = ParseHelper.getPermIdFromText(lineSplit[0]);
                        String prefLabel = StringEscapeUtils.unescapeJava(lineSplit[2]);

                        if (result.containsKey(permId)) {
                            //result.put(gcdId, new HashSet<>());
                            result.get(permId).add(prefLabel);
                        }
                        else {
                            System.out.println(line);
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static void savePrefLabelsByGcdId(Map<String, Set<String>> prefLabelsByGcdId) {
        try {
            try (FileWriter fileWriter = new FileWriter(filePath_05_TagsByGcdId)) {
                fileWriter.write("GcdId;Tags" + System.lineSeparator());

                for (String gcdId:prefLabelsByGcdId.keySet()) {
                    fileWriter.write(gcdId + ";" + prefLabelsByGcdId.get(gcdId).stream().collect(Collectors.joining("|")) + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void savePrefLabelsByPermId(Map<Long, Set<String>> prefLabelsByPermId) {
        try {
            try (FileWriter fileWriter = new FileWriter(filePath_05_TagsByPermId)) {
                fileWriter.write("GcdId;Tags" + System.lineSeparator());

                for (long permId:prefLabelsByPermId.keySet()) {
                    fileWriter.write(permId + ";" + prefLabelsByPermId.get(permId).stream().collect(Collectors.joining("|")) + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
