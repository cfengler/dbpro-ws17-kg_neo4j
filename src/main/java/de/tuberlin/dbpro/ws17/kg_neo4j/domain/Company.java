package de.tuberlin.dbpro.ws17.kg_neo4j.domain;

import java.util.List;

public class Company {
    public DbProId dbProId;
    public String name;
    public String dbPediaAbstract;
    public List<String> dbPediaLocationCountries = null;
    public List<String> dbPediaLocationCities = null;
    public Company dbPediaParentCompany = null;
    public List<Company> dbPediaSubsidiaries = null;
    public List<Integer> dbPediaFormationYears = null;
    public List<Integer> dbPediaNumberOfEmployees = null;
}
