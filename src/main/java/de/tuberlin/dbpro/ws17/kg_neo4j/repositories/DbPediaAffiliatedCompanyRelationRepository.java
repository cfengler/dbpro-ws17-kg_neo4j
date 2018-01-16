package de.tuberlin.dbpro.ws17.kg_neo4j.repositories;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaAffiliatedCompanyRelation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DbPediaAffiliatedCompanyRelationRepository extends CrudRepository<DbPediaAffiliatedCompanyRelation, Long> {

    List<DbPediaAffiliatedCompanyRelation> findByCustomIdIn(Iterable<Long> customIds);
    //DbPediaAffiliatedCompanyRelation get

}
