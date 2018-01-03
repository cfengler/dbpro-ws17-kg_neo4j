package de.tuberlin.dbpro.ws17.kg_neo4j.analysis;

import de.tuberlin.dbpro.ws17.kg_neo4j.model.RdfQuadrupel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class StringContainsAnalyser {

    private static List<String> searchStrings = Arrays.asList();
        //"<http://corp.dbpedia.org/resource/permid_5040791276>",
        //"<http://rdf.freebase.com/ns/m.0nbs2_2>",
        //"<http://rdf.freebase.com/ns/m.0p4wb>",
        //"<http://corp.dbpedia.org/resource/permid_4295868979>",
        //"<http://corp.dbpedia.org/resource/grid_425362_4>",
        //"<http://dbpedia.org/resource/Lufthansa>",
        //"<http://de.dbpedia.org/resource/Lufthansa>");


    private static String directory = "/media/cfengler/Kingston DT HyperX 3.0/Daten/";
    private static int fromFileIndex = 1;
    private static int toFileIndex = 92;

    public static void main(String[] args) {
        //String searchString = "gcd_f588";
        // "Lufthansa"
        // "permid_4295868979";
        // "gcd_f588";
        for (String searchString:searchStrings) {
            List<String> fileNames = getInputFilesNames();
            List<String> linesToWrite = getSortedLines(findLinesWithSearchString(fileNames, searchString));
            writeOutputFile(linesToWrite, searchString);
        }
    }

    private static List<String> getInputFilesNames() {
        List<String> result = new ArrayList<>();
        for (int i = fromFileIndex; i < toFileIndex + 1; i++) {
            result.add("data" + String.format("%03d", i));
        }
        return result;
    }

    private static List<String> findLinesWithSearchString(List<String> fileNames, String searchString) {
        List<String> result = new ArrayList<>();
        try {
            for (String fileName : fileNames) {
                System.out.println(fileName);
                try (Stream<String> stream = Files.lines(Paths.get(directory + fileName))) {
                    stream.parallel().forEach(line -> {
                        if (line.contains(searchString)) {
                            result.add(line);
                        }
                    });
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static List<String> getSortedLines(List<String> lines) {
        List<String> result = new ArrayList<>();

        List<RdfQuadrupel> rdfEntries = new ArrayList<>();

        for (String line:lines) {
            rdfEntries.add(new RdfQuadrupel(line));
        }

        rdfEntries.sort((o1, o2) ->  {
            int compareResult = o1.subject.compareTo(o2.subject);
            return compareResult;
        });

        for (RdfQuadrupel rdfEntry:rdfEntries) {
            result.add(rdfEntry.toString());
        }

        return result;
    }

    private static void writeOutputFile(List<String> linesToWrite, String searchString) {
        String fileName = getFileNameFromSearchString(searchString);

        String analysisFile = directory + String.format("%03d", fromFileIndex) + "_" + String.format("%03d", toFileIndex) + "_" + fileName + ".txt";

        try {
            FileWriter fileWriter = new FileWriter(analysisFile);
            fileWriter.write(String.join(System.lineSeparator() , linesToWrite));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileNameFromSearchString(String searchString) {
        return searchString.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }

}
