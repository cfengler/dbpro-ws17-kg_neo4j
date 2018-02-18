package de.tuberlin.dbpro.ws17.kg_neo4j.old.importation;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbProId;

import java.util.Comparator;

public class DbProIdsComparator implements Comparator<DbProId> {
    @Override
    public int compare(DbProId o1, DbProId o2) {
        return Long.compare(o1.getValue(), o2.getValue());
    }
}
