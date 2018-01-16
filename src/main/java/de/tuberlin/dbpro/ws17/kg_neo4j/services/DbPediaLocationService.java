package de.tuberlin.dbpro.ws17.kg_neo4j.services;

import de.tuberlin.dbpro.ws17.kg_neo4j.common.ParseHelper;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.*;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbPediaLocationCityRepository;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DbPediaLocationCountryRepository;
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
public class DbPediaLocationService {

    private DataProviderService dataProviderService = null;
    private DbProIdService dbProIdService = null;
    private DbPediaLocationCountryRepository dbPediaLocationCountryRepository = null;
    private DbPediaLocationCityRepository dbPediaLocationCityRepository = null;

    @Autowired
    public DbPediaLocationService(DataProviderService dataProviderService,
                                  DbProIdService dbProIdService,
                                  DbPediaLocationCountryRepository dbPediaLocationCountryRepository,
                                  DbPediaLocationCityRepository dbPediaLocationCityRepository) {
        this.dataProviderService = dataProviderService;
        this.dbProIdService = dbProIdService;
        this.dbPediaLocationCountryRepository = dbPediaLocationCountryRepository;
        this.dbPediaLocationCityRepository = dbPediaLocationCityRepository;
    }

    private static String filePathLocationCountryPredicate = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport DbPedia/predicates/_http___dbpedia.org_ontology_locationCountry_.predicate";
    private static String filePathLocationCityPredicate = "/media/cfengler/Kingston DT HyperX 3.0/Datenimport DbPedia/predicates/_http___dbpedia.org_ontology_locationCity_.predicate";

    private static String dbPediaPrefix = "<http://dbpedia.org/resource/";

