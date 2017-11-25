package de.tuberlin.dbpro.ws17.kg_neo4j.analysis;

import java.util.HashSet;
import java.util.Set;

public class DataProvider {
    public String name;
    public Set<String> files;
    public int count;

    public DataProvider(String name) {
        this.name = name;
        this.files = new HashSet<String>();
        this.count = 1;
    }

    @Override
    public String toString() {
        return "DataProvider{" +
            "name='" + name + '\'' +
            ", count=" + count +
            '}';
    }
}
