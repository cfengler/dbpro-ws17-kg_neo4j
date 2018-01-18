package de.tuberlin.dbpro.ws17.kg_neo4j.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class DbPediaLabel {

    @Id
    @GeneratedValue
    private Long id;

    private String value;
    private String languageKey;

    @Relationship(type = "HAS_LABEL", direction = Relationship.INCOMING)
    private DbProId dbProId;

    @Relationship(type = "DATA_FROM", direction = Relationship.OUTGOING)
    private DataProvider dataProvider;

    public DbPediaLabel() {

    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) { this.value = value; }

    public String getLanguageKey() {
        return languageKey;
    }
    public void setLanguageKey(String languageKey) { this.languageKey = languageKey; }

    public DbProId getDbProId() {return this.dbProId; }
    public void setDbProId(DbProId dbProId) {
        this.dbProId = dbProId;
    }

    public DataProvider getDataProvider() {
        return this.dataProvider;
    }
    public void setDataProvider(DataProvider dataProvider) { this.dataProvider = dataProvider; }

    public String toString() {
        return value + ";" + languageKey + ";" + dataProvider.getName();
    }
}
