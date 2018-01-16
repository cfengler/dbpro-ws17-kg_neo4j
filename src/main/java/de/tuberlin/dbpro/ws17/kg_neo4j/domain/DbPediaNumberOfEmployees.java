package de.tuberlin.dbpro.ws17.kg_neo4j.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
public class DbPediaNumberOfEmployees {

    @Id
    @GeneratedValue
    private Long id;

    private int value;

    @Relationship(type = "HAS_DBPEDIA_NUMBER_OF_EMPLOYEES", direction = Relationship.INCOMING)
    DbProId dbProId;

    @Relationship(type = "DATA_FROM", direction = Relationship.OUTGOING)
    DataProvider dataProvider;

    public DbPediaNumberOfEmployees() {

    }

    public int getValue() {
        return value;
    }
    public void setValue(int value) { this.value = value; }

    public DbProId getDbProId() {return this.dbProId; }
    public void setDbProId(DbProId dbProId) {
        this.dbProId = dbProId;
    }

    public DataProvider getDataProvider() {
        return this.dataProvider;
    }
    public void setDataProvider(DataProvider dataProvider) { this.dataProvider = dataProvider; }

}
