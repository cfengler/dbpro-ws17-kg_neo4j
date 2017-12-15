package de.tuberlin.dbpro.ws17.kg_neo4j.model;

import de.tuberlin.dbpro.ws17.kg_neo4j.analysis.RdfEntity_cf;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ModelExporter {

//    private static String directory = "/media/cfengler/Kingston DT HyperX 3.0/Daten/";
//
//    private static List<String> linesNotProcessed = new ArrayList<>();
//
//    public static void main(String[] args) {
//        Company company = new Company("permid_4295868979", "gcd_f588");
//        List<String> fileNames = getInputFilesNames(1, 92);
//        processInputFiles(company, fileNames);
//        writeOutputFile(company);
//    }
//
//    private static List<String> getInputFilesNames(int fromFileIndex, int toFileIndex) {
//        List<String> result = new ArrayList<>();
//        for (int i = fromFileIndex; i < toFileIndex + 1; i++) {
//            result.add("data" + String.format("%03d", i));
//        }
//        return result;
//    }
//
//    private static void processInputFiles(Company company, List<String> fileNames) {
//        try {
//            for (String fileName : fileNames) {
//                System.out.println(fileName);
//                try (Stream<String> stream = Files.lines(Paths.get(directory + fileName))) {
//                    stream.parallel().forEach(line -> {
//                        if (line.contains(company.id_permid)) {
//                            processLineWith_id_permid(company, line);
//                        }
//                        else if (line.contains(company.id_gcd)) {
//                            processLineWith_id_gcd(company, line);
//                        }
//                    });
//                }
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void processLineWith_id_permid(Company company, String line) {
//        if (line.contains("<http://www.w3.org/2004/02/skos/core#prefLabel>")) {
//            //Bezeichnung
//            RdfEntity_cf rdfEntity = new RdfEntity_cf(line);
//            company.names.add(rdfEntity.object);
//        }
//        else if (line.contains("<http://www.w3.org/ns/prov#startedAtTime>")) {
//            //Gründung
//            RdfEntity_cf rdfEntity = new RdfEntity_cf(line);
//            if (company.founding == null) {
//                company.founding = rdfEntity.object;
//            }
//            else {
//                System.out.println("not processed:" + rdfEntity.toString());
//            }
//        }
//        else if (line.contains("<http://corp.dbpedia.org/resource/" + company.id_permid + "_headquarterSite>")) {
//            //Hauptsitz
//            RdfEntity_cf rdfEntity = new RdfEntity_cf(line);
//            if (company.headquarterSite == null) {
//                company.headquarterSite = rdfEntity.object;
//            }
//            else {
//                System.out.println("not processed:" + rdfEntity.toString());
//            }
//        }
//        else if (line.contains("<http://corp.dbpedia.org/resource/" + company.id_permid + "_registeredSite>")) {
//            //juristischer Sitz
//            RdfEntity_cf rdfEntity = new RdfEntity_cf(line);
//            if (company.registeredSite == null) {
//                company.registeredSite = rdfEntity.object;
//            }
//            else {
//                System.out.println("not processed:" + rdfEntity.toString());
//            }
//        }
//        else {
//            linesNotProcessed.add(line);
//        }
//        //else if () {
//            //Tochtergesellschaften
//        //}
//    }
//
//    private static void processLineWith_id_gcd(Company company, String line) {
//        if (line.contains("<http://www.w3.org/2004/02/skos/core#prefLabel>")) {
//            RdfEntity_cf rdfEntity = new RdfEntity_cf(line);
//            company.names.add(rdfEntity.object);
//        }
//        else {
//            linesNotProcessed.add(line);
//        }
//    }
//
//    private static void writeOutputFile(Company company) {
//        String fileName = "company_permid" + company.id_permid + ".txt";
//
//        String analysisFile = directory + fileName;
//
//        try {
//            FileWriter fileWriter = new FileWriter(analysisFile);
//            fileWriter.write(company.toString());
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
