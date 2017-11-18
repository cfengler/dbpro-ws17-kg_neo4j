package de.tuberlin.dbpro.ws17.kg_neo4j.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphDatabaseServiceTest {

    private static GraphDatabaseService dbService = null;

    private static Node nodeChristian = null;
    private static Node nodeMagnus = null;
    private static Node nodeJulian = null;

    private static Relation relationChristianJulian = null;
    private static Relation relationJulianMagnus = null;
    private static Relation relationMagnusChristian= null;

    public static void main(String[] args) {
        dbService = new GraphDatabaseService();

        dbService.deleteAllEntities(true);

        AddTestData();



        List<String> allLabels = dbService.getAllLabels();

        for (String label: allLabels) {
            System.out.println("Label:" + label);
        }

        List<String> searchLabels = new ArrayList<String>();
        searchLabels.add("Person");
        List<Node> allNodes = dbService.getNodes(searchLabels);

        for (Node node: allNodes) {
            System.out.println("Node:" + node.getCypher("node"));
        }

        try {
            dbService.close();
        }
        catch (Exception e) {

        }
    }

    private static void AddTestData() {
        addTestNodes();
        addTestRelations();
    }

    private static void addTestNodes() {
        nodeChristian = new Node();
        nodeChristian.labels = new ArrayList<String>();
        nodeChristian.labels.add("Person");
        nodeChristian.labels.add("Meister");

        nodeChristian.properties = new HashMap<String, String>();
        nodeChristian.properties.put("prename", "Christian");
        nodeChristian.properties.put("name", "Fengler");

        nodeMagnus = new Node();
        nodeMagnus.labels.add("Person");
        nodeMagnus.labels.add("Padawan");

        nodeMagnus.properties = new HashMap<String, String>();
        nodeMagnus.properties.put("prename", "Magnus");
        nodeMagnus.properties.put("name", "Brieler");

        nodeJulian = new Node();
        nodeJulian.labels.add("Person");
        nodeJulian.labels.add("Padawan");

        nodeJulian.properties = new HashMap<String, String>();
        nodeJulian.properties.put("prename", "Julian");
        nodeJulian.properties.put("name", "Legler");

        dbService.addNode(nodeChristian);
        dbService.addNode(nodeMagnus);
        dbService.addNode(nodeJulian);
    }

    private static void addTestRelations() {
        relationChristianJulian = new Relation(nodeChristian, nodeJulian);
        relationChristianJulian.labels.add("WORKED_WITH");

        relationJulianMagnus = new Relation(nodeJulian, nodeMagnus);
        relationJulianMagnus.labels.add("WORKED_WITH");

        relationMagnusChristian = new Relation(nodeMagnus, nodeChristian);
        relationMagnusChristian.labels.add("WORKED_WITH");

        try {
            dbService.addRelation(relationChristianJulian);
            dbService.addRelation(relationJulianMagnus);
            dbService.addRelation(relationMagnusChristian);
        }
        catch (Exception e) {

        }
    }

}
