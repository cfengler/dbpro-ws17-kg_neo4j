package de.tuberlin.dbpro.ws17.kg_neo4j.transformator;

import de.tuberlin.dbpro.ws17.kg_neo4j.analysis.RDFEntity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class TransformatorService {

    public void createListOfAllAvailableObjects(String pathToDataDir) {

        Map<String, String> predicateNamesIdentifiers;
        // TODO: File exists -> load from file
        if(true) {
            predicateNamesIdentifiers = getAllPredicates(pathToDataDir);
        }
        createOrderedFileStructure(pathToDataDir, predicateNamesIdentifiers);
    }

    private Map getAllPredicates(String pathToDataDir) {
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
                            predicateProperty = predicateProperty.substring(0, predicateProperty.length()-2);
                            int size = predicateNamesIdentifiers.size();
                            predicateNamesIdentifiers.put(predicateProperty, predicate);
                            if(predicateNamesIdentifiers.size() > size) {
                                System.out.println(predicateProperty + " : " + predicate);
                            }

                        }
                        else {
                            predicateNamesIdentifiers.put("unbehandeltesFormat", line);
                        }


                        //System.out.println(line);
                        /*
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.matches()) {
                            String predicate = matcher.group("predicate");
                            String[] splittedPredicate = predicate.split("/");
                            String predicateProperty = splittedPredicate[splittedPredicate.length - 1];
                            predicateNamesIdentifiers.put(predicateProperty, predicate);
                        } else {
                            predicateNamesIdentifiers.put("unbehandeltesFormat", line);
                        }
                        */
                        synchronized (this) {
                            if (predicateNamesIdentifiers.size() > 100) {
                                writeMapToFile(predicateNamesIdentifiers, predicateFilePath);
                                predicateNamesIdentifiers.clear();
                            }
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        writeMapToFile(predicateNamesIdentifiers, predicateFilePath);

        return predicateNamesIdentifiers;
    }

    public void createOrderedFileStructure(String pathToDataDir, Map<String, String> predicateNamesIdentifiers) {
        predicateNamesIdentifiers.entrySet().stream().forEach((p) ->
        {
            createFileContainsOnlyPredicate(pathToDataDir, p.getKey(), p.getValue());
        });
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
