package de.tuberlin.dbpro.ws17.kg_neo4j.analysis;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class StringContainsAnalyser {

    private static String searchString = "permid_4295868979";
    //private static List<String> linesFound;

    public static List<String> linesToWrite;


    public static void main(String[] args) {
        linesToWrite = new ArrayList<String>();

        String directory = "/media/cfengler/Kingston DT HyperX 3.0/Daten/001-030/";
        List<String> fileNames = new ArrayList<String>();
        for (int i = 1; i < 93; i++) {
            fileNames.add("data" + String.format("%03d", i));
        }

        try {
            for (String fileName : fileNames) {
                try (Stream<String> stream = Files.lines(Paths.get(directory + fileName))) {
                    stream.parallel().forEach(line -> {
                        if (line.contains(searchString)) {
                            linesToWrite.add(fileName + "   " + line);
                        }
                    });
                }
            }
        }
        catch (IOException e) {

        }

        String analysisFile = directory + "analysis_stringContains_" + searchString + ".txt";

        try {
            FileWriter fileWriter = new FileWriter(analysisFile);
            fileWriter.write(String.join(System.lineSeparator() , linesToWrite));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
