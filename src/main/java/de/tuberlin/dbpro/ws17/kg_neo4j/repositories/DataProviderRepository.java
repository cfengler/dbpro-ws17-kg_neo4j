package de.tuberlin.dbpro.ws17.kg_neo4j.repositories;


import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DataProvider;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataProviderRepository extends CrudRepository<DataProvider, Long> {

}
