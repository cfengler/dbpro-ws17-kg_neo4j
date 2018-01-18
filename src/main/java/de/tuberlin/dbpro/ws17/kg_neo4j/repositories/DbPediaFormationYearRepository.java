package de.tuberlin.dbpro.ws17.kg_neo4j.repositories;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaFormationYear;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbPediaFormationYearRepository extends CrudRepository<DbPediaFormationYear, Long> {

    List<DbPediaFormationYear> findByValue(int value);

}