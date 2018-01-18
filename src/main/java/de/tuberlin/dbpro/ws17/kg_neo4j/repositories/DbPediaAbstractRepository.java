package de.tuberlin.dbpro.ws17.kg_neo4j.repositories;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaAbstract;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DbPediaAbstractRepository extends CrudRepository<DbPediaAbstract, Long> {

    List<DbPediaAbstract> findByValueContainsIgnoreCase(String value);

}
