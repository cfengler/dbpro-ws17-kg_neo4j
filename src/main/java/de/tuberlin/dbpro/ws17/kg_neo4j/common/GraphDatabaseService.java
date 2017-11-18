package de.tuberlin.dbpro.ws17.kg_neo4j.common;

import org.neo4j.driver.v1.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphDatabaseService implements Closeable {

    private Driver driver = null;
    private Session session = null;

    public GraphDatabaseService() {
        driver = GraphDatabase.driver("bolt://localhost:7687");
        session = driver.session();
    }

    public List<Node> getAllNodes() {
        return session.writeTransaction(new TransactionWork<List<Node>>() {
            @Override
            public List<Node> execute(Transaction transaction) {
                //TODO: search query for all nodes or make it with seachNode(null, null)?
                String query = "";
                return new ArrayList<Node>();
            }
        });
    }

    public List<Node> getNodes(List<String> labels) {
        return getNodes(labels, null);
    }

    public List<Node> getNodes(Map<String, String> properties) {
        return getNodes(null, properties);
    }

    public List<Node> getNodes(List<String> labels, Map<String, String> properties) {

        return session.writeTransaction(new TransactionWork<List<Node>>() {
            public List<Node> execute(Transaction transaction) {

                String query = "MATCH (searchedNode";

                if (labels != null && !labels.isEmpty()) {
                    query += CypherService.getCypher(labels);
                }

                if (properties != null && !properties.isEmpty()) {
                    query += " " + CypherService.getCypther(properties);
                }
                query += ")" + System.lineSeparator();
                query += "RETURN searchedNode";

                StatementResult result = transaction.run(query);
                //TODO: map StatementResult to List<Node>
                return new ArrayList<Node>();// result.list();
            }
        });
    }

    public void addNode(Node node) {
        session.writeTransaction(new TransactionWork<Void>() {
            public Void execute(Transaction transaction) {

                String query = "CREATE " + node.getCypher("n");

                transaction.run(query);

                return null;
            }
        });
    }

    public void updateNode(Node node) {
        //TODO: update Node
    }

    public void deleteNode(Node node) {
        session.writeTransaction(new TransactionWork<Void>() {
            @Override
            public Void execute(Transaction transaction) {

                String query = "MATCH " + node.getCypher("n") + System.lineSeparator();
                query += "DELETE n";

                transaction.run(query);

                return null;
            }
        });
    }

    public void deleteNodeWithAllRelationships(Node node) {
        session.writeTransaction(new TransactionWork<Void>() {
            @Override
            public Void execute(Transaction transaction) {

                String query = "MATCH " + node.getCypher("n") + System.lineSeparator();
                query += "DETACH DELETE n";

                transaction.run(query);

                return null;
            }
        });
    }

    public List<Relation> getRelations(List<String> labels) {
        return getRelations(labels, null);
    }

    public List<Relation> getRelations(Map<String, String> properties) {
        return getRelations(null, properties);
    }

    public List<Relation> getRelations(List<String> labels, Map<String, String> properties) {
        //TODO: getRelations
        return null;
    }

    public void addRelation(Relation relation) throws Exception {
        if (relation == null)
            throw new Exception("relation null");

        if (relation.firstNode == null)
            throw new Exception("relation.firstNode null");

        if (relation.secondNode == null)
            throw new Exception("relation.secondNode null");

        session.writeTransaction(new TransactionWork<Void>() {
            @Override
            public Void execute(Transaction transaction) {

                String query = "MATCH " + relation.firstNode.getCypher("firstNode") + "," + relation.secondNode.getCypher("secondNode") + System.lineSeparator();
                query += "CREATE (firstNode)-" + relation.getCypher("relation") + "->(secondNode)";

                transaction.run(query);

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

    public void deleteAllEntities(boolean reallyWantToDoThis) {
        if (reallyWantToDoThis) {
            session.writeTransaction(new TransactionWork<Void>() {
                @Override
                public Void execute(Transaction transaction) {
                    String query = "MATCH (n)" + System.lineSeparator();
                    query += "DETACH DELETE n";

                    transaction.run(query);

                    return null;
                }
            });
        }
    }

    @Override
    public void close() throws IOException {
        session.close();
        driver.close();
    }
}
