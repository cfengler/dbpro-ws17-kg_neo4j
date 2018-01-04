package de.tuberlin.dbpro.ws17.kg_neo4j.importation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class GcdIdsService {
    private static String filePathGcdIds = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport/01_GcdIds.data";

    public static Set<Long> getGcdIds() {
        Set<Long> result = new HashSet<>();

        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePathGcdIds))) {
                stream.forEach(line -> {
                    Long gcdId = Long.parseLong(line);
                    if (!result.contains(gcdId)) {
                        result.add(gcdId);
                    }
                    else {
                        System.out.println("hier stimmt was nicht: " + gcdId);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
