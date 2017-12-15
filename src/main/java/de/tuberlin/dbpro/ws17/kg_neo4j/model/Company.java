package de.tuberlin.dbpro.ws17.kg_neo4j.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Company {

    public int id;
    public List<ExternalId> externalIds;
    public List<Tag> tags;

    public String summary;
    public String founding;
    public String headquarterSite;
    public String registeredSite;
    public String webSite;

    public int numberOfEmployees;
    public List<ExternalId> subsidiaries;

    public Company(String id_permid, String id_gcd) {
        this.id = 5;
        externalIds = new ArrayList<>();
        tags = new ArrayList<>();

        subsidiaries = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Company{" +
            "id=" + id +
            ", externalIds=" + externalIds.stream().map(Object::toString).collect(Collectors.joining(", ")) +
            ", tags=" + tags.stream().map(Object::toString).collect(Collectors.joining(", ")) +
            ", summary='" + summary + '\'' +
            ", founding='" + founding + '\'' +
            ", headquarterSite='" + headquarterSite + '\'' +
            ", registeredSite='" + registeredSite + '\'' +
            ", webSite='" + webSite + '\'' +
            ", numberOfEmployees=" + numberOfEmployees +
            ", subsidiaries=" + subsidiaries.stream().map(Object::toString).collect(Collectors.joining(", ")) +
            '}';
    }

//    @Override
//    public String toString() {
//        return "Company {" + System.lineSeparator() +
//            "externalIds={" + id_permid + System.lineSeparator() +
//            ", id_gcd='" + id_gcd + System.lineSeparator() +
//            ", names={" + String.join(", ", names) + "}" + System.lineSeparator() +
//            ", summary='" + summary + System.lineSeparator() +
//            ", founding='" + founding + System.lineSeparator() +
//            ", headquarterSite='" + headquarterSite + System.lineSeparator() +
//            ", registeredSite='" + registeredSite + System.lineSeparator() +
//            ", webSite='" + webSite + System.lineSeparator() +
//            ", numberOfEmployees=" + numberOfEmployees + System.lineSeparator() +
//            ", subsidiaries={" + String.join(", ", subsidiaries) + "}" + System.lineSeparator()  +
//            '}';
//    }
}
