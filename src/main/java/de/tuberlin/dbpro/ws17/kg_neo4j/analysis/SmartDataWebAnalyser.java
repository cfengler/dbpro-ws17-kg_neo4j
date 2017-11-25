package de.tuberlin.dbpro.ws17.kg_neo4j.analysis;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SmartDataWebAnalyser {

    public static Map<String, DataProvider> filesByDataProvider;

    public static void main(String[] args) {
        filesByDataProvider = new HashMap<String, DataProvider>();

        String directory = "/media/cfengler/Kingston DT HyperX 3.0/Daten/001-030/";
        List<String> fileNames = new ArrayList<String>();
        for (int i = 1; i < 93; i++) {
            fileNames.add("data" + String.format("%03d", i));
        }

        try {
            for (String fileName : fileNames) {
                try (Stream<String> stream = Files.lines(Paths.get(directory + fileName))) {
                    stream.forEach(line -> {

                        String[] lineSplit = line.split("\t");
                        if (lineSplit.length == 4) {
                            String dataProviderName = lineSplit[3];

                            if (!filesByDataProvider.containsKey(dataProviderName)) {
                                filesByDataProvider.put(dataProviderName, new DataProvider(dataProviderName));
                            }
                            else {
                                filesByDataProvider.get(dataProviderName).count++;
                            }

                            if (!filesByDataProvider.get(dataProviderName).files.contains(fileName)) {
                                filesByDataProvider.get(dataProviderName).files.add(fileName);
                            }
                        }
                        else
                        {
                            System.out.println("Wrong: " + fileName + ":" + line);
                        }
                    });
                }
            }
        }
        catch (IOException e) {

        }
        //Entferne alle Daten-Provider die nur für einen einzigen Eintrag verantwortlich sind
        List<DataProvider> dataProvidersCount_Greater_1 = filesByDataProvider.values().stream().filter(dataProvider -> dataProvider.count > 1).collect(Collectors.toList());
        //Sortiere nach Anzahl der Einträge
        dataProvidersCount_Greater_1.sort((o1, o2) -> o2.count - o1.count);

        String analysisFile = directory + "analysis.txt";

        try {
            FileWriter fileWriter = new FileWriter(analysisFile);

            for (DataProvider dataProvider : dataProvidersCount_Greater_1) {
                fileWriter.write(dataProvider.toString() + System.lineSeparator());
                fileWriter.write("Files: " + String.join(", ", dataProvider.files + System.lineSeparator()));
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        filesByDataProvider.size();
    }



}
