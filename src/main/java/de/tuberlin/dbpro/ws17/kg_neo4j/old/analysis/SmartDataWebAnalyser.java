package de.tuberlin.dbpro.ws17.kg_neo4j.old.analysis;

import de.tuberlin.dbpro.ws17.kg_neo4j.old.model.RdfQuadrupel;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class SmartDataWebAnalyser {

    private static String smartDataWebDirectory = "/media/cfengler/My Passport/DBPro_Daten/SmartDataWeb/";
    private static String statisticsDirectory = "/media/cfengler/MyPassport_1TB/Transformation Christian/statistics/";

    //public static Map<String, DataProvider> filesByDataProvider;

    public static void main(String[] args) {
        List<String> inputFilePaths = getInputFilePaths(1, 92);
        Map<String, Integer> dataProvidersWithRdfQuadrupelCount = loadDataProvidersWithRdfQuadrupelCount();//getDataProvidersWithRdfQuadrupelCount(inputFilePaths);

        saveDataProvidersWithRdfQuadrupelCountSorted(dataProvidersWithRdfQuadrupelCount);

//        filesByDataProvider = new HashMap<String, DataProvider>();
//
//        String directory = "/media/cfengler/Kingston DT HyperX 3.0/Daten/001-030/";
//        List<String> fileNames = new ArrayList<String>();
//        for (int i = 1; i < 93; i++) {
//            fileNames.add("data" + String.format("%03d", i));
//        }
//
//        try {
//            for (String fileName : fileNames) {
//                try (Stream<String> stream = Files.lines(Paths.get(directory + fileName))) {
//                    stream.forEach(line -> {
//                        String[] lineSplit = line.split("\t");
//                        if (lineSplit.length == 4) {
//                            String dataProviderName = lineSplit[3];
//
//                            if (!filesByDataProvider.containsKey(dataProviderName)) {
//                                filesByDataProvider.put(dataProviderName, new DataProvider(dataProviderName));
//                            }
//                            else {
//                                filesByDataProvider.get(dataProviderName).count++;
//                            }
//
//                            if (!filesByDataProvider.get(dataProviderName).files.contains(fileName)) {
//                                filesByDataProvider.get(dataProviderName).files.add(fileName);
//                            }
//                        }
//                        else
//                        {
//                            System.out.println("Wrong: " + fileName + ":" + line);
//                        }
//                    });
//                }
//            }
//        }
//        catch (IOException e) {
//
//        }
//        //Entferne alle Daten-Provider die nur für einen einzigen Eintrag verantwortlich sind
//        List<DataProvider> dataProvidersCount_Greater_1 = filesByDataProvider.values().stream().filter(dataProvider -> dataProvider.count > 1).collect(Collectors.toList());
//        //Sortiere nach Anzahl der Einträge
//        dataProvidersCount_Greater_1.sort((o1, o2) -> o2.count - o1.count);
//
//        String analysisFile = directory + "analysis.txt";
//
//        try {
//            FileWriter fileWriter = new FileWriter(analysisFile);
//
//            for (DataProvider dataProvider : dataProvidersCount_Greater_1) {
//                fileWriter.write(dataProvider.toString() + System.lineSeparator());
//                fileWriter.write("Files: " + String.join(", ", dataProvider.files + System.lineSeparator()));
//            }
//
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        filesByDataProvider.size();
    }

    private static List<String> getInputFilePaths(int fromFileIndex, int toFileIndex) {
        List<String> result = new ArrayList<>();
        for (int i = fromFileIndex; i < toFileIndex + 1; i++) {
            result.add(smartDataWebDirectory + "output" + String.format("%06d", i) + ".nq");
        }
        return result;
    }

    private static Map<String, Integer> getDataProvidersWithRdfQuadrupelCount(List<String> inputFilePaths) {
        Map<String, Integer> result = new ConcurrentHashMap<>();

        try {
            for (String filePath : inputFilePaths) {
                System.out.println(filePath);
                try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
                    stream.parallel().forEach(line -> {
                        RdfQuadrupel rdfQuadrupel = new RdfQuadrupel(line);
                        if (result.containsKey(rdfQuadrupel.source)) {
                            result.put(rdfQuadrupel.source, result.get(rdfQuadrupel.source) + 1);
                        }
                        else {
                            result.put(rdfQuadrupel.source, 1);
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

    private static void saveDataProvidersWithRdfQuadrupelCount(Map<String, Integer> dataProvidersWithRdfQuadrupelCount) {
        try {
            try (FileWriter fileWriter = new FileWriter(statisticsDirectory + "dataProvidersWithRdfQuadrupelCount.txt")) {
                for (Map.Entry<String, Integer> entry:dataProvidersWithRdfQuadrupelCount.entrySet()) {
                    fileWriter.write(String.format("%09d", entry.getValue()) + "_" + entry.getKey() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveDataProvidersWithRdfQuadrupelCountSorted(Map<String, Integer> dataProvidersWithRdfQuadrupelCount) {

    }

    private static Map<String, Integer> loadDataProvidersWithRdfQuadrupelCount() {
        Map<String, Integer> result = new ConcurrentHashMap<>();

        try {
            try (Stream<String> stream = Files.lines(Paths.get(statisticsDirectory + "dataProvidersWithRdfQuadrupelCount.txt"))) {
                stream.parallel().forEach(line -> {
                    result.put(line.substring(10), Integer.parseInt(line.substring(0, 9)));
                });
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


}
