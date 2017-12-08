package de.tuberlin.dbpro.ws17.kg_neo4j.model;

import java.util.ArrayList;
import java.util.List;

public class Company {

    public String id_permid;
    public String id_gcd;

    //public String name;
    public List<String> names;

    public String summary;
    public String founding;
    public String headquarterSite;
    public String registeredSite;
    public String webSite;
    public int numberOfEmployees;

    public List<String> subsidiaries;



    public Company(String id_permid, String id_gcd) {
        this.id_permid = id_permid;
        this.id_gcd = id_gcd;
        names = new ArrayList<>();
        subsidiaries = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Company{" + System.lineSeparator() +
            "id_permid='" + id_permid + System.lineSeparator() +
            ", id_gcd='" + id_gcd + System.lineSeparator() +
            ", names={" + String.join(", ", names) + "}" + System.lineSeparator() +
            ", summary='" + summary + System.lineSeparator() +
            ", founding='" + founding + System.lineSeparator() +
            ", headquarterSite='" + headquarterSite + System.lineSeparator() +
            ", registeredSite='" + registeredSite + System.lineSeparator() +
            ", webSite='" + webSite + System.lineSeparator() +
            ", numberOfEmployees=" + numberOfEmployees + System.lineSeparator() +
            ", subsidiaries={" + String.join(", ", subsidiaries) + "}" + System.lineSeparator()  +
            '}';
    }
}
