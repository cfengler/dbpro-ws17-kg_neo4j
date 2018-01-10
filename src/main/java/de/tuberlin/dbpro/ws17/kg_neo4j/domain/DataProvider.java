package de.tuberlin.dbpro.ws17.kg_neo4j.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.util.StringUtils;

import java.util.Set;

@NodeEntity
public class DataProvider {
    @org.neo4j.ogm.annotation.Id
    @GeneratedValue
    private Long id;

    private String name;

    @Relationship(type="DATA_FROM", direction = Relationship.INCOMING)
    Set<DbPediaId> dbPediaIds;

    public DataProvider() {

    }

    public DataProvider(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataProvider dataProvider = (DataProvider) o;

        return name.equals(dataProvider.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
