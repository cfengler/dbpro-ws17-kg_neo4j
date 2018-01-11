package de.tuberlin.dbpro.ws17.kg_neo4j.repositories;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaLabel;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DbPediaLabelRepository extends PagingAndSortingRepository<DbPediaLabel, Long> {

    List<DbPediaLabel> findByValue(String value);
    List<DbPediaLabel> findByValueContainsIgnoreCase(String value);

}
