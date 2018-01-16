package de.tuberlin.dbpro.ws17.kg_neo4j.repositories;


import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaFormationYear;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DbPediaFormationYearRepository extends CrudRepository<DbPediaFormationYear, Long> {

    List<DbPediaFormationYear> findByValue(int value);

}