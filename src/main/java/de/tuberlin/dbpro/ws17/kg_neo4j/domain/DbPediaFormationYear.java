package de.tuberlin.dbpro.ws17.kg_neo4j.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
public class DbPediaFormationYear {

    @Id
    @GeneratedValue
    private Long id;

    private int value;

    @Relationship(type = "HAS_DBPEDIA_FORMATION_YEAR", direction = Relationship.INCOMING)
    private Set<DbProId> dbProIds;

    @Relationship(type = "DATA_FROM", direction = Relationship.OUTGOING)
    private DataProvider dataProvider;

    public DbPediaFormationYear() {

    }

    public int getValue() {
        return value;
    }
    public void setValue(int value) { this.value = value; }

    public Set<DbProId> getDbProIds() { return this.dbProIds; }
    public void setDbProId(Set<DbProId> dbProIds) { this.dbProIds = dbProIds; }

    public DataProvider getDataProvider() {
        return this.dataProvider;
    }
    public void setDataProvider(DataProvider dataProvider) { this.dataProvider = dataProvider; }

}
