package de.tuberlin.dbpro.ws17.kg_neo4j.services;

import de.tuberlin.dbpro.ws17.kg_neo4j.common.ParseHelper;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DataProvider;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaNumberOfEmployees;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbProId;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbPediaNumberOfEmployeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@Scope(value = "singleton")
public class DbPediaNumberOfEmployeesService {

    private DataProviderService dataProviderService = null;
    private DbProIdService dbProIdService = null;
    private DbPediaNumberOfEmployeesRepository dbPediaNumberOfEmployeesRepository = null;

    @Autowired
    public DbPediaNumberOfEmployeesService(DataProviderService dataProviderService,
                                           DbProIdService dbProIdService,
                                           DbPediaNumberOfEmployeesRepository dbPediaNumberOfEmployeesRepository) {
        this.dataProviderService = dataProviderService;
        this.dbProIdService = dbProIdService;
        this.dbPediaNumberOfEmployeesRepository = dbPediaNumberOfEmployeesRepository;
    }

    private static String filePathNumberOfEmployeesPredicate = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport DbPedia/predicates/_http___dbpedia.org_ontology_numberOfEmployees_.predicate";

    private static String dbPediaPrefix = "<http://dbpedia.org/resource/";

    public void importDbPediaNumberOfEmployees() {
        Map<String, DataProvider> dataProvidersByDataProviderName = dataProviderService.getDataProvidersByDataProviderName();
        Map<String, DbProId> dbProIdsByDbPediaIdValue = dbProIdService.getDbProIdsByDbPediaIdValueFast();

        List<DbPediaNumberOfEmployees> newDbPediaNumberOfEmployees = new ArrayList<>();

        try {
            try(Stream<String> stream = Files.lines(Paths.get(filePathNumberOfEmployeesPredicate))) {
                stream.forEach(line -> {
                    if (line.startsWith(dbPediaPrefix)) {
                        String[] lineSplit = line.split("\t");

                        String dbPediaIdValue = ParseHelper.getDbPediaIdFromText(lineSplit[0]);

                        if (dbProIdsByDbPediaIdValue.containsKey(dbPediaIdValue)) {
                            int dbPediaNumberOfEmployeesValue = ParseHelper.getDbPediaNumberOfEmployeesValue(lineSplit[2]);
                            String dataProviderName = ParseHelper.getDataProviderName(lineSplit[3]);

                            DbPediaNumberOfEmployees dbPediaNumberOfEmployees = new DbPediaNumberOfEmployees();
                            dbPediaNumberOfEmployees.setDbProId(dbProIdsByDbPediaIdValue.get(dbPediaIdValue));
                            dbPediaNumberOfEmployees.setValue(dbPediaNumberOfEmployeesValue);
                            dbPediaNumberOfEmployees.setDataProvider(dataProvidersByDataProviderName.get(dataProviderName));

                            newDbPediaNumberOfEmployees.add(dbPediaNumberOfEmployees);
                        }
                    }
                });
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //newDbPediaNumberOfEmployees.size();

        saveToNeo4j(newDbPediaNumberOfEmployees);
    }

    private void saveToNeo4j(List<DbPediaNumberOfEmployees> newDbPediaNumberOfEmployees) {
        List<DbPediaNumberOfEmployees> tempChunkList = new ArrayList<>();

        for (int i = 0; i < newDbPediaNumberOfEmployees.size(); i++) {

            tempChunkList.add(newDbPediaNumberOfEmployees.get(i));

            if (tempChunkList.size() > 500) {
                System.out.println(i + "/" + newDbPediaNumberOfEmployees.size());
                dbPediaNumberOfEmployeesRepository.saveAll(tempChunkList);
                tempChunkList.clear();
            }
        }

        if (tempChunkList.size() > 0) {
            dbPediaNumberOfEmployeesRepository.saveAll(tempChunkList);
            tempChunkList.clear();
        }
    }
}
