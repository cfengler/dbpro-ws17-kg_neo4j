package de.tuberlin.dbpro.ws17.kg_neo4j.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class DbPediaAffiliatedCompanyRelation {

    @Id
    @GeneratedValue
    private Long id;

    private long customId;

    @Relationship(type = "HAS_DBPEDIA_PARENT_COMPANY", direction = Relationship.INCOMING)
    private DbProId subsidiary;

    @Relationship(type = "HAS_DBPEDIA_SUBSIDIARY", direction = Relationship.INCOMING)
    private DbProId parentCompany;

    @Relationship(type = "DATA_FROM", direction = Relationship.OUTGOING)
    private DataProvider dataProvider;

    public DbPediaAffiliatedCompanyRelation() {

    }

    public long getId() {
        return id;
    }

    public long getCustomId() {
        return customId;
    }
    public void setCustomId(long customId) { this.customId = customId; }

    public DbProId getSubsidiary() {
        return subsidiary;
    }
    public void setSubsidiary(DbProId subsidiary) { this.subsidiary = subsidiary; }

    public DbProId getParentCompany() {
        return parentCompany;
    }
    public void setParentCompany(DbProId parentCompany) { this.parentCompany = parentCompany; }

    public DataProvider getDataProvider() {
        return this.dataProvider;
    }
    public void setDataProvider(DataProvider dataProvider) { this.dataProvider = dataProvider; }

}
