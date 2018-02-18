package de.tuberlin.dbpro.ws17.kg_neo4j.old.importation;

public class GcdId_DbProId_Importer {

    public static void main(String[] args) {
        //try (GraphDatabaseService graphDatabaseService = new GraphDatabaseService()) {
            //List<String> gcdIds = ER_01_GcdIds_Service.getGcdIds();
            //List<Long> listGcdIds = new ArrayList<Long>(gcdIds);

//            List<Relation> tempRelations = new ArrayList<>();
//
//            for (int i = 0; i < listGcdIds.size(); i++) {
//                System.out.println((i + 1) + "/" + listGcdIds.size());
//                long gcdId = listGcdIds.get(i);
//                Node nodeGcdId = SdwEntryToGraphEntityMapper.getNodeByGcdId(gcdId);
//                Node nodeDbProId = SdwEntryToGraphEntityMapper.getNodeByDbProId(i + 1);
//
//                Relation relationSameAs = new Relation(nodeGcdId, nodeDbProId);
//                relationSameAs.labels.add("SAME_AS");
//
//                tempRelations.add(relationSameAs);
//
//                if (tempRelations.size() > 99) {
//                    graphDatabaseService.createRelationsWithNodes(tempRelations);
//                    tempRelations.clear();
//                }
//            }
//
//            graphDatabaseService.createRelationsWithNodes(tempRelations);
//            tempRelations.clear();
        //}
    }
}
