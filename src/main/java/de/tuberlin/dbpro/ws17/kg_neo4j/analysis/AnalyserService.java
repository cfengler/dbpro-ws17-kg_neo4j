package de.tuberlin.dbpro.ws17.kg_neo4j.analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class AnalyserService {


    public Set getIdentifiersFromFile(Set<String> ids, String pathToDataDir) {
        //Set<String> equalEntityIds = new HashSet<>();
        List<String> linesContainsStartIDAndSameAsProperty = fetchLinesContainingIDAndSameAsProperty(ids, pathToDataDir);

        /*
            Test values to skip file reading

        List<String> linesContainsStartIDAndSameAsProperty = new LinkedList<>();
        linesContainsStartIDAndSameAsProperty.add("<http://corp.dbpedia.org/resource/gcd_f588>\t<http://www.w3.org/2002/07/owl#sameAs>\t<http://corp.dbpedia.org/resource/permid_4295868979>\t<http://dfki.gcd.de> .");
        linesContainsStartIDAndSameAsProperty.add("<http://corp.dbpedia.org/resource/permid_4295868979>\t<http://www.w3.org/2002/07/owl#sameAs>\t<http://corp.dbpedia.org/resource/gcd_f588>\t<http://permid.org> .");
        linesContainsStartIDAndSameAsProperty.add("<http://corp.dbpedia.org/resource/permid_4295868979>\t<http://www.w3.org/2002/07/owl#sameAs>\t<https://permid.org/1-4295868979>\t<http://permid.org> .");
        linesContainsStartIDAndSameAsProperty.add("<http://corp.dbpedia.org/resource/permid_4295868979>\t<http://www.w3.org/2002/07/owl#sameAs>\t<http://corp.dbpedia.org/resource/grid_425362_4>\t<http://permid.org> .");
        */
        /*
        List<String> linesContainsStartIDAndSameAsProperty = new LinkedList<>();
        linesContainsStartIDAndSameAsProperty.add("<http://corp.dbpedia.org/resource/gcd_40245>\t<http://www.w3.org/2002/07/owl#sameAs>\t<http://corp.dbpedia.org/resource/permid_4295870332>\t<http://dfki.gcd.de> .");
        linesContainsStartIDAndSameAsProperty.add("<http://corp.dbpedia.org/resource/gcd_f48>\t<http://www.w3.org/2002/07/owl#sameAs>\t<http://corp.dbpedia.org/resource/permid_4295870332>\t<http://dfki.gcd.de> .");
        linesContainsStartIDAndSameAsProperty.add("<http://corp.dbpedia.org/resource/permid_4295870332>	<http://www.w3.org/2002/07/owl#sameAs>	<http://corp.dbpedia.org/resource/gcd_40245>	<http://permid.org> .");
        linesContainsStartIDAndSameAsProperty.add("<http://corp.dbpedia.org/resource/permid_4295870332>\t<http://www.w3.org/2002/07/owl#sameAs>\t<http://corp.dbpedia.org/resource/gcd_f48>\t<http://permid.org> .");
        linesContainsStartIDAndSameAsProperty.add("<http://corp.dbpedia.org/resource/permid_4295870332>\t<http://www.w3.org/2002/07/owl#sameAs>\t<https://permid.org/1-4295870332>\t<http://permid.org> .");
        */

        List<RDFEntity> rdfList = getRDFEntitiesFromLines(linesContainsStartIDAndSameAsProperty);

        rdfList.stream().forEach( rdfEntity ->
        {
            try {
                //System.out.println(rdfEntity.subject);
                //System.out.println(rdfEntity.object);
                if(!rdfEntity.subject.contains("owl#sameAs")) {
                    ids.add(rdfEntity.subject);
                }
                if(!rdfEntity.object.contains("owl#sameAs")) {
                    ids.add(rdfEntity.object);
                }

            } catch (Exception e) {

            }


        });

        return ids;
    }

    public List getLinesContainingEntityIds(String pathToDataDir, Set<String> ids) {
        List<String> linesContainsID = new ArrayList();
        List<String> fileNames = getFilesInDirectory(pathToDataDir);
        System.out.println("Found " + fileNames.size() + " Files in given directory.");
        System.out.println("Full Path to first file: " + pathToDataDir + fileNames.get(0));


        String analysisFile = pathToDataDir + "analysis_equivalent_" + ids.iterator().next() + ".txt";

        fileNames.stream().forEach(fileName -> {
            try {
                try (Stream<String> stream = Files.lines(Paths.get(pathToDataDir + fileName))) {
                    System.out.println("[" + fileName + "] analysing");
                    stream.parallel().forEach(line -> {
                        ids.parallelStream().forEach(id ->
                        {
                            if (line.contains(id)) {
                                linesContainsID.add(fileName + "   " + line);
                            }
                        });

                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        return linesContainsID;

    }

    private List getRDFEntitiesFromLines(List<String> linesContainsStartIDAndSameAsProperty) {
        List<RDFEntity> rdfList = new ArrayList<>();
        Pattern pattern = Pattern.compile("[<](?<subject>.+?)[>]\\s[<](?<predicate>.+?)[>]\\s[<](?<object>.+?)[>]\\s[<](?<source>.+?)[>]\\s.");
        // <http://corp.dbpedia.org/resource/permid_4295868979>	<http://www.w3.org/2002/07/owl#sameAs>	<http://corp.dbpedia.org/resource/grid_425362_4>	<http://permid.org>
        linesContainsStartIDAndSameAsProperty.parallelStream().forEach(line ->
                {
                    //System.out.println(line);
                    Matcher matcher = pattern.matcher(line);
                    if(matcher.matches()) {

                        String subject = matcher.group("subject");
                        String[] splittedSubject = subject.split("/");
                        String subjectId = splittedSubject[splittedSubject.length-1];


                        String predicate = matcher.group("predicate");
                        String[] splittedPredicate = predicate.split("/");
                        String predicateProperty = splittedPredicate[splittedPredicate.length-1];
                        if(predicateProperty.equals("owl#sameAs")) predicateProperty = "sameAs";


                        String object = matcher.group("object");
                        String[] splittedObject = object.split("/");
                        String objectId = splittedObject[splittedObject.length-1];


                        String source = matcher.group("source");
                        String[] splittedSource = source.split("/");
                        String sourceValue = splittedSource[splittedSource.length-1];
                        RDFEntity rdf = new RDFEntity(subjectId, predicateProperty, objectId, sourceValue);
                        rdfList.add(rdf);

                        //System.out.println(rdf);
                    }
                    else {
                        System.out.println("Unbhandeltes Format: " + line);
                    }

                }

        );
        return rdfList;
    }


    private List fetchLinesContainingIDAndSameAsProperty(Set<String> ids, String pathToDataDir) {
        List<String> linesContainsStartIDAndSameAsProperty = new ArrayList();
        List<String> fileNames = getFilesInDirectory(pathToDataDir, "sameAs");
        System.out.println("Found " + fileNames.size() + " Files in given directory.");
        System.out.println("Full Path to first file: " + pathToDataDir + fileNames.get(0));

        fileNames.stream().forEach(fileName -> {
            try {
                try (Stream<String> stream = Files.lines(Paths.get(pathToDataDir + fileName))) {
                    System.out.println("[" + fileName + "] analysing");
                    stream.parallel().forEach(line -> {
                        ids.parallelStream().forEach(id ->
                        {
                            if (line.contains(id) && line.contains("<http://www.w3.org/2002/07/owl#sameAs>") && !line.contains("rdf.freebase.com")) {
                                linesContainsStartIDAndSameAsProperty.add(line);
                            }
                        });
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        /*String analysisFile = pathToDataDir + "analysis_stringContains_" + ids.iterator().next() + ".txt";

        try {
            FileWriter fileWriter = new FileWriter(analysisFile);
            fileWriter.write(String.join(System.lineSeparator(), linesContainsStartIDAndSameAsProperty));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        return linesContainsStartIDAndSameAsProperty;
    }

    public List getFilesInDirectory(String pathToDirectory, String type) {
        List fileNames = new ArrayList();
        File directory = new File(pathToDirectory);
        for (final File fileEntry : directory.listFiles()) {
            if(fileEntry.getName().contains(type)) {
                fileNames.add(fileEntry.getName());
            }
        }
        return fileNames;

    }

    public List getFilesInDirectory(String pathToDirectory) {
        List fileNames = new ArrayList();
        File directory = new File(pathToDirectory);
        for (final File fileEntry : directory.listFiles()) {
            //System.out.println(fileEntry.getName());
            fileNames.add(fileEntry.getName());
        }
        return fileNames;

    }
}
