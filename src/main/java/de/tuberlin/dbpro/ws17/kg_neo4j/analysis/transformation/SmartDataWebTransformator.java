package de.tuberlin.dbpro.ws17.kg_neo4j.analysis.transformation;

import org.apache.commons.text.StringEscapeUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class SmartDataWebTransformator {

    private static String baseDataDirectory = "/media/cfengler/My Passport/DBPro Daten/Rohdaten/";
    private static String statisticsFilePath = "/media/cfengler/MyPassport_1TB/Transformation Christian/info/statistic.txt";
    private static String transformDataDirectory = "/media/cfengler/MyPassport_1TB/Transformation Christian/";
    private static String predicatesDirectory = "/media/cfengler/MyPassport_1TB/Transformation Christian/predicates_min_5000_entries/";

    public static void main(String[] args) {
        Map<String, Integer> allPredicates = readAllPredicatesFromStatistics(statisticsFilePath);
        removeAllPredicatesWithLessThen5000Entries(allPredicates);
        Map<String, FileWriter> fileWriterByPredicate = getFileWriterByPredicate(allPredicates);

        List<String> inputFilePaths = getInputFilePaths(1, 92);
        readInputAndWriteToPredicateFiles(inputFilePaths, fileWriterByPredicate);

        closeFileWriters(fileWriterByPredicate.values());
        //Test if filenames are ok
        //createFilesForAllPredicates(allPredicates);


        //create Statistics-File
        //List<String> inputFilePaths = getInputFilePaths(1, 92);
        //Map<String, Integer> allPredicates = getAllPredicates(inputFilePaths);
        //savePredicates(allPredicates);
    }

    private static void readInputAndWriteToPredicateFiles(List<String> inputFilePaths, Map<String, FileWriter> fileWriterByPredicate) {
        for (String inputFilePath:inputFilePaths) {
            System.out.println(inputFilePath);
            try {
                try (Stream<String> stream = Files.lines(Paths.get(inputFilePath))) {
                    stream.forEach(line -> {
                        String[] lineSplit = line.split("\t");
                        if (lineSplit.length == 4) {
                            String unescapedPredicate = StringEscapeUtils.unescapeJava(lineSplit[1]);
                            if (fileWriterByPredicate.containsKey(unescapedPredicate)) {
                                try {
                                    fileWriterByPredicate.get(unescapedPredicate).append(line + System.lineSeparator());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else {
                            System.out.println(line);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Map<String, Integer> readAllPredicatesFromStatistics(String statisticsFilePath) {
        //fileWriter.write(String.format("%09d", entry.getValue()) + "_" + entry.getKey() + System.lineSeparator());
        Map<String, Integer> allPredicates = new HashMap<>();
        try {
            try (Stream<String> stream = Files.lines(Paths.get(statisticsFilePath))) {
                stream.forEach(line -> {
                    allPredicates.put(line.substring(10), Integer.parseInt(line.substring(0, 9)));
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allPredicates;
    }

    private static void removeAllPredicatesWithLessThen5000Entries(Map<String, Integer> allPredicates) {
        allPredicates.entrySet().removeIf(entry -> entry.getValue() < 5000);
    }

    private static Map<String, FileWriter> getFileWriterByPredicate(Map<String, Integer> allPredicates) {
        Map<String, FileWriter> result = new HashMap<>();

        for (Map.Entry<String, Integer> entry:allPredicates.entrySet()) {
            String fileName = convertStringToFileName(entry.getKey()) + ".predicate";
            try {
                result.put(entry.getKey(), new FileWriter(predicatesDirectory + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private static void closeFileWriters(Collection<FileWriter> fileWriters) {
        for (FileWriter fileWriter:fileWriters) {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createFilesForAllPredicates(Map<String, Integer> allPredicates) {
        for (Map.Entry<String, Integer> entry:allPredicates.entrySet()) {
            String fileName = convertStringToFileName(entry.getKey()) + ".predicate";
            File predicateFile = new File(predicatesDirectory +  fileName);
            if (predicateFile.exists()) {
                System.out.println("Problem: " + entry.getKey() + "___" + fileName);
            }
            else {
                try {
                    predicateFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static List<String> getInputFilePaths(int fromFileIndex, int toFileIndex) {
        List<String> result = new ArrayList<>();
        for (int i = fromFileIndex; i < toFileIndex + 1; i++) {
            result.add(baseDataDirectory + "output" + String.format("%06d", i) + ".nq");
        }
        return result;
    }

    private static Map<String, Integer> getAllPredicates(List<String> filePaths) {
        Map<String, Integer> allPredicates = new HashMap<>();

        for (String filePath:filePaths) {
            System.out.println(filePath);
            try {
                try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
                    stream.forEach(line -> {
                        String[] lineSplit = line.split("\t");
                        if (lineSplit.length == 4) {
                            String unescapedPredicate = StringEscapeUtils.unescapeJava(lineSplit[1]);

                            if (allPredicates.containsKey(unescapedPredicate)) {
                                allPredicates.put(unescapedPredicate, allPredicates.get(unescapedPredicate) + 1);
                            }
                            else {
                                allPredicates.put(unescapedPredicate, 1);
                            }
                        }
                        else {
                            System.out.println(line);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return allPredicates;
    }

    private static void savePredicates(Map<String, Integer> allPredicates) {
        try {
            try (FileWriter fileWriter = new FileWriter(transformDataDirectory + "statistic.txt", true)) {
                for (Map.Entry<String, Integer> entry:allPredicates.entrySet()) {
                    fileWriter.write(String.format("%09d", entry.getValue()) + "_" + entry.getKey() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String convertStringToFileName(String searchString) {
        String result = searchString.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
        if (result.contains("\\") || result.contains("//")) {
            System.out.println(result);
        }
        return result;
    }

}
