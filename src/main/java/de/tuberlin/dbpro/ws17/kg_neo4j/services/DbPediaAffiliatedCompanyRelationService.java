package de.tuberlin.dbpro.ws17.kg_neo4j.services;

import de.tuberlin.dbpro.ws17.kg_neo4j.common.ParseHelper;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DataProvider;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaAffiliatedCompanyRelation;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbProId;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbPediaAffiliatedCompanyRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Component
@Scope(value = "singleton")
public class DbPediaAffiliatedCompanyRelationService {

    private DataProviderService dataProviderService = null;
    private DbProIdService dbProIdService = null;
    private DbPediaAffiliatedCompanyRelationRepository dbPediaAffiliatedCompanyRelationRepository = null;

    @Autowired
    public DbPediaAffiliatedCompanyRelationService(DataProviderService dataProviderService,
                                                   DbProIdService dbProIdService,
                                                   DbPediaAffiliatedCompanyRelationRepository dbPediaAffiliatedCompanyRelationRepository) {
        this.dataProviderService = dataProviderService;
        this.dbProIdService = dbProIdService;
        this.dbPediaAffiliatedCompanyRelationRepository = dbPediaAffiliatedCompanyRelationRepository;
    }

    private static String filePathSubsidiaryPredicate = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport DbPedia/predicates/_http___dbpedia.org_ontology_subsidiary_.predicate";
    private static String filePathParentPredicate = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport DbPedia/predicates/_http___dbpedia.org_property_parent_.predicate";

    private static String dbPediaPrefix = "<http://dbpedia.org/resource/";

    public void importDbPediaAffiliatedCompanyRelations() {
        Map<String, DataProvider> dataProvidersByDataProviderName = dataProviderService.getDataProvidersByDataProviderName();
        Map<String, DbProId> dbProIdsByDbPediaIdValue = dbProIdService.getDbProIdsByDbPediaIdValue();

        Map<String, DbPediaAffiliatedCompanyRelation> newDbPediaAffiliatedCompanyRelations = new HashMap<>();

        try {

            //read from subsidiary-File
            try(Stream<String> stream = Files.lines(Paths.get(filePathSubsidiaryPredicate))) {
                stream.forEach(line -> {
                    String[] lineSplit = line.split("\t");

                    if (lineSplit[0].startsWith(dbPediaPrefix) && lineSplit[2].startsWith(dbPediaPrefix)) {
                        String dbPediaIdValueParent = ParseHelper.getDbPediaIdFromText(lineSplit[0]);
                        String dbPediaIdValueSubsidiary = ParseHelper.getDbPediaIdFromText(lineSplit[2]);

                        if (dbProIdsByDbPediaIdValue.containsKey(dbPediaIdValueParent) && dbProIdsByDbPediaIdValue.containsKey(dbPediaIdValueSubsidiary)) {
                            String dataProviderName = ParseHelper.getDataProviderName(lineSplit[3]);
                            DbPediaAffiliatedCompanyRelation dbPediaAffiliatedCompanyRelation = new DbPediaAffiliatedCompanyRelation();
                            dbPediaAffiliatedCompanyRelation.setParentCompany(dbProIdsByDbPediaIdValue.get(dbPediaIdValueParent));
                            dbPediaAffiliatedCompanyRelation.setSubsidiary(dbProIdsByDbPediaIdValue.get(dbPediaIdValueSubsidiary));
                            dbPediaAffiliatedCompanyRelation.setDataProvider(dataProvidersByDataProviderName.get(dataProviderName));

                            String key = dbPediaIdValueParent + "->" + dbPediaIdValueSubsidiary;

                            if (!newDbPediaAffiliatedCompanyRelations.containsKey(key)) {
                                newDbPediaAffiliatedCompanyRelations.put(key, dbPediaAffiliatedCompanyRelation);
                            }
                            else {
                                //System.out.println(key + " exists");
                            }
                        }
                    }
                });
            }
            //System.out.println(newDbPediaAffiliatedCompanyRelations.size());
            //read from parent-File
            try(Stream<String> stream = Files.lines(Paths.get(filePathParentPredicate))) {
                stream.forEach(line -> {
                    String[] lineSplit = line.split("\t");

                    if (lineSplit[0].startsWith(dbPediaPrefix) && lineSplit[2].startsWith(dbPediaPrefix)) {
                        String dbPediaIdValueParent = ParseHelper.getDbPediaIdFromText(lineSplit[2]);
                        String dbPediaIdValueSubsidiary = ParseHelper.getDbPediaIdFromText(lineSplit[0]);

                        if (dbProIdsByDbPediaIdValue.containsKey(dbPediaIdValueParent) && dbProIdsByDbPediaIdValue.containsKey(dbPediaIdValueSubsidiary)) {
                            String dataProviderName = ParseHelper.getDataProviderName(lineSplit[3]);
                            DbPediaAffiliatedCompanyRelation dbPediaAffiliatedCompanyRelation = new DbPediaAffiliatedCompanyRelation();
                            dbPediaAffiliatedCompanyRelation.setParentCompany(dbProIdsByDbPediaIdValue.get(dbPediaIdValueParent));
                            dbPediaAffiliatedCompanyRelation.setSubsidiary(dbProIdsByDbPediaIdValue.get(dbPediaIdValueSubsidiary));
                            dbPediaAffiliatedCompanyRelation.setDataProvider(dataProvidersByDataProviderName.get(dataProviderName));

                            String key = dbPediaIdValueParent + "->" + dbPediaIdValueSubsidiary;

                            if (!newDbPediaAffiliatedCompanyRelations.containsKey(key)) {
                                newDbPediaAffiliatedCompanyRelations.put(key, dbPediaAffiliatedCompanyRelation);
                            }
                            else {
                                //System.out.println(key + " exists");
                            }
                        }
                    }
                });
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(newDbPediaAffiliatedCompanyRelations.size());
        saveToNeo4jDatabase(new ArrayList<>(newDbPediaAffiliatedCompanyRelations.values()));
    }

    private void saveToNeo4jDatabase(List<DbPediaAffiliatedCompanyRelation> newDbPediaAffiliatedCompanyRelations) {
        List<DbPediaAffiliatedCompanyRelation> tempChunkList = new ArrayList<>();

        for (int i = 0; i < newDbPediaAffiliatedCompanyRelations.size(); i++) {

            tempChunkList.add(newDbPediaAffiliatedCompanyRelations.get(i));

            if (tempChunkList.size() > 500) {
                System.out.println(i + "/" + newDbPediaAffiliatedCompanyRelations.size());
                dbPediaAffiliatedCompanyRelationRepository.saveAll(tempChunkList);
                tempChunkList.clear();
            }
        }

        if (tempChunkList.size() > 0) {
            dbPediaAffiliatedCompanyRelationRepository.saveAll(tempChunkList);
            tempChunkList.clear();
        }
    }
}
