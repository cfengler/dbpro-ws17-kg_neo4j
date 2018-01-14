package de.tuberlin.dbpro.ws17.kg_neo4j.domain;

import java.util.List;

public class Company {
    public long id;
    public String name;
    public String dbPediaAbstract;
    public CompanyInfo parentCompany = null;
    public List<CompanyInfo> subsidiaries = null;
}
