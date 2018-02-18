package de.tuberlin.dbpro.ws17.kg_neo4j.old.importation;

import de.tuberlin.dbpro.ws17.kg_neo4j.old.model.EquivalenceRelation;

import java.util.Comparator;

public class EquivalenceRelationsComparator implements Comparator<EquivalenceRelation> {

    private static GcdIdsComparator gcdIdsComparator = new GcdIdsComparator();

    @Override
    public int compare(EquivalenceRelation o1, EquivalenceRelation o2) {
        return gcdIdsComparator.compare(o1.gcdId, o2.gcdId);
        //return 0;
    }

}
