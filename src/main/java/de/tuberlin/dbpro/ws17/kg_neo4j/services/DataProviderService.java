package de.tuberlin.dbpro.ws17.kg_neo4j.services;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DataProvider;
import de.tuberlin.dbpro.ws17.kg_neo4j.repositories.DataProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope(value = "singleton" )
public class DataProviderService {

    private DataProviderRepository dataProviderRepository = null;

    @Autowired
    public DataProviderService(DataProviderRepository dataProviderRepository) {
        this.dataProviderRepository = dataProviderRepository;
    }

    public Map<String, DataProvider> getDataProvidersByDataProviderName() {
        Map<String, DataProvider> result = new HashMap<>();

        for (DataProvider dataProvider:dataProviderRepository.findAll()) {
            result.put(dataProvider.getName(), dataProvider);
        }

        return result;
    }
}
