package de.tuberlin.dbpro.ws17.kg_neo4j.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class MainLabel {

    @Id
    @GeneratedValue
    Long id;

    private String value = null;

    @Relationship(type="HAS_MAIN_LABEL", direction = Relationship.INCOMING)
    DbProId dbProId;

    @Relationship(type="DATA_FROM", direction = Relationship.OUTGOING)
    DataProvider dataProvider;

    public MainLabel() {

    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) { this.value = value; }

    public DbProId getDbProId() {return this.dbProId; }
    public void setDbProId(DbProId dbProId) {
        this.dbProId = dbProId;
    }

    public DataProvider getDataProvider() {
        return this.dataProvider;
    }
    public void setDataProvider(DataProvider dataProvider) { this.dataProvider = dataProvider; }

}
