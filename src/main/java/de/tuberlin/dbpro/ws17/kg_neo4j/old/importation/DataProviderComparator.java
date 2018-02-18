package de.tuberlin.dbpro.ws17.kg_neo4j.old.importation;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DataProvider;

import java.util.Comparator;

public class DataProviderComparator implements Comparator<DataProvider> {
    @Override
    public int compare(DataProvider o1, DataProvider o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
