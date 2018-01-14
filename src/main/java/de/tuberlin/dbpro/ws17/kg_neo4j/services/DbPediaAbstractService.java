package de.tuberlin.dbpro.ws17.kg_neo4j.services;

import de.tuberlin.dbpro.ws17.kg_neo4j.common.ParseHelper;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DataProvider;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaAbstract;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbProId;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DataProviderRepository;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbPediaAbstractRepository;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbPediaIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@Scope(value = "singleton")
public class DbPediaAbstractService {

    private DataProviderService dataProviderService = null;
    private DbProIdService dbProIdService = null;
    private DbPediaAbstractRepository dbPediaAbstractRepository = null;

    @Autowired
    public DbPediaAbstractService(DataProviderService dataProviderService,
                                  DbProIdService dbProIdService,
                                  DbPediaAbstractRepository dbPediaAbstractRepository) {
        this.dataProviderService = dataProviderService;
        this.dbProIdService = dbProIdService;
        this.dbPediaAbstractRepository = dbPediaAbstractRepository;
    }

    private static String filePathAbstractPredicate = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport DbPedia/predicates/_http___dbpedia.org_ontology_abstract_.predicate";

    private static String dbPediaPrefix = "<http://dbpedia.org/resource/";

    public void importDbPediaAbstract() {
        Map<String, DataProvider> dataProvidersByDataProviderName = dataProviderService.getDataProvidersByDataProviderName();
        Map<String, DbProId> dbProIdsByDbPediaIdValue = dbProIdService.getDbProIdsByDbPediaIdValue();

        List<DbPediaAbstract> newDbPediaAbstracts = new ArrayList<>();
        //ReadDbPediaAbstracts
        try {
            try(Stream<String> stream = Files.lines(Paths.get(filePathAbstractPredicate))) {
                stream.forEach(line -> {
                    if (line.startsWith(dbPediaPrefix)) {
                        String[] lineSplit = line.split("\t");

                        String dbPediaIdValue = ParseHelper.getDbPediaIdFromText(lineSplit[0]);

                        if (dbProIdsByDbPediaIdValue.containsKey(dbPediaIdValue)) {
                            String dbPediaAbstractValue = ParseHelper.getDbPediaAbstractValue(lineSplit[2]);
                            String dbPediaAbstractLanguageKey = ParseHelper.getDbPediaAbstractLanguageKey(lineSplit[2]);
                            String dataProviderName = ParseHelper.getDataProviderName(lineSplit[3]);

                            DbPediaAbstract dbPediaAbstract = new DbPediaAbstract();
                            dbPediaAbstract.setDbProId(dbProIdsByDbPediaIdValue.get(dbPediaIdValue));
                            dbPediaAbstract.setValue(dbPediaAbstractValue);
                            dbPediaAbstract.setLanguageKey(dbPediaAbstractLanguageKey);
                            dbPediaAbstract.setDataProvider(dataProvidersByDataProviderName.get(dataProviderName));

                            newDbPediaAbstracts.add(dbPediaAbstract);
                        }
                    }
                });
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //newDbPediaAbstracts.size();
        //TODO: save list via chunks with repository
        saveToNeo4jDatabase(newDbPediaAbstracts);
    }

    private void saveToNeo4jDatabase(List<DbPediaAbstract> newDbPediaAbstracts) {
        List<DbPediaAbstract> tempChunkList = new ArrayList<>();

        for (int i = 0; i < newDbPediaAbstracts.size(); i++) {

            tempChunkList.add(newDbPediaAbstracts.get(i));

            if (tempChunkList.size() > 500) {
                System.out.println(i + "/" + newDbPediaAbstracts.size());
                dbPediaAbstractRepository.saveAll(tempChunkList);
                tempChunkList.clear();
            }
        }

        if (tempChunkList.size() > 0) {
            dbPediaAbstractRepository.saveAll(tempChunkList);
            tempChunkList.clear();
        }
    }
}
