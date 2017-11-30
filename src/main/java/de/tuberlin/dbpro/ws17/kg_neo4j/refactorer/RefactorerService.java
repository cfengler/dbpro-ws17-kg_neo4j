package de.tuberlin.dbpro.ws17.kg_neo4j.refactorer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RefactorerService {


    public void createSameAsFile(String pathToDataDir) {
        //List<String> sameAsLines = new ArrayList();
        StringBuffer sameAsLines = new StringBuffer();
        List<String> fileNames = getFilesInDirectory(pathToDataDir);
        System.out.println("Found " + fileNames.size() + " Files in given directory.");
        System.out.println("Full Path to first file: " + pathToDataDir + fileNames.get(0));

        String sameAsFilePath = pathToDataDir + "sameAs.txt";

        fileNames.stream().forEach(fileName ->
                {
                    try {
                        try (Stream<String> stream = Files.lines(Paths.get(pathToDataDir + fileName))) {
                            System.out.println("[" + fileName + "] analysing");
                            stream.parallel().forEach(line -> {
                                if (line.contains("<http://www.w3.org/2002/07/owl#sameAs>")) {
                                    sameAsLines.append(line + "\n");
                                }
                                if(sameAsLines.length() > 100000) {
                                    writeListToFile(sameAsLines, sameAsFilePath);
                                    sameAsLines.setLength(0);
                                }

                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        writeListToFile(sameAsLines, sameAsFilePath);
    }

    public synchronized void writeListToFile(StringBuffer lines, String sameAsFilePath) {
        try {
            FileWriter fileWriter = new FileWriter(sameAsFilePath, true);
            fileWriter.append(lines.toString());
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
            fileNames.add(fileEntry.getName());
        }
        return fileNames;

    }
}
