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
    private DbPediaId dbPediaId;

    @Relationship(type = "DATA_FROM", direction = Relationship.OUTGOING)
    private DataProvider dataProvider;

    @Relationship(type = "HAS_LABEL", direction = Relationship.OUTGOING)
    private Set<DbPediaLabel> dbPediaLabels;

    @Relationship(type = "HAS_ABSTRACT", direction = Relationship.OUTGOING)
    private Set<DbPediaAbstract> dbPediaAbstracts;

    @Relationship(type = "HAS_DBPEDIA_PARENT_COMPANY", direction = Relationship.OUTGOING)
    private DbPediaAffiliatedCompanyRelation dbPediaParentCompany;

    @Relationship(type = "HAS_DBPEDIA_SUBSIDIARY", direction = Relationship.OUTGOING)
    private Set<DbPediaAffiliatedCompanyRelation> dbPediaSubsidiaries;

    @Relationship(type = "HAS_DBPEDIA_LOCATION_COUNTRY", direction = Relationship.OUTGOING)
    private Set<DbPediaLocationCountry> dbPediaLocationCountries;

    @Relationship(type = "HAS_DBPEDIA_LOCATION_CITY", direction = Relationship.OUTGOING)
    private Set<DbPediaLocationCity> dbPediaLocationCities;

    @Relationship(type = "HAS_DBPEDIA_FORMATION_YEAR", direction = Relationship.OUTGOING)
    private Set<DbPediaFormationYear> dbPediaFormationYears;

    @Relationship(type = "HAS_DBPEDIA_NUMBER_OF_EMPLOYEES", direction = Relationship.OUTGOING)
    private Set<DbPediaNumberOfEmployees> dbPediaNumberOfEmployees;

    public DbProId() {

    }

    public long getId() {
        return id;
    }

    public long getValue() {
        return this.value;
    }
    //public void setValue(long value) { this.value = value; }

    public DataProvider getDataProvider() {
        return this.dataProvider;
    }
    //public void setDataProvider(DataProvider dataProvider) { this.dataProvider = dataProvider; }

    public DbPediaId getDbPediaId() {
        return this.dbPediaId;
    }
    //public void setDbPediaId(DbPediaId dbPediaId) { this.dbPediaId = dbPediaId; }

    public Set<DbPediaLabel> getDbPediaLabels() { return this.dbPediaLabels; }

    public Set<DbPediaAbstract> getDbPediaAbstract() {
        return this.dbPediaAbstracts;
    }
    //public void setDbPediaAbstract(Set<DbPediaAbstract> dbPediaAbstracts) { this.dbPediaAbstracts = dbPediaAbstracts; }

    public DbPediaAffiliatedCompanyRelation getDbPediaParentCompany() {
        return this.dbPediaParentCompany;
    }
    //public void setDbPediaParentCompany(DbPediaAffiliatedCompanyRelation dbPediaParentCompany) { this.dbPediaParentCompany = dbPediaParentCompany; }

    public Set<DbPediaAffiliatedCompanyRelation> getDbPediaSubsidiaries() { return this.dbPediaSubsidiaries; }

    public Set<DbPediaLocationCountry> getDbPediaLocationCountries() { return this.dbPediaLocationCountries; }

    public Set<DbPediaLocationCity> getDbPediaLocationCities() { return this.dbPediaLocationCities; }

    public Set<DbPediaFormationYear> getDbPediaFormationYears() {return this.dbPediaFormationYears; }

    public Set<DbPediaNumberOfEmployees> getDbPediaNumberOfEmployees() { return this.dbPediaNumberOfEmployees; }

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
