package de.tuberlin.dbpro.ws17.kg_neo4j.repositories;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaAbstract;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbPediaAbstractRepository extends CrudRepository<DbPediaAbstract, Long> {

    List<DbPediaAbstract> findByValueContainsIgnoreCase(String value);

}
