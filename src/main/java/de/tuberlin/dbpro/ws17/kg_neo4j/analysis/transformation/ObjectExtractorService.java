package de.tuberlin.dbpro.ws17.kg_neo4j.analysis.transformation;

import de.tuberlin.dbpro.ws17.kg_neo4j.model.RdfQuadrupel;
import org.apache.commons.text.StringEscapeUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ObjectExtractorService {

    private static String objectToSearch = "<http://dbpedia.org/ontology/Company>";

    private static String filePathPredicate = "/media/cfengler/Kingston DT HyperX 3.0/Predicates/_http___www.w3.org_1999_02_22-rdf-syntax-ns_type_.predicate";
    private static String filePathExtraction = null;

    public static void main(String[] args) {
        filePathExtraction = filePathPredicate.replace(".predicate", convertStringToFileName(objectToSearch) + ".extraction");

        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePathPredicate))) {
                FileWriter fileWriter = new FileWriter(filePathExtraction);

                stream.forEach(line -> {
                    RdfQuadrupel rdfQuadrupel = new RdfQuadrupel(line);
                    if (rdfQuadrupel.object.equals(objectToSearch)) {
                        try {
                            fileWriter.write(line + System.lineSeparator());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                fileWriter.close();
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
