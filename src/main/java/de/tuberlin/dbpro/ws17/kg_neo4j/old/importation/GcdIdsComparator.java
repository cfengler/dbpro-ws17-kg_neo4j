package de.tuberlin.dbpro.ws17.kg_neo4j.old.importation;

import java.util.Comparator;

public class GcdIdsComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        if (o1.startsWith("f") && o2.startsWith("f")) {
            return Integer.parseInt(o1.substring(1)) - Integer.parseInt(o2.substring(1));
        }
        else if (o1.startsWith("f") && !o2.startsWith("f")) {
            return -1;
        }
        else if (!o1.startsWith("f") && o2.startsWith("f")) {
            return 1;
        }
        else {
            return Integer.parseInt(o1) - Integer.parseInt(o2);
        }
    }
}
