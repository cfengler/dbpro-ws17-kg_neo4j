package de.tuberlin.dbpro.ws17.kg_neo4j.importation;

import org.neo4j.driver.v1.*;

import static org.neo4j.driver.v1.Values.parameters;

import java.util.*;

public class SmartDataWebImporter {

    private Driver driver = null;
    private Session session = null;

    public SmartDataWebImporter() {

    }

    public void Connect() {
        driver = GraphDatabase.driver("bolt://localhost:7687");
        session = driver.session();

    }

    public void Disconnect() {

        session.close();
        driver.close();
    }

    public void createData() {

        Node nodeChristian = new Node();
        nodeChristian.labels = new ArrayList<String>();
        nodeChristian.labels.add("Person");
        nodeChristian.labels.add("Profi");

        nodeChristian.properties = new HashMap<String, String>();
        nodeChristian.properties.put("prename", "Christian");
        nodeChristian.properties.put("name", "Fengler");

        createNode(nodeChristian);
//        List<Record> result = session.writeTransaction(new TransactionWork<List<Record>>() {
//            public List<Record> execute(Transaction transaction) {
//                StatementResult result = transaction.run(
//                     "CREATE (Christian:Person {name:$name}) " +
//
//                        "RETURN Christian",
//                    parameters( "name", "Christian" )
//                );
//                return result.list();
//            }
//        });
//
//
//
//        for ( Record record: result ) {
//            System.out.println( record.get(0).get("name") );
//        }


    }

    private void createNode(Node newNode) {

        session.writeTransaction(new TransactionWork<Void>() {
            public Void execute(Transaction transaction) {

                String query = "CREATE (node";
                for (String label: newNode.labels) {
                    query += ":" + label;
                }

                if (!newNode.properties.isEmpty()) {
                    query += " { ";

                    for (Iterator<Map.Entry<String, String>> mapEntryIterator = newNode.properties.entrySet().iterator(); mapEntryIterator.hasNext();) {
                        Map.Entry<String, String> mapEntry = mapEntryIterator.next();

                        query += mapEntry.getKey() + ": '" + mapEntry.getValue() + "'";

                        if (mapEntryIterator.hasNext()) {
                            query += ", ";
                        }
                    }

                    query += " }";
                }
                query += ")";

                System.out.println(query);

                transaction.run(query);

                StatementResult result;

                return null;
            }
        });
    }

    public List<String> getAllLabels() {
        List<Record> records = session.writeTransaction(new TransactionWork<List<Record>>() {
            public List<Record> execute(Transaction transaction) {
                String query = "MATCH (n) RETURN distinct labels(n)";
                return transaction.run(query).list();
            }
        });

        List<String> result = new ArrayList<String>();

        for (Record record: records) {
            result.add(record.values().toString());
        }

        return result;
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
