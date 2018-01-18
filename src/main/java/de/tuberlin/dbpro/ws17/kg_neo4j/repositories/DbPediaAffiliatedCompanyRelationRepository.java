package de.tuberlin.dbpro.ws17.kg_neo4j.repositories;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaAffiliatedCompanyRelation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbPediaAffiliatedCompanyRelationRepository extends CrudRepository<DbPediaAffiliatedCompanyRelation, Long> {

    List<DbPediaAffiliatedCompanyRelation> findByCustomIdIn(Iterable<Long> customIds);

}
