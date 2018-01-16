package de.tuberlin.dbpro.ws17.kg_neo4j.services;

import de.tuberlin.dbpro.ws17.kg_neo4j.common.ParseHelper;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DataProvider;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaFormationYear;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbProId;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbPediaFormationYearRepository;
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
public class DbPediaFormationYearService {

    private DataProviderService dataProviderService = null;
    private DbProIdService dbProIdService = null;
    private DbPediaFormationYearRepository dbPediaFormationYearRepository = null;

    @Autowired
    public DbPediaFormationYearService(DataProviderService dataProviderService,
                                       DbProIdService dbProIdService,
                                       DbPediaFormationYearRepository dbPediaFormationYearRepository) {
        this.dataProviderService = dataProviderService;
        this.dbProIdService = dbProIdService;
        this.dbPediaFormationYearRepository = dbPediaFormationYearRepository;
    }

    private static String filePathFormationYearPredicate = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport DbPedia/predicates/_http___dbpedia.org_ontology_formationYear_.predicate";
    private static String dbPediaPrefix = "<http://dbpedia.org/resource/";

    public void importDbPediaFormationYears() {
        Map<String, DataProvider> dataProvidersByDataProviderName = dataProviderService.getDataProvidersByDataProviderName();
        Map<String, DbProId> dbProIdsByDbPediaIdValue = dbProIdService.getDbProIdsByDbPediaIdValueFast();

        Map<String, DbPediaFormationYear> dbPediaFormationYears = new HashMap<>();

        try {
            try(Stream<String> stream = Files.lines(Paths.get(filePathFormationYearPredicate))) {
                stream.forEach(line -> {
                    if (line.startsWith(dbPediaPrefix)) {
                        String[] lineSplit = line.split("\t");

                        String dbPediaIdValue = ParseHelper.getDbPediaIdFromText(lineSplit[0]);
                        if (dbProIdsByDbPediaIdValue.containsKey(dbPediaIdValue)) {
                            int dbPediaFormationYearValue = ParseHelper.getDbPediaFormationYearFromText(lineSplit[2]);

                            if (1400 < dbPediaFormationYearValue && dbPediaFormationYearValue < 2018) {
                                String dataProviderName = ParseHelper.getDataProviderName(lineSplit[3]);
                                String key = dbPediaFormationYearValue + ";" + dataProviderName;

                                if (!dbPediaFormationYears.containsKey(key)) {
                                    DbPediaFormationYear dbPediaFormationYear = new DbPediaFormationYear();
                                    dbPediaFormationYear.setValue(dbPediaFormationYearValue);
                                    dbPediaFormationYear.setDataProvider(dataProvidersByDataProviderName.get(dataProviderName));
                                    dbPediaFormationYear.setDbProId(new HashSet<>());
                                    dbPediaFormationYears.put(key, dbPediaFormationYear);
                                }
                                dbPediaFormationYears.get(key).getDbProIds().add(dbProIdsByDbPediaIdValue.get(dbPediaIdValue));
                            }
                            else {
                                System.out.println(line);
                            }
                        }
                    }
                });
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //dbPediaFormationYears.size();
        importToNeo4j(dbPediaFormationYears);
    }

    private void importToNeo4j(Map<String, DbPediaFormationYear> dbPediaFormationYears) {
        List<DbPediaFormationYear> formationYears = new ArrayList<>(dbPediaFormationYears.values());
        List<DbPediaFormationYear> tempChunk = new ArrayList<>();

        int dbProIdRelations = 0;
        for (int i = 0; i < formationYears.size(); i++) {
            tempChunk.add(formationYears.get(i));
            dbProIdRelations += formationYears.get(i).getDbProIds().size();

            if (dbProIdRelations > 500) {
                System.out.println(i + "/" + formationYears.size());
                dbPediaFormationYearRepository.saveAll(tempChunk);
                tempChunk.clear();
                dbProIdRelations = 0;
            }
        }

        if (dbProIdRelations > 0) {
            dbPediaFormationYearRepository.saveAll(tempChunk);
            tempChunk.clear();
            dbProIdRelations = 0;
        }
    }

}
