package de.tuberlin.dbpro.ws17.kg_neo4j.services;

import de.tuberlin.dbpro.ws17.kg_neo4j.common.ParseHelper;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DataProvider;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaId;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaLabel;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbProId;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DataProviderRepository;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbPediaIdRepository;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbPediaLabelRepository;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbProIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Component
@Scope(value = "singleton")
public class LabelsService {

    private IdsService idsService;
    private DataProviderRepository dataProviderRepository;
    private DbProIdRepository dbProIdRepository;
    private DbPediaIdRepository dbPediaIdRepository;
    private DbPediaLabelRepository dbPediaLabelRepository;

    @Autowired
    public LabelsService(IdsService idsService,
                         DataProviderRepository dataProviderRepository,
                         DbProIdRepository dbProIdRepository,
                         DbPediaIdRepository dbPediaIdRepository,
                         DbPediaLabelRepository dbPediaLabelRepository) {
        this.idsService = idsService;
        this.dataProviderRepository = dataProviderRepository;
        this.dbProIdRepository = dbProIdRepository;
        this.dbPediaIdRepository = dbPediaIdRepository;
        this.dbPediaLabelRepository = dbPediaLabelRepository;
    }

    Map<String, DataProvider> dataProviderMap = null;

    public void importNodesAndRelations() {

        initDataProviderMap();
        //TODO: DbPediaIdsService gibt Map aus Datei erstellt zurück
        Set<String> dbPediaIds = idsService.getDbPediaIdsFromFile();
        //TODO: alle Labels aus der Datei auslesen welche einen Match in der Map haben
        Map<String, List<DbPediaLabel>> dbPediaLabelsByDbPediaId = getDbPediaLabelsByDbPediaIdFromPredicateFile(dbPediaIds);
        //TODO: erstelle Knoten für die entsprechenden Labels

        importNodes(dbPediaLabelsByDbPediaId);
        //TODO: erstelle Kanten für die entsprechenden Labels
    }

    private void initDataProviderMap() {
        dataProviderMap = new HashMap<>();
        Iterable<DataProvider> dataProvidersIterable = dataProviderRepository.findAll();
        for (DataProvider dataProvider : dataProvidersIterable) {
            dataProviderMap.put(dataProvider.getName(), dataProvider);
        }
    }

    private String filePathLabelPredicate = "/media/cfengler/Kingston DT HyperX 3.0/Predicates/_http___www.w3.org_2000_01_rdf-schema_label_.predicate";
    //private Map<String, List<DbPediaLabel>> labelsByDbPediaId = null;

    private Map<String, List<DbPediaLabel>> getDbPediaLabelsByDbPediaIdFromPredicateFile(Set<String> dbPediaIds) {
        Map<String, List<DbPediaLabel>> result = new HashMap<>();

        for (String dbPediaIdString:dbPediaIds) {
            result.put(dbPediaIdString, new ArrayList<>());
        }

        try {
            try (Stream<String> stream = Files.lines(Paths.get(filePathLabelPredicate))) {
                stream.forEach(line -> {
                    if (line.startsWith("<http://dbpedia.org/resource/")) {
                        String[] lineSplit = line.split("\t");

                        String dbPediaIdString = ParseHelper.getDbPediaIdFromText(lineSplit[0]);

                        if (result.containsKey(dbPediaIdString)) {
                            String labelValue = ParseHelper.getDbPediaLabelValue(lineSplit[2]);
                            String labelLanguageKey =  ParseHelper.getDbPediaLabelLanguageKey(lineSplit[2]);
                            String dataProviderName = ParseHelper.getDataProviderName(lineSplit[3]);

                            DbPediaLabel dbPediaLabel = new DbPediaLabel();
                            dbPediaLabel.setValue(labelValue);
                            dbPediaLabel.setLanguageKey(labelLanguageKey);
                            dbPediaLabel.setDataProvider(dataProviderMap.get(dataProviderName));

                            result.get(dbPediaIdString).add(dbPediaLabel);
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void importNodes(Map<String, List<DbPediaLabel>> dbPediaLabelsByDbPediaId) {
        int page = 0;

        Page<DbPediaId> dbPediaIds = dbPediaIdRepository.findAll(PageRequest.of(page, 1000, Sort.Direction.ASC, "value"));
        importNodesForDbPediaIds(dbPediaLabelsByDbPediaId, dbPediaIds);
        while (dbPediaIds.hasNext()) {
            dbPediaIds = dbPediaIdRepository.findAll(dbPediaIds.nextPageable());
            importNodesForDbPediaIds(dbPediaLabelsByDbPediaId, dbPediaIds);
        }

        importNodesInChunks(dbPediaLabelsByDbPediaId);
    }

    private void importNodesForDbPediaIds(Map<String, List<DbPediaLabel>> dbPediaLabelsByDbPediaId, Page<DbPediaId> dbPediaIds) {

        for (DbPediaId dbPediaId:dbPediaIds) {
            if (dbPediaLabelsByDbPediaId.containsKey(dbPediaId.getValue())) {
                for (DbPediaLabel dbPediaLabel:dbPediaLabelsByDbPediaId.get(dbPediaId.getValue())) {
                    dbPediaLabel.setDbProId(dbPediaId.getDbProId());
                }
            }
        }
    }

    private void importNodesInChunks(Map<String, List<DbPediaLabel>> dbPediaLabelsByDbPediaId) {
        List<DbPediaLabel> tempChunkList = new ArrayList<>();

        for (List<DbPediaLabel> dbPediaLabels : dbPediaLabelsByDbPediaId.values()) {
            tempChunkList.addAll(dbPediaLabels);

            if (tempChunkList.size() > 500) {
                dbPediaLabelRepository.saveAll(tempChunkList);
                tempChunkList.clear();
            }
        }

        if (tempChunkList.size() > 0) {
            dbPediaLabelRepository.saveAll(tempChunkList);
            tempChunkList.clear();
        }
    }
//    private static String filePathDbPediaLabels = "/media/cfengler/MyPassport_1TB/Datenimport DbPedia/DbPediaLabels.data";
//
//    private void saveDbPediaLabelsToFile() {
//        try {
//            try (FileWriter fileWriter = new FileWriter(filePathDbPediaLabels)) {
//
//                for (String dbPediaId:labelsByDbPediaId.keySet()) {
//
//                    for (DbPediaLabel dbPedialabel:labelsByDbPediaId.get(dbPediaId)) {
//                        fileWriter.write(dbPediaId + ";" + dbPedialabel.toString() + System.lineSeparator());
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
