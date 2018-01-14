package de.tuberlin.dbpro.ws17.kg_neo4j.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class DbPediaLocationCountry {

    @Id
    @GeneratedValue
    private Long id;

    private String value;

    @Relationship(type = "HAS_LOCATION_COUNTRY", direction = Relationship.INCOMING)
    DbProId dbProId;

    @Relationship(type = "DATA_FROM", direction = Relationship.OUTGOING)
    DataProvider dataProvider;

    public DbPediaLocationCountry() {

    }

    public String getValue() {
        return value;
    }
    public void setValue(String name) { this.value = value; }

    public DataProvider getDataProvider() {
        return this.dataProvider;
    }
    public void setDataProvider(DataProvider dataProvider) { this.dataProvider = dataProvider; }

}
