package de.tuberlin.dbpro.ws17.kg_neo4j.services;

import de.tuberlin.dbpro.ws17.kg_neo4j.common.ParseHelper;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DataProvider;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaId;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbProId;
import de.tuberlin.dbpro.ws17.kg_neo4j.importation.DataProviderComparator;
import de.tuberlin.dbpro.ws17.kg_neo4j.importation.DbPediaIdsComparator;
import de.tuberlin.dbpro.ws17.kg_neo4j.importation.DbProIdsComparator;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DataProviderRepository;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbPediaIdRepository;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbProIdRepository;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class IdsService implements CommandLineRunner {

    private DataProviderRepository dataProviderRepository;
    private DbProIdRepository dbProIdRepository;
    private DbPediaIdRepository dbPediaIdRepository;
    private SessionFactory sessionFactory;

    private static String filePathTypePredicate = "/media/cfengler/Kingston DT HyperX 3.0/Predicates/_http___www.w3.org_1999_02_22-rdf-syntax-ns_type_.predicate";

    private static String filePathDataProviders = "/media/cfengler/MyPassport_1TB/Datenimport DbPedia/DataProviders.data";
    private static String filePathDbPediaIds = "/media/cfengler/MyPassport_1TB/Datenimport DbPedia/DbPediaIds.data";

    private static String dbPediaPrefix = "<http://dbpedia.org/resource/";
    private static String dbPediaOntologyCompany = "<http://dbpedia.org/ontology/Company>";

    private static Map<String, DataProvider> dataProviders = null;
    private static List<DataProvider> dataProvidersSorted = null;

    private static Map<String, DbPediaId> dbPediaIds = null;
    private static List<DbPediaId> dbPediaIdsSorted = null;

    private static Map<Long, DbProId> dbProIds = null;
    private static List<DbProId> dbProIdsSorted = null;

    private static DataProvider dataProviderDbPro = null;

    @Autowired
    public IdsService(DataProviderRepository dataProviderRepository,
                      DbProIdRepository dbProIdRepository,
                      DbPediaIdRepository dbPediaIdRepository,
                      SessionFactory sessionFactory) {
        this.dataProviderRepository = dataProviderRepository;
        this.dbProIdRepository = dbProIdRepository;
        this.dbPediaIdRepository = dbPediaIdRepository;

        this.sessionFactory = sessionFactory;
    }

    @Override
    public void run(String... strings) throws Exception {
        dataProviders = new HashMap<>();
        dbPediaIds = new HashMap<>();
        dbProIds = new HashMap<>();

        gatherDataProvidersFromFile();
        gatherDbProIdsAndDbPediaIdsFromPredicateFile();

        importNodes();
        importRelations();
        //Session session = sessionFactory.openSession();
        //session.save(dbPediaIdsSorted.subList(0, 1000));

        //session.save(dbPediaIdsSorted.subList(1000, 2000));

        //importDataProviders();
        //importDbProIds();
        //importDbPediaIds();
    }
//Code zum erstellen der Dateien aus der predicate-File
//    @Override
//    public void run(String... strings) throws Exception {
//        dataProviders = new HashMap<>();
//        dataProviderDbPro = new DataProvider("tuberlin.dbpro.kg_neo4j");
//        dataProviders.put(dataProviderDbPro.getName(), dataProviderDbPro);
//
//        dbPediaIds = new HashMap<>();
//        dbProIds = new HashMap<>();
//
//
//        gatherDbProIdsAndDbPediaIdsFromPredicateFile();
//
//        dbPediaIdsSorted = new ArrayList<>(dbPediaIds.values());
//        dbPediaIdsSorted.sort(new DbPediaIdsComparator());
//        createDbProIds();
//
//        dataProvidersSorted = new ArrayList<>(dataProviders.values());
//        dataProvidersSorted.sort(new DataProviderComparator());
//        saveDataProvidersToFile();
//
//        saveDbPediaIdsToFile();
//    }

    private void gatherDataProvidersFromFile() {
        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePathDataProviders))) {
                stream.skip(1).forEach(line -> {
                    dataProviders.put(line, new DataProvider(line));
                    if (line.equals("tuberlin.dbpro.kg_neo4j")) {
                        dataProviderDbPro = dataProviders.get(line);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        dataProvidersSorted = new ArrayList<>(dataProviders.values());
        dataProvidersSorted.sort(new DataProviderComparator());
    }

    private void gatherDbProIdsAndDbPediaIdsFromPredicateFile() {
        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePathDbPediaIds))) {
                stream.skip(1).forEach(line -> {
                    String[] lineSplit = line.split(";");

                    DbProId dbProId = new DbProId(Long.parseLong(lineSplit[0]), dataProviderDbPro);

                    DbPediaId dbPediaId = new DbPediaId(lineSplit[1]);
//                    String[] dataProviderNames = lineSplit[2].split("\\|");
//                    for (String dataProviderName:dataProviderNames) {
//                        dbPediaId.getDataProviders().add(dataProviders.get(dataProviderName));
//                    }

                    //dbPediaId.setDbProId(dbProId);
                    //dbProId.setDbPediaId(dbPediaId);

                    dbPediaIds.put(dbPediaId.getValue(), dbPediaId);
                    dbProIds.put(dbProId.getValue(), dbProId);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        dbProIdsSorted = new ArrayList<>(dbProIds.values());
        dbProIdsSorted.sort(new DbProIdsComparator());

        dbPediaIdsSorted = new ArrayList<>(dbPediaIds.values());
        dbPediaIdsSorted.sort(new DbPediaIdsComparator());
    }

    private void gatherDbPediaIdsFromPredicateFile() {
        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePathTypePredicate))) {
                stream.forEach(line -> {
                    if (line.startsWith(dbPediaPrefix)) {
                        String[] lineSplit = line.split("\t");

                        if (lineSplit[2].equals(dbPediaOntologyCompany)) {
                            String dbPediaIdValue = ParseHelper.getDbPediaIdFromText(lineSplit[0]);
                            String dataProviderName = ParseHelper.getDataProviderName(lineSplit[3]);

                            if (!dataProviders.containsKey(dataProviderName)) {
                                try {
                                    dataProviders.put(dataProviderName, new DataProvider(dataProviderName));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if (!dbPediaIds.containsKey(dbPediaIdValue)) {
                                try {
                                    dbPediaIds.put(dbPediaIdValue, new DbPediaId(dbPediaIdValue));
                                    dbPediaIds.get(dbPediaIdValue).getDataProviders().add(dataProviders.get(dataProviderName));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                dbPediaIds.get(dbPediaIdValue).getDataProviders().add(dataProviders.get(dataProviderName));
                            }
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDbProIds() {
        for (int i = 0; i < dbPediaIdsSorted.size(); i++) {
            DbPediaId dbPediaId = dbPediaIdsSorted.get(i);
            try {
                DbProId dbProId = new DbProId(i + 1, dataProviderDbPro);
                dbPediaId.setDbProId(dbProId);
                dbProIds.put(dbProId.getValue(), dbProId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void saveDataProvidersToFile() {
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

    private void saveDbPediaIdsToFile() {
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

    private void importNodes() {
        dataProviderRepository.saveAll(dataProvidersSorted);
        importDbProIdsInChunks();
        importDbPediaIdsInChunks();
        //dbProIdRepository.saveAll(dbProIdsSorted);
        //dbPediaIdRepository.saveAll(dbPediaIdsSorted);
    }

    private void importRelations() {

    }

    private void importDataProviders() {
        dataProviderRepository.saveAll(dataProvidersSorted);
    }

    private void importDbProIdsInChunks() {
        System.out.println("DbProIds");
        List<DbProId> tempChunkList = new ArrayList<>();

        for (int i = 0; i < dbProIdsSorted.size(); i++) {

            tempChunkList.add(dbProIdsSorted.get(i));

            if (tempChunkList.size() == 500) {
                System.out.println((i + 1) + "/" + dbProIdsSorted.size());
                dbProIdRepository.saveAll(tempChunkList);
                tempChunkList.clear();
            }
        }

        if (tempChunkList.size() > 0) {
            dbProIdRepository.saveAll(tempChunkList);
            tempChunkList.clear();
        }
    }

    private void importDbPediaIdsInChunks() {
        System.out.println("DbPediaIds");
        List<DbPediaId> tempChunkList = new ArrayList<>();

        for (int i = 0; i < dbPediaIdsSorted.size(); i++) {

            tempChunkList.add(dbPediaIdsSorted.get(i));

            if (tempChunkList.size() == 500) {
                System.out.println((i + 1) + "/" + dbPediaIdsSorted.size());
                dbPediaIdRepository.saveAll(tempChunkList);
                tempChunkList.clear();
            }
        }

        if (tempChunkList.size() > 0) {
            dbPediaIdRepository.saveAll(tempChunkList);
            tempChunkList.clear();
        }
    }

    public static <T> Stream<List<T>> batches(List<T> source, int length) {
        if (length <= 0)
            throw new IllegalArgumentException("length = " + length);
        int size = source.size();
        if (size <= 0)
            return Stream.empty();
        int fullChunks = (size - 1) / length;
        return IntStream.range(0, fullChunks + 1).mapToObj(
            n -> source.subList(n * length, n == fullChunks ? size : (n + 1) * length));
    }
}
