package de.tuberlin.dbpro.ws17.kg_neo4j.analysis;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StringContainsAnalyser {

    private static String searchString = "4295868979";
    private static List<String> linesFound;

    public static void main(String[] args) {
        linesFound = new ArrayList<String>();

        String directory = "/media/cfengler/Kingston DT HyperX 3.0/Daten/001-030/";
        List<String> fileNames = new ArrayList<String>();
        for (int i = 1; i < 93; i++) {
            fileNames.add("data" + String.format("%03d", i));
        }

        try {
            for (String fileName : fileNames) {
                try (Stream<String> stream = Files.lines(Paths.get(directory + fileName))) {
                    stream.forEach(line -> {
                        if (line.contains(searchString)) {
                            linesFound.add(line);
                        }
                    });
                }
            }
        }
        catch (IOException e) {

        }

        String analysisFile = directory + "analysis_stringContains" + searchString + ".txt";

        try {
            FileWriter fileWriter = new FileWriter(analysisFile);
            fileWriter.write(String.join(", ", linesFound + System.lineSeparator()));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