    public void importDbPediaLocations() {
        Map<String, DataProvider> dataProvidersByDataProviderName = dataProviderService.getDataProvidersByDataProviderName();
        //Map<String, DbProId> dbProIdsByDbPediaIdValue1 = dbProIdService.getDbProIdsByDbPediaIdValue();
        //dbProIdsByDbPediaIdValue1 = null;
        Map<String, DbProId> dbProIdsByDbPediaIdValue = dbProIdService.getDbProIdsByDbPediaIdValueFast();

        Map<String, DbPediaLocationCountry> dbPediaLocationCountries = new HashMap<>();
        Map<String, DbPediaLocationCity> dbPediaLocationCities = new HashMap<>();

        try {
            try(Stream<String> stream = Files.lines(Paths.get(filePathLocationCountryPredicate))) {
                stream.forEach(line -> {
                    String[] lineSplit = line.split("\t");
                    if (lineSplit[0].startsWith(dbPediaPrefix) && lineSplit[2].startsWith(dbPediaPrefix)) {
                        String dbPediaIdValue = ParseHelper.getDbPediaIdFromText(lineSplit[0]);

                        if (dbProIdsByDbPediaIdValue.containsKey(dbPediaIdValue)) {
                            String dbPediaLocationCountryName = ParseHelper.getDbPediaCountryNameFromText(lineSplit[2]);
                            String dataProviderName = ParseHelper.getDataProviderName(lineSplit[3]);
                            String key = dbPediaLocationCountryName + ";" + dataProviderName;

                            if (!dbPediaLocationCountries.containsKey(key)) {
                                DbPediaLocationCountry dbPediaLocationCountry = new DbPediaLocationCountry();
                                dbPediaLocationCountry.setName(dbPediaLocationCountryName);
                                dbPediaLocationCountry.setDataProvider(dataProvidersByDataProviderName.get(dataProviderName));
                                dbPediaLocationCountry.setDbProId(new HashSet<>());
                                dbPediaLocationCountries.put(key, dbPediaLocationCountry);
                            }
                            dbPediaLocationCountries.get(key).getDbProIds().add(dbProIdsByDbPediaIdValue.get(dbPediaIdValue));
                        }
                    }
                });
            }

            try(Stream<String> stream = Files.lines(Paths.get(filePathLocationCityPredicate))) {
                stream.forEach(line -> {
                    String[] lineSplit = line.split("\t");
                    if (lineSplit[0].startsWith(dbPediaPrefix) && lineSplit[2].startsWith(dbPediaPrefix)) {
                        String dbPediaIdValue = ParseHelper.getDbPediaIdFromText(lineSplit[0]);

                        if (dbProIdsByDbPediaIdValue.containsKey(dbPediaIdValue)) {
                            String dbPediaLocationCityName = ParseHelper.getDbPediaIdFromText(lineSplit[2]);
                            String dataProviderName = ParseHelper.getDataProviderName(lineSplit[3]);
                            String key = dbPediaLocationCityName + ";" + dataProviderName;

                            if (!dbPediaLocationCities.containsKey(key)) {
                                DbPediaLocationCity dbPediaLocationCity = new DbPediaLocationCity();
                                dbPediaLocationCity.setName(dbPediaLocationCityName);
                                dbPediaLocationCity.setDataProvider(dataProvidersByDataProviderName.get(dataProviderName));
                                dbPediaLocationCity.setDbProId(new HashSet<>());
                                dbPediaLocationCities.put(key, dbPediaLocationCity);
                            }
                            dbPediaLocationCities.get(key).getDbProIds().add(dbProIdsByDbPediaIdValue.get(dbPediaIdValue));
                        }
                    }
                });
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //analyseDbPediaLocationCountries(dbPediaLocationCountries);

        importDbPediaLocationCountires(dbPediaLocationCountries);
        importDbPediaLocationCities(dbPediaLocationCities);

    }

//    private void analyseDbPediaLocationCountries(Map<String, DbPediaLocationCountry> dbPediaLocationCountries) {
//        List<DbPediaLocationCountry> countriesSorted = new ArrayList<>(dbPediaLocationCountries.values());
//        countriesSorted.sort(new Comparator<DbPediaLocationCountry>() {
//            @Override
//            public int compare(DbPediaLocationCountry o1, DbPediaLocationCountry o2) {
//
//                return o2.getDbProIds().size() < o2.getDbProIds().size()
//                    ? 1
//                    : -1;
//            }
//        });
//
//        int maxDbProIds = 0;
//        DbPediaLocationCountry country = null;
//
//        for (DbPediaLocationCountry dbPediaLocationCountry:countriesSorted) {
//            System.out.println(dbPediaLocationCountry.getName() + ":" + dbPediaLocationCountry.getDbProIds().size());
//
//            if (maxDbProIds < dbPediaLocationCountry.getDbProIds().size()) {
//                maxDbProIds = dbPediaLocationCountry.getDbProIds().size();
//                country = dbPediaLocationCountry;
//            }
//        }
//
//        country.getDbProIds().size();
//    }

    private void importDbPediaLocationCountires(Map<String, DbPediaLocationCountry> dbPediaLocationCountries) {
        List<DbPediaLocationCountry> countries = new ArrayList<>(dbPediaLocationCountries.values());
        List<DbPediaLocationCountry> tempChunk = new ArrayList<>();

        int dbProIdRelations = 0;
        for (int i = 0; i < countries.size(); i++) {
            tempChunk.add(countries.get(i));
            dbProIdRelations += countries.get(i).getDbProIds().size();

            if (dbProIdRelations > 500) {
                System.out.println(i + "/" + countries.size());
                dbPediaLocationCountryRepository.saveAll(tempChunk);
                tempChunk.clear();
                dbProIdRelations = 0;
            }
        }

        if (dbProIdRelations > 0) {
            dbPediaLocationCountryRepository.saveAll(tempChunk);
            tempChunk.clear();
            dbProIdRelations = 0;
        }
    }

    private void importDbPediaLocationCities(Map<String, DbPediaLocationCity> dbPediaLocationCities) {
        List<DbPediaLocationCity> cities = new ArrayList<>(dbPediaLocationCities.values());
        List<DbPediaLocationCity> tempChunk = new ArrayList<>();

        int dbProIdRelations = 0;
        for (int i = 0; i < cities.size(); i++) {
            tempChunk.add(cities.get(i));
            dbProIdRelations += cities.get(i).getDbProIds().size();

            if (dbProIdRelations > 500) {
                System.out.println(i + "/" + cities.size());
                dbPediaLocationCityRepository.saveAll(tempChunk);
                tempChunk.clear();
                dbProIdRelations = 0;
            }
        }

        if (dbProIdRelations > 0) {
            dbPediaLocationCityRepository.saveAll(tempChunk);
            tempChunk.clear();
            dbProIdRelations = 0;
        }
    }
}
