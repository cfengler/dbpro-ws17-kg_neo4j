package de.tuberlin.dbpro.ws17.kg_neo4j.repositories;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaId;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbProId;
import org.springframework.data.domain.Page;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbProIdRepository extends PagingAndSortingRepository<DbProId, Long> {

    DbProId findByValue(long value);
    List<DbProId> findByValueIn(Iterable<Long> values);

}
