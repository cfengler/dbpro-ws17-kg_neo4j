package de.tuberlin.dbpro.ws17.kg_neo4j.repositories;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaLocationCountry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbPediaLocationCountryRepository extends CrudRepository<DbPediaLocationCountry, Long> {

    List<DbPediaLocationCountry> findByNameContainsIgnoreCase(String name);

}
