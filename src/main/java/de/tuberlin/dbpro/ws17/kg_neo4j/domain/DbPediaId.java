package de.tuberlin.dbpro.ws17.kg_neo4j.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class DbPediaId {

    @Id
    @GeneratedValue
    Long id;

    private String value = null;

    @Relationship(type="SAME_AS", direction = Relationship.OUTGOING)
    DbProId dbProId;

    @Relationship(type="DATA_FROM", direction = Relationship.OUTGOING)
    Set<DataProvider> dataProviders;

    public DbPediaId() {
        this.dataProviders = new HashSet<>();
    }

    //public DbPediaId(String value) {
    //    this.value = value;
    //    this.dataProviders = new HashSet<>();
    //}

    public String getValue() {
        return value;
    }
    public void setValue(String value) { this.value = value; }

    public Set<DataProvider> getDataProviders() {
        return dataProviders;
    }

    public DbProId getDbProId() {return this.dbProId; }
    public void setDbProId(DbProId dbProId) {
        this.dbProId = dbProId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbPediaId dbPediaId = (DbPediaId) o;

        return value.equals(dbPediaId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
