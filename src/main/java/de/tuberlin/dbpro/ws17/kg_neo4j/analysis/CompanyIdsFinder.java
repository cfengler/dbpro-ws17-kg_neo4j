package de.tuberlin.dbpro.ws17.kg_neo4j.analysis;

import de.tuberlin.dbpro.ws17.kg_neo4j.model.ExternalId;
import de.tuberlin.dbpro.ws17.kg_neo4j.model.RdfQuadrupel;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class CompanyIdsFinder {

    private static String predicatesDirectory = "/media/cfengler/MyPassport_1TB/Transformation Christian/predicates_min_5000_entries/";
    private static String sameAsFilePath = "/media/cfengler/MyPassport_1TB/Transformation Christian/predicates_min_5000_entries/_http___www.w3.org_2002_07_owl_sameAs_.predicate";
    private static String equivalenceRelationFilePath = "/media/cfengler/MyPassport_1TB/Transformation Christian/Lufthansa/equivalenceRelation.txt";

    public static void main(String[] args) {
        String permid = "<http://corp.dbpedia.org/resource/permid_4295868979>";
        Set<String> equivalenceRelation = getEquivalenceRelation(sameAsFilePath, permid);

        saveEquivalenceRelation(equivalenceRelation, equivalenceRelationFilePath);
    }

    private static Set<String> getEquivalenceRelation(String sameAsFilePath, String externalId) {
        Set<String> result = new HashSet<>();
        List<String> searchIds = new ArrayList<>();

        result.add(externalId);
        searchIds.add(externalId);

        while (!searchIds.isEmpty()) {
            String searchId = searchIds.remove(0);
            System.out.println(searchId);

            try {
                try (Stream<String> stream = Files.lines(Paths.get(sameAsFilePath))) {
                    stream.forEach(line -> {
                        RdfQuadrupel rdfQuadrupel = new RdfQuadrupel(line);
                        if (rdfQuadrupel.subject.equals(searchId)) {
                            if (!result.contains(rdfQuadrupel.object)) {
                                result.add(rdfQuadrupel.object);
                                searchIds.add(rdfQuadrupel.object);
                            }
                        }
                    });
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }


        return result;
    }

    private static void saveEquivalenceRelation(Set<String> equivalenceRelation, String outputFilePath) {

        try {
            FileWriter fileWriter = new FileWriter(outputFilePath);

            for (Iterator<String> iterator = equivalenceRelation.iterator();
                 iterator.hasNext();) {
                fileWriter.write(iterator.next() + System.lineSeparator());
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
