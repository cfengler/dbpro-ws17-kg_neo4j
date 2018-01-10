package de.tuberlin.dbpro.ws17.kg_neo4j.old.analysis;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class StringContainsAnalyser {

    private static String searchString = "<http://dbpedia.org/resource/Lufthansa>";

    private static String directoryPathInput = "/media/cfengler/My Passport/DBPro_Daten/SmartDataWeb/";
    private static String directoryPathOutput = "/media/cfengler/MyPassport_1TB/Datenimport DbPedia/Lufthansa/";

    private static List<String> subjectLines = new ArrayList<>();
    private static List<String> objectLines = new ArrayList<>();

    public static void main(String[] args) {
        List<String> inputFilePaths = getInputFilePaths();
        executeSearch(inputFilePaths, searchString);
        writeOutputFile(searchString);
        //for (String searchString:searchStrings) {
            //List<String> linesToWrite = findLinesWithSearchString(inputFilePaths, searchString);

        //}
    }

    private static List<String> getInputFilePaths() {
        List<String> result = new ArrayList<>();

        try {
            try (Stream<Path> paths = Files.walk(Paths.get(directoryPathInput))) {
                paths
                    .sorted()
                    .filter(Files::isRegularFile)
                    .forEach(p -> result.add(p.toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static void executeSearch(List<String> filePaths, String searchString) {
        try {
            for (String filePath : filePaths) {
                System.out.println(filePath);
                try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
                    stream.parallel().forEach(line -> {
                        String[] lineSplit = line.split("\t");
                        if (lineSplit[0].equals(searchString)) {
                            subjectLines.add(line);
                        } else if (lineSplit[2].equals(searchString)) {
                            objectLines.add(line);
                        }
                    });
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeOutputFile(String searchString) {
        String fileName = getFileNameFromSearchString(searchString);

        String objectFileName = directoryPathOutput + fileName + "AsObject" + ".txt";
        String subjectFileName = directoryPathOutput + fileName + "AsSubject" + ".txt";

        try {
            FileWriter fileWriter = new FileWriter(objectFileName);
            fileWriter.write(String.join(System.lineSeparator() , objectLines));
            fileWriter.close();

            fileWriter = new FileWriter(subjectFileName);
            fileWriter.write(String.join(System.lineSeparator() , subjectLines));
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileNameFromSearchString(String searchString) {
        return searchString.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }

}
