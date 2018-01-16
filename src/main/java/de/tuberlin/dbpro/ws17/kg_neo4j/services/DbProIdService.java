package de.tuberlin.dbpro.ws17.kg_neo4j.services;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaId;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbProId;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DataProviderRepository;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbPediaIdRepository;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbProIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope(value = "singleton")
public class DbProIdService {

    private DataProviderRepository dataProviderRepository;
    private DbProIdRepository dbProIdRepository;
    private DbPediaIdRepository dbPediaIdRepository;

    private Map<Long, DbProId> dbProIdsByDbProIdValue = null;

    @Autowired
    public DbProIdService(DataProviderRepository dataProviderRepository,
                          DbProIdRepository dbProIdRepository,
                          DbPediaIdRepository dbPediaIdRepository) {
        this.dataProviderRepository = dataProviderRepository;
        this.dbProIdRepository = dbProIdRepository;
        this.dbPediaIdRepository = dbPediaIdRepository;

        dbProIdsByDbProIdValue = new HashMap<>();
    }

    public Map<String, DbProId> getDbProIdsByDbPediaIdValue() {
        long start = System.currentTimeMillis();

        Map<String, DbProId> result = new HashMap<>();

        Page<DbProId> dbProIdsTemp = dbProIdRepository.findAll(PageRequest.of(0, 2000));

        for (DbProId dbProId:dbProIdsTemp) {
            result.put(dbProId.getDbPediaId().getValue(), dbProId);
        }

        while (dbProIdsTemp.hasNext()) {
            dbProIdsTemp = dbProIdRepository.findAll(dbProIdsTemp.nextPageable());

            for (DbProId dbProId:dbProIdsTemp) {
                result.put(dbProId.getDbPediaId().getValue(), dbProId);
            }
        }

        System.out.println("getDbProIdsByDbPediaIdValue need " + (System.currentTimeMillis() - start) + " ms.");

        return result;
    }

    public Map<String, DbProId> getDbProIdsByDbPediaIdValueFast() {
        long start = System.currentTimeMillis();

        Map<String, DbProId> result = new HashMap<>();

        Page<DbPediaId> dbPediaIdsTemp = dbPediaIdRepository.findAll(PageRequest.of(0, 10000));

        for (DbPediaId dbPediaId:dbPediaIdsTemp) {
            result.put(dbPediaId.getValue(), dbPediaId.getDbProId());
        }

        while (dbPediaIdsTemp.hasNext()) {
            dbPediaIdsTemp = dbPediaIdRepository.findAll(dbPediaIdsTemp.nextPageable());

            for (DbPediaId dbPediaId:dbPediaIdsTemp) {
                result.put(dbPediaId.getValue(), dbPediaId.getDbProId());
            }
        }

        System.out.println("getDbProIdsFastWithDbPediaIdRepository need " + (System.currentTimeMillis() - start) + " ms.");

        return result;
    }

    public DbProId findDbProIdByValue(Long value) {
        if (dbProIdsByDbProIdValue.containsKey(value)) {
            return dbProIdsByDbProIdValue.get(value);
        }

        DbProId dbProId = dbProIdRepository.findByValue(value);
        if (dbProId != null) {
            dbProIdsByDbProIdValue.put(value, dbProId);
            return dbProIdsByDbProIdValue.get(value);
        }

        return null;
    }

