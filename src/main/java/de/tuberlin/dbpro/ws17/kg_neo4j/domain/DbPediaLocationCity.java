package de.tuberlin.dbpro.ws17.kg_neo4j.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
public class DbPediaLocationCity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Relationship(type = "HAS_DBPEDIA_LOCATION_CITY", direction = Relationship.INCOMING)
    Set<DbProId> dbProIds;

    @Relationship(type = "DATA_FROM", direction = Relationship.OUTGOING)
    DataProvider dataProvider;

    public DbPediaLocationCity() {

    }

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public Set<DbProId> getDbProIds() { return this.dbProIds; }
    public void setDbProId(Set<DbProId> dbProIds) { this.dbProIds = dbProIds; }

    public DataProvider getDataProvider() {
        return this.dataProvider;
    }
    public void setDataProvider(DataProvider dataProvider) { this.dataProvider = dataProvider; }

}
