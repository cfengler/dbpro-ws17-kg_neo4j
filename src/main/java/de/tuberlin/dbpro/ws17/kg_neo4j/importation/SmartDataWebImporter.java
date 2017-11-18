package de.tuberlin.dbpro.ws17.kg_neo4j.importation;

import de.tuberlin.dbpro.ws17.kg_neo4j.common.GraphDatabaseService;
import de.tuberlin.dbpro.ws17.kg_neo4j.common.Node;
import org.neo4j.driver.v1.*;

import java.io.IOException;
import java.util.*;

public class SmartDataWebImporter {

    private GraphDatabaseService graphDatabaseService = null;

    public SmartDataWebImporter() {

    }

    public void Connect() {
        graphDatabaseService = new GraphDatabaseService();
    }

    public void Disconnect() {
        try {
            graphDatabaseService.close();
        }
        catch (IOException e) {

        }
    }

    public void createData() {

        Node nodeChristian = new Node();
        nodeChristian.labels = new ArrayList<String>();
        nodeChristian.labels.add("Person");
        nodeChristian.labels.add("Meister");

        nodeChristian.properties = new HashMap<String, String>();
        nodeChristian.properties.put("prename", "Christian");
        nodeChristian.properties.put("name", "Fengler");

        Node nodeMagnus = new Node();
        nodeMagnus.labels.add("Person");
        nodeMagnus.labels.add("Padawan");

        nodeMagnus.properties = new HashMap<String, String>();
        nodeMagnus.properties.put("prename", "Magnus");
        nodeMagnus.properties.put("name", "Brieler");

        Node nodeJulian = new Node();
        nodeJulian.labels.add("Person");
        nodeJulian.labels.add("Padawan");

        nodeJulian.properties = new HashMap<String, String>();
        nodeJulian.properties.put("prename", "Julian");
        nodeJulian.properties.put("name", "Legler");

        graphDatabaseService.addNode(nodeChristian);
        graphDatabaseService.addNode(nodeMagnus);
        graphDatabaseService.addNode(nodeJulian);
    }

    private void loadNodeId(Node node) {

        session.writeTransaction(new TransactionWork<Object>() {
            @Override
            public Object execute(Transaction transaction) {
                return null;
            }
        });


    }









//    public List<Node> getAllNodes() {
//        List<Record> records session.writeTransaction(new TransactionWork<List<Record>>() {
//            public
//        });
//    }

//    private void CreateNode(List<String> labels, Map<String, Object> properties) {
//
//        List<Record> result = session.writeTransaction(new TransactionWork<List<Record>>() {
//            public List<Record> execute(Transaction transaction) {
//                StatementResult result = transaction.run(
//                    "CREATE (Christian:Person {name:$name}) " +
//
//                        "RETURN Christian",
//                    parameters( "name", "Christian" )
//                );
//                return result.list();
//            }
//        });
//
//    }


}