    public List<DbProId> findDbProIdsByValueIn(Iterable<Long> values) {
        List<DbProId> result = new ArrayList<>();
        List<Long> dbProIdValuesNotRetrieved = new ArrayList<>();

        for (long dbProIdValue:values) {
            if (dbProIdsByDbProIdValue.containsKey(dbProIdValue)) {
                result.add(dbProIdsByDbProIdValue.get(dbProIdValue));
            }
            else {
                dbProIdValuesNotRetrieved.add(dbProIdValue);
            }
        }

        if (dbProIdValuesNotRetrieved.size() == 0) {
            return result;
        }
        else {
            List<DbProId> loadedDbProIds = dbProIdRepository.findByValueIn(dbProIdValuesNotRetrieved);
            for (DbProId dbProId:loadedDbProIds) {
                dbProIdsByDbProIdValue.put(dbProId.getValue(), dbProId);
            }
            result.addAll(loadedDbProIds);
            return result;
        }
    }

//    private static String filePathDataProviders = "/media/cfengler/MyPassport_1TB/Datenimport DbPedia/DataProviders.data";
//    private static String filePathDbPediaIds = "/media/cfengler/MyPassport_1TB/Datenimport DbPedia/DbPediaIds.data";
//
//    private static String dbProDataProviderName = "tuberlin.dbpro.kg_neo4j";
//    private static Map<String, DataProvider> dataProviders = null;
//    private static List<DataProvider> dataProvidersSorted = null;
//
//    private static Map<String, DbPediaId> dbPediaIds = null;
//    private static List<DbPediaId> dbPediaIdsSorted = null;
//
//    private static Map<Long, DbProId> dbProIds = null;
//    private static List<DbProId> dbProIdsSorted = null;
//
//    private static DataProvider dataProviderDbPro = null;

//    public void importNodesAndRelations() {
//        dataProviders = new HashMap<>();
//        dbPediaIds = new HashMap<>();
//        dbProIds = new HashMap<>();
//
//        gatherNodes();
//
//        dataProvidersSorted = new ArrayList<>(dataProviders.values());
//        dataProvidersSorted.sort(new DataProviderComparator());
//        dbProIdsSorted = new ArrayList<>(dbProIds.values());
//        dbProIdsSorted.sort(new DbProIdsComparator());
//        dbPediaIdsSorted = new ArrayList<>(dbPediaIds.values());
//        dbPediaIdsSorted.sort(new DbPediaIdsComparator());
//
//        importNodes();
//
//        gatherDbProIdsRelations();
//        importDbProIdsInChunks();
//
//        gatherDbPediaIdsRelations();
//        importDbPediaIdsInChunks();
//    }

//    private void gatherNodes() {
//        try {
//
//            try (Stream<String> stream = Files.lines(Paths.get(filePathDataProviders))) {
//                stream.skip(1).forEach(line -> {
//                    DataProvider dataProvider = new DataProvider();
//                    dataProvider.setName(line);
//                    dataProviders.put(line, dataProvider);
//                });
//            }
//
//            dataProviderDbPro = dataProviders.get(dbProDataProviderName);
//
//            AtomicInteger ai = new AtomicInteger(0);
//            try (Stream<String> stream = Files.lines(Paths.get(filePathDbPediaIds))) {
//                stream.skip(1).forEach(line -> {
//                    ai.set(ai.get() + 1);
//                    String[] lineSplit = line.split(";");
//
//                    DbProId dbProId = new DbProId();
//                    dbProId.setValue(Long.parseLong(lineSplit[0]));
//                    dbProId.setDataProvider(dataProviderDbPro);
//                    if (dbProIds.containsKey(dbProId.getValue())) {
//                        System.out.println(line);
//                    }
//                    dbProIds.put(dbProId.getValue(), dbProId);
//
//                    DbPediaId dbPediaId = new DbPediaId();
//                    dbPediaId.setValue(lineSplit[1]);
//                    if (dbPediaIds.containsKey(dbPediaId.getValue())) {
//                        System.out.println(line);
//                    }
//                    dbPediaIds.put(dbPediaId.getValue(), dbPediaId);
//                });
//            }
//            System.out.println(ai.get());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

//    private void importNodes() {
//        dataProviderRepository.saveAll(dataProvidersSorted);
//        importDbProIdsInChunks();
//        importDbPediaIdsInChunks();
//    }
//
//    private void gatherDbProIdsRelations() {
//        try {
//            try (Stream<String> stream = Files.lines(Paths.get(filePathDbPediaIds))) {
//                stream.skip(1).forEach(line -> {
//                    String[] lineSplit = line.split(";");
//                    DbProId dbProId = dbProIds.get(Long.parseLong(lineSplit[0]));
//                    DbPediaId dbPediaId = dbPediaIds.get(lineSplit[1]);
//
//                    dbProId.setDbPediaId(dbPediaId);
//                    dbProId.setDataProvider(dataProviderDbPro);
//                });
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void gatherDbPediaIdsRelations() {
//        try {
//            try (Stream<String> stream = Files.lines(Paths.get(filePathDbPediaIds))) {
//                stream.skip(1).forEach(line -> {
//                    String[] lineSplit = line.split(";");
//
//                    DbPediaId dbPediaId = dbPediaIds.get(lineSplit[1]);
//
//                    String[] dataProviderNames = lineSplit[2].split("\\|");
//                    for (String dataProviderName:dataProviderNames) {
//                        dbPediaId.getDataProviders().add(dataProviders.get(dataProviderName));
//                    }
//                });
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private void importDbProIdsInChunks() {
//        System.out.println("DbProIds");
//        List<DbProId> tempChunkList = new ArrayList<>();
//
//        for (int i = 0; i < dbProIdsSorted.size(); i++) {
//
//            tempChunkList.add(dbProIdsSorted.get(i));
//
//            if (tempChunkList.size() == 500) {
//                System.out.println((i + 1) + "/" + dbProIdsSorted.size());
//                dbProIdRepository.saveAll(tempChunkList);
//                tempChunkList.clear();
//            }
//        }
//
//        if (tempChunkList.size() > 0) {
//            dbProIdRepository.saveAll(tempChunkList);
//            tempChunkList.clear();
//        }
//    }
//
//    private void importDbPediaIdsInChunks() {
//        System.out.println("DbPediaIds");
//        List<DbPediaId> tempChunkList = new ArrayList<>();
//
//        for (int i = 0; i < dbPediaIdsSorted.size(); i++) {
//
//            tempChunkList.add(dbPediaIdsSorted.get(i));
//
//            if (tempChunkList.size() == 500) {
//                System.out.println((i + 1) + "/" + dbPediaIdsSorted.size());
//                dbPediaIdRepository.saveAll(tempChunkList);
//                tempChunkList.clear();
//            }
//        }
//
//        if (tempChunkList.size() > 0) {
//            dbPediaIdRepository.saveAll(tempChunkList);
//            tempChunkList.clear();
//        }
//    }
//
//    public Set<String> getDbPediaIdsFromFile() {
//        Set<String> result = new HashSet<>();
//        try {
//            try (Stream<String> stream = Files.lines(Paths.get(filePathDbPediaIds))) {
//                stream.skip(1).forEach(line -> {
//                    String[] lineSplit = line.split(";");
//
//                    result.add(lineSplit[1]);
//                });
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    //private static String filePathTypePredicate = "/media/cfengler/Kingston DT HyperX 3.0/Predicates/_http___www.w3.org_1999_02_22-rdf-syntax-ns_type_.predicate";

    //private static String dbPediaPrefix = "<http://dbpedia.org/resource/";
    //private static String dbPediaOntologyCompany = "<http://dbpedia.org/ontology/Company>";

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

//    private void gatherDataProvidersFromFile() {
//        try {
//            try (Stream<String> stream = Files.lines(Paths.get(filePathDataProviders))) {
//                stream.skip(1).forEach(line -> {
//                    dataProviders.put(line, new DataProvider(line));
//                    if (line.equals("tuberlin.dbpro.kg_neo4j")) {
//                        dataProviderDbPro = dataProviders.get(line);
//                    }
//                });
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        dataProvidersSorted = new ArrayList<>(dataProviders.values());
//        dataProvidersSorted.sort(new DataProviderComparator());
//    }

//    private void gatherDbProIdsAndDbPediaIdsFromPredicateFile() {
//        try {
//            try (Stream<String> stream = Files.lines(Paths.get(filePathDbPediaIds))) {
//                stream.skip(1).forEach(line -> {
//                    String[] lineSplit = line.split(";");
//
//                    DbProId dbProId = new DbProId(Long.parseLong(lineSplit[0]), dataProviderDbPro);
//
//                    DbPediaId dbPediaId = new DbPediaId(lineSplit[1]);
////                    String[] dataProviderNames = lineSplit[2].split("\\|");
////                    for (String dataProviderName:dataProviderNames) {
////                        dbPediaId.getDataProviders().add(dataProviders.get(dataProviderName));
////                    }
//
//                    //dbPediaId.setDbProId(dbProId);
//                    //dbProId.setDbPediaId(dbPediaId);
//
//                    dbPediaIds.put(dbPediaId.getValue(), dbPediaId);
//                    dbProIds.put(dbProId.getValue(), dbProId);
//                });
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        dbProIdsSorted = new ArrayList<>(dbProIds.values());
//        dbProIdsSorted.sort(new DbProIdsComparator());
//
//        dbPediaIdsSorted = new ArrayList<>(dbPediaIds.values());
//        dbPediaIdsSorted.sort(new DbPediaIdsComparator());
//    }

//    private void gatherDbPediaIdsFromPredicateFile() {
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
//
//    private void createDbProIds() {
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
//
//    private void saveDataProvidersToFile() {
//        try {
//            try (FileWriter fileWriter = new FileWriter(filePathDataProviders)) {
//                fileWriter.write("DataProvider" + System.lineSeparator());
//                for (int i = 0; i < dataProvidersSorted.size(); i++) {
//                    DataProvider dataProvider = dataProvidersSorted.get(i);
//
//                    fileWriter.write(dataProvider.toString() + System.lineSeparator());
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void saveDbPediaIdsToFile() {
//        try {
//            try (FileWriter fileWriter = new FileWriter(filePathDbPediaIds)) {
//                fileWriter.write("DbProId;DbPediaId;DbPediaId_DataProviders" + System.lineSeparator());
//                for (int i = 0; i < dbPediaIdsSorted.size(); i++) {
//                    DbPediaId dbPediaId = dbPediaIdsSorted.get(i);
//
//                    fileWriter.write(dbPediaId.getDbProId().toString() + ";"
//                        + dbPediaId.toString() + ";"
//                        + dbPediaId.getDataProviders().stream().map(DataProvider::getName).collect(Collectors.joining("|"))
//                        + System.lineSeparator());
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
