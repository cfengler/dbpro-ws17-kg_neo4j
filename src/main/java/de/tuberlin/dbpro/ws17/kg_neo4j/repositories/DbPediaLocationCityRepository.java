package de.tuberlin.dbpro.ws17.kg_neo4j.repositories;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaLocationCity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DbPediaLocationCityRepository extends CrudRepository<DbPediaLocationCity, Long> {

    List<DbPediaLocationCity> findByNameContainsIgnoreCase(String name);

}
