package de.tuberlin.dbpro.ws17.kg_neo4j.importation;

import de.tuberlin.dbpro.ws17.kg_neo4j.common.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GcdIdDbProIdImporter {

    public static void main(String[] args) {
        try (GraphDatabaseService graphDatabaseService = new GraphDatabaseService()) {
            Set<Long> gcdIds = GcdIdsService.getGcdIds();
            List<Long> listGcdIds = new ArrayList<Long>(gcdIds);

            for (int i = 0; i < listGcdIds.size(); i++) {
                System.out.println((i + 1) + "/" + listGcdIds.size());
                long gcdId = listGcdIds.get(i);
                Node nodeGcdId = SdwEntryToGraphEntityMapper.getNodeByGcdId(gcdId);
                Node nodeDbProId = SdwEntryToGraphEntityMapper.getNodeByDbProId(i + 1);
                graphDatabaseService.createNode(nodeGcdId);
                graphDatabaseService.createNode(nodeDbProId);

                Relation relationSameAs = new Relation(nodeGcdId, nodeDbProId);
                relationSameAs.labels.add("SAME_AS");

                try {
                    graphDatabaseService.createRelation(relationSameAs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
