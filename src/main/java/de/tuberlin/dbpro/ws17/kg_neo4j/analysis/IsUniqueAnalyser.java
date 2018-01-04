package de.tuberlin.dbpro.ws17.kg_neo4j.analysis;

import de.tuberlin.dbpro.ws17.kg_neo4j.model.RdfQuadrupel;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class IsUniqueAnalyser {

    private static String filePathIds = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport/01_GcdIds_PermIds/_http___www.w3.org_1999_02_22-rdf-syntax-ns_type__http___dbpedia.org_ontology_Company_.extraction";
    private static String filePathGcdIds = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport/GcdIds.data";

    private static String gcdPrefix = "<http://corp.dbpedia.org/resource/gcd";
    private static String permPrefix = "<http://corp.dbpedia.org/resource/permid";

    public static void main(String[] args) {
        Set<Long> uniqueGcdIds = new HashSet<>();
        Set<Long> uniquePermIds = new HashSet<>();

        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePathIds))) {
                stream.forEach(line -> {
                    //System.out.println(line);

                    RdfQuadrupel rdfQuadrupel = new RdfQuadrupel(line);

                    if (rdfQuadrupel.subject.startsWith(gcdPrefix)) {
                        //<http://corp.dbpedia.org/resource/gcd_31840>
                        String[] subjectSplit = rdfQuadrupel.subject.split("_");
                        if (subjectSplit.length == 2 && subjectSplit[0].equals(gcdPrefix)) {
                            try {
                                String gcdIdAsString = subjectSplit[1].substring(0, subjectSplit[1].length() - 1);

                                Long gcdId = Long.parseLong(gcdIdAsString);
                                if (!uniqueGcdIds.contains(gcdId)) {
                                    uniqueGcdIds.add(gcdId);
                                } else {
                                    System.out.println("gcdId: " + gcdId + " exists double.");
                                }
                            }
                            catch (NumberFormatException nfe) {
                                System.out.println("not processed");
                            }
                        }
                        else {
                            System.out.println("not processed");
                        }
                    }
                    else if (rdfQuadrupel.subject.startsWith(permPrefix)) {
                        //<http://corp.dbpedia.org/resource/permid_5036198099>
                        String[] subjectSplit = rdfQuadrupel.subject.split("_");
                        if (subjectSplit.length == 2 && subjectSplit[0].equals(permPrefix)) {
                            String permIdAsString = subjectSplit[1].substring(0, subjectSplit[1].length() - 1);
                            Long permId = Long.parseLong(permIdAsString);
                            if (!uniquePermIds.contains(permId)) {
                                uniquePermIds.add(permId);
                            }
                            else {
                                System.out.println("permId: " + permId + " exists double.");
                            }
                        }
                        else {
                            System.out.println("not processed");
                        }
                    }
                    else {
                        System.out.println("not processed");
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        saveGcdIds(uniqueGcdIds);

        System.out.println("uniqueGcdIds.size: " + uniqueGcdIds.size());
        System.out.println("uniquePermIds.size: " + uniquePermIds.size());
    }

    private static void saveGcdIds(Set<Long> uniqueGcdIds) {
        try {
            try (FileWriter fileWriter = new FileWriter(filePathGcdIds)) {
                uniqueGcdIds.stream().forEach(e -> {
                    try {
                        fileWriter.write(e + System.lineSeparator());
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
