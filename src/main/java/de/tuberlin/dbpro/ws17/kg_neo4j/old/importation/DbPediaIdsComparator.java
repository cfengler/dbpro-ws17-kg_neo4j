package de.tuberlin.dbpro.ws17.kg_neo4j.old.importation;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbPediaId;

import java.util.Comparator;

public class DbPediaIdsComparator implements Comparator<DbPediaId> {
    @Override
    public int compare(DbPediaId o1, DbPediaId o2) {
        return o1.getValue().compareTo(o2.getValue());
    }
}
