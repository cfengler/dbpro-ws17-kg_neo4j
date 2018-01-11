package de.tuberlin.dbpro.ws17.kg_neo4j.repositories;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaId;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbPediaIdRepository extends PagingAndSortingRepository<DbPediaId, Long> {

    DbPediaId findByValue(String value);

}
