package de.tuberlin.dbpro.ws17.kg_neo4j.repositories;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaLabel;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DbPediaLabelRepository extends PagingAndSortingRepository<DbPediaLabel, Long> {

}
