package de.tuberlin.dbpro.ws17.kg_neo4j.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
public class DbProId {

    @Id @GeneratedValue
    private Long id;
    private Long Value;

    @Relationship(type = "")
    private List<ExternalId> externalIds;

    public DbProId() {

    }
}
