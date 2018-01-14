package de.tuberlin.dbpro.ws17.kg_neo4j.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
public class DbProId {

    @Id
    @GeneratedValue
    private Long id;

    private long value;

    @Relationship(type = "SAME_AS", direction = Relationship.INCOMING)
    DbPediaId dbPediaId;

    @Relationship(type = "DATA_FROM", direction = Relationship.OUTGOING)
    DataProvider dataProvider;

    @Relationship(type = "HAS_LABEL", direction = Relationship.OUTGOING)
    Set<DbPediaLabel> dbPediaLabels;

    @Relationship(type = "HAS_ABSTRACT", direction = Relationship.OUTGOING)
    Set<DbPediaAbstract> dbPediaAbstracts;

    @Relationship(type = "HAS_DBPEDIA_PARENT_COMPANY", direction = Relationship.OUTGOING)
    DbPediaAffiliatedCompanyRelation dbPediaParentCompany;

    @Relationship(type = "HAS_DBPEDIA_SUBSIDIARY", direction = Relationship.OUTGOING)
    Set<DbPediaAffiliatedCompanyRelation> dbPediaSubsidiaries;

    public DbProId() {

    }

    public long getId() {
        return id;
    }

    public long getValue() {
        return this.value;
    }
    public void setValue(long value) { this.value = value; }

    public DataProvider getDataProvider() {
        return this.dataProvider;
    }
    public void setDataProvider(DataProvider dataProvider) { this.dataProvider = dataProvider; }

    public DbPediaId getDbPediaId() {
        return this.dbPediaId;
    }
    public void setDbPediaId(DbPediaId dbPediaId) { this.dbPediaId = dbPediaId; }

    public Set<DbPediaLabel> getDbPediaLabels() { return this.dbPediaLabels; }

    public Set<DbPediaAbstract> getDbPediaAbstract() {
        return this.dbPediaAbstracts;
    }
    //public void setDbPediaAbstract(Set<DbPediaAbstract> dbPediaAbstracts) { this.dbPediaAbstracts = dbPediaAbstracts; }

    public DbPediaAffiliatedCompanyRelation getDbPediaParentCompany() {
        return this.dbPediaParentCompany;
    }
    public void setDbPediaParentCompany(DbPediaAffiliatedCompanyRelation dbPediaParentCompany) { this.dbPediaParentCompany = dbPediaParentCompany; }

    public Set<DbPediaAffiliatedCompanyRelation> getDbPediaSubsidiaries() { return this.dbPediaSubsidiaries; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbProId dbProId = (DbProId) o;

        return value == dbProId.value;// value.equals(dataProvider.value);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);// value.hashCode();
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }
}
