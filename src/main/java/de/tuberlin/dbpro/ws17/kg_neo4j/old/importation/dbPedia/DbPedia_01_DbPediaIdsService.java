package de.tuberlin.dbpro.ws17.kg_neo4j.old.importation.dbPedia;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DataProvider;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaId;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbProId;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DbPedia_01_DbPediaIdsService {

    private static String filePathTypePredicate = "/media/cfengler/Kingston DT HyperX 3.0/Predicates/_http___www.w3.org_1999_02_22-rdf-syntax-ns_type_.predicate";

    private static String filePathDbPediaIds = "/media/cfengler/MyPassport_1TB/Datenimport DbPedia/DbPediaIds.data";
    private static String filePathDataProviders = "/media/cfengler/MyPassport_1TB/Datenimport DbPedia/DataProviders.data";

    private static String dbPediaPrefix = "<http://dbpedia.org/resource/";
    private static String dbPediaOntologyCompany = "<http://dbpedia.org/ontology/Company>";

    private static Map<String, DataProvider> dataProviders = null;
    private static List<DataProvider> dataProvidersSorted = null;

    private static Map<String, DbPediaId> dbPediaIds = null;
    private static List<DbPediaId> dbPediaIdsSorted = null;
    private static Map<Long, DbProId> dbProIds = null;

    private static DataProvider dataProviderDbPro = null;

    //@Autowired
    //private static DataProviderRepository dataProviderRepository;

//    public static void main(String[] args) {
//        SpringApplication.run(DbPedia_01_DbPediaIdsService.class, args);
//    }
//
//    @Bean
//    CommandLineRunner test(DataProviderRepository dataProviderRepository) {
//        return args -> {
//            try {
//                dataProviderDbPro = new DataProvider("tuberlin.dbpro.kg_neo4j");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            dbPediaIds = new HashMap<>();
//            dataProviders = new HashMap<>();
//            dataProviders.put(dataProviderDbPro.getName(), dataProviderDbPro);
//            dbProIds = new HashMap<>();
//
//            gatherDbPediaIdsFromPredicateFile();
//
//            dbPediaIdsSorted = new ArrayList<>(dbPediaIds.values());
//            dbPediaIdsSorted.sort(new DbPediaIdsComparator());
//            createDbProIds();
//
//            dataProvidersSorted = new ArrayList<>(dataProviders.values());
//            dataProvidersSorted.sort(new DataProviderComparator());
//            saveDataProviders();
//
//            saveDbPediaIds();
//
//            long c = dataProviderRepository.count();
//
//            importDataProviders();
//            importDbProIds();
//            importDbPediaIds();
//        };
//    }

//    private static void gatherDbPediaIdsFromPredicateFile() {
//        try {
//            try (Stream<String> stream = Files.lines(Paths.get(filePathTypePredicate))) {
//                stream.forEach(line -> {
//                    if (line.startsWith(dbPediaPrefix)) {
//                        String[] lineSplit = line.split("\t");
//
//                        if (lineSplit[2].equals(dbPediaOntologyCompany)) {
//                            String dbPediaIdValue = ParseHelper.getDbPediaIdFromText(lineSplit[0]);
//                            String dataProviderName = ParseHelper.getDataProviderName(lineSplit[3]);
//
//                            if (!dataProviders.containsKey(dataProviderName)) {
//                                try {
//                                    dataProviders.put(dataProviderName, new DataProvider(dataProviderName));
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            if (!dbPediaIds.containsKey(dbPediaIdValue)) {
//                                try {
//                                    dbPediaIds.put(dbPediaIdValue, new DbPediaId(dbPediaIdValue));
//                                    dbPediaIds.get(dbPediaIdValue).getDataProviders().add(dataProviders.get(dataProviderName));
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                dbPediaIds.get(dbPediaIdValue).getDataProviders().add(dataProviders.get(dataProviderName));
//                            }
//                        }
//                    }
//                });
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private static void createDbProIds() {
//        for (int i = 0; i < dbPediaIdsSorted.size(); i++) {
//            DbPediaId dbPediaId = dbPediaIdsSorted.get(i);
//            try {
//                DbProId dbProId = new DbProId(i + 1, dataProviderDbPro);
//                dbPediaId.setDbProId(dbProId);
//                dbProIds.put(dbProId.getValue(), dbProId);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//    }

    private static void saveDataProviders() {
        try {
            try (FileWriter fileWriter = new FileWriter(filePathDataProviders)) {
                fileWriter.write("DataProvider" + System.lineSeparator());
                for (int i = 0; i < dataProvidersSorted.size(); i++) {
                    DataProvider dataProvider = dataProvidersSorted.get(i);

                    fileWriter.write(dataProvider.toString() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveDbPediaIds() {
        try {
            try (FileWriter fileWriter = new FileWriter(filePathDbPediaIds)) {
                fileWriter.write("DbProId;DbPediaId;DbPediaId_DataProviders" + System.lineSeparator());
                for (int i = 0; i < dbPediaIdsSorted.size(); i++) {
                    DbPediaId dbPediaId = dbPediaIdsSorted.get(i);

                    fileWriter.write(dbPediaId.getDbProId().toString() + ";"
                        + dbPediaId.toString() + ";"
                        + dbPediaId.getDataProviders().stream().map(DataProvider::getName).collect(Collectors.joining("|"))
                        + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void importDataProviders() {

    }

    private static void importDbProIds() {

    }

    private static void importDbPediaIds() {

    }

//    public static Set<DbPediaId> getDbPediaIds() {
//        Set<DbPediaId> result = new HashSet<>();
//
//        try {
//            try (Stream<String> stream = Files.lines(Paths.get(filePathDbPediaIds))) {
//                stream.skip(1).forEach(line -> {
//                    String[] lineSplit = line.split(";");
//
//                    DbPediaId dbPediaId = new DbPediaId();
//                    //dbPediaId.value =
//                    //result.add(dbPediaId);
//                });
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
}
