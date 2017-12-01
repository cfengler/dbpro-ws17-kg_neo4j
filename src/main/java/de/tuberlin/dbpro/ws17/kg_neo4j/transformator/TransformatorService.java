package de.tuberlin.dbpro.ws17.kg_neo4j.transformator;

import de.tuberlin.dbpro.ws17.kg_neo4j.analysis.RDFEntity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class TransformatorService {

    public void createListOfAllAvailableObjects(String pathToDataDir) {

        Map<String, Long> predicateCounter = new HashMap<>();
        Map<String, String> predicateNamesIdentifiers;
        // TODO: File exists -> load from file
        if(Files.exists(Paths.get(pathToDataDir + "predicateList.txt"))) {
            predicateNamesIdentifiers = getAllPredicatesFromTransformedFile(pathToDataDir, predicateCounter);
        }
        else {
            predicateNamesIdentifiers = getAllPredicates(pathToDataDir, predicateCounter);
        }
        createOrderedFileStructure(pathToDataDir, predicateNamesIdentifiers, predicateCounter);
    }

    private Map getAllPredicatesFromTransformedFile(String pathToDataDir, Map<String, Long> predicateCounter) {
        Map<String, String> predicateNamesIdentifiers = new HashMap();
        String fileName = "predicateList.txt";

            try (Stream<String> stream = Files.lines(Paths.get(pathToDataDir + fileName))) {
                System.out.println("[" + fileName + "] loading");
                stream.forEach(line -> {
                    {
                        String[] splittedLine = line.split("\t");
                        if(splittedLine.length == 3) {
                            String predicateProperty = splittedLine[0];
                            String predicateIdentifier = splittedLine[1];
                            long predicateCount = Long.valueOf(splittedLine[2]);

                            predicateNamesIdentifiers.put(predicateProperty, predicateIdentifier);
                            predicateCounter.put(predicateProperty, predicateCount);
                        }
                        else {
                            predicateNamesIdentifiers.put("unbehandeltesFormat", line);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        return predicateNamesIdentifiers;
    }

    private Map getAllPredicates(String pathToDataDir, Map<String, Long> predicateCounter) {
        Map<String, String> predicateNamesIdentifiers = new HashMap();

        Pattern pattern = Pattern.compile("[<](?<subject>.+?)[>]\\s[<](?<predicate>.+?)[>]\\s[<](?<object>.+?)[>]\\s[<](?<source>.+?)[>]\\s.");
        // <http://corp.dbpedia.org/resource/permid_4295868979>	<http://www.w3.org/2002/07/owl#sameAs>	<http://corp.dbpedia.org/resource/grid_425362_4>	<http://permid.org>

        List<String> fileNames = getFilesInDirectory(pathToDataDir);

        System.out.println("Found " + fileNames.size() + " Files in given directory.");
        System.out.println("Full Path to first file: " + pathToDataDir + fileNames.get(0));

        String predicateFilePath = pathToDataDir + "predicateList" + ".txt";
        fileNames.stream().forEach(fileName -> {
            try (Stream<String> stream = Files.lines(Paths.get(pathToDataDir + fileName))) {
                System.out.println("[" + fileName + "] analysing");
                stream.parallel().forEach(line -> {
                    {
                        String[] splittedLine = line.split("\t");
                        if(splittedLine.length == 4) {

                            String predicate = splittedLine[1];
                            String[] splittedPredicate = predicate.split("/");
                            String predicateProperty = splittedPredicate[splittedPredicate.length - 1];
                            predicateProperty = predicateProperty.substring(0, predicateProperty.length()-1);
                            int size = predicateNamesIdentifiers.size();
                            predicateNamesIdentifiers.put(predicateProperty, predicate);
                            synchronized (this) {
                                if(predicateNamesIdentifiers.size() > size) {
                                    System.out.println(predicateProperty + " : " + predicate);
                                    predicateCounter.put(predicateProperty, 1L);
                                }
                                else {
                                    predicateCounter.put(predicateProperty, predicateCounter.get(predicateProperty)+1);
                                }
                            }

                        }
                        else {
                            predicateNamesIdentifiers.put("unbehandeltesFormat", line);
                        }

                        synchronized (this) {
                            if (predicateNamesIdentifiers.size() > 100000) {
                                writePredicateMapToFile(predicateNamesIdentifiers, predicateCounter, predicateFilePath);
                                predicateNamesIdentifiers.clear();
                            }
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        writePredicateMapToFile(predicateNamesIdentifiers, predicateCounter, predicateFilePath);

        return predicateNamesIdentifiers;
    }

    public void createOrderedFileStructure(String pathToDataDir, Map<String, String> predicateNamesIdentifiers, Map<String, Long> predicateCounter) {

        //predicateNamesIdentifiers = sortPredicateMap(predicateNamesIdentifiers, predicateCounter);

        predicateCounter.entrySet().stream().sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
                .forEach(p -> {
                    if(predicateCounter.get(p.getKey()) > 1000) {
                        System.out.println(p.getKey() + ": " + p.getValue());
                        System.out.println("Creating extra file for predicate " + p.getKey() + " with " + p.getValue() + " occurrences");
                        createFileContainsOnlyPredicate(pathToDataDir, p.getKey(), predicateNamesIdentifiers.get(p.getKey()));
                    }
                    else {
                        // TODO: Andere in eine Datei schreiben?
                    }
                });



    }

    public Map<String, String> sortPredicateMap(Map<String, String> predicateNamesIdentifiers, Map<String, Long> predicateCounter) {
        List<Map.Entry<String, String>> list = new LinkedList<>(predicateNamesIdentifiers.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                if(predicateCounter.get(o1.getKey()) < predicateCounter.get(o1.getKey())) return -1;
                else if(predicateCounter.get(o1.getKey()) > predicateCounter.get(o1.getKey())) return 1;
                else return 0;
            }
        });

        predicateNamesIdentifiers = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : list) {
            predicateNamesIdentifiers.put(entry.getKey(), entry.getValue());
        }
        return predicateNamesIdentifiers;
    }



    public void createFileContainsOnlyPredicate(String pathToDataDir, String predicateName, String completePredicateIdentifier) {
        //List<String> containsPredicateLines = new ArrayList();
        StringBuffer containsPredicateLines = new StringBuffer();
        List<String> fileNames = getFilesInDirectory(pathToDataDir);
        System.out.println("Found " + fileNames.size() + " Files in given directory.");
        System.out.println("Full Path to first file: " + pathToDataDir + fileNames.get(0));

        String predicateFilePath = pathToDataDir + predicateName + ".txt";

        fileNames.stream().forEach(fileName ->
        {
            try {
                try (Stream<String> stream = Files.lines(Paths.get(pathToDataDir + fileName))) {
                    System.out.println("[" + fileName + "] analysing");
                    stream.parallel().forEach(line -> {
                        if (line.contains(completePredicateIdentifier)) {
                            containsPredicateLines.append(line + "\n");
                        }
                        synchronized (this) {
                            if(containsPredicateLines.length() > 100000) {
                                writeListToFile(containsPredicateLines, predicateFilePath);
                                containsPredicateLines.setLength(0);
                            }
                        }


                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writeListToFile(containsPredicateLines, predicateFilePath);
    }

    public void createSameAsFile(String pathToDataDir) {
        createFileContainsOnlyPredicate(pathToDataDir, "owl#sameAs", "<http://www.w3.org/2002/07/owl#sameAs>");
    }



    public synchronized void writeListToFile(StringBuffer lines, String filePath) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, true);
            fileWriter.append(lines.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void writePredicateMapToFile(Map<String, String> map, Map<String, Long> predicateCounter, String filePath) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, true);
            map.entrySet().stream().forEach((p) ->
            {
                try {
                    fileWriter.append(p.getKey() + "\t" + p.getValue() + "\t" + predicateCounter.get(p.getKey()) + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void writeMapToFile(Map<String, String> map, String filePath) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, true);
            map.entrySet().stream().forEach((p) ->
            {
                try {
                    fileWriter.append(p.getKey() + "\t" + p.getValue() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List getFilesInDirectory(String pathToDirectory) {
        List fileNames = new ArrayList();
        File directory = new File(pathToDirectory);
        for (final File fileEntry : directory.listFiles()) {
            //System.out.println(fileEntry.getName());
            if(fileEntry.getName().endsWith(".nq")) {
                fileNames.add(fileEntry.getName());
            }

        }
        return fileNames;

    }
}
