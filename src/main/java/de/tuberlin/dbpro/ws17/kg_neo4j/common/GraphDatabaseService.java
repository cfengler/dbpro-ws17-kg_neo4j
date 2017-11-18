package de.tuberlin.dbpro.ws17.kg_neo4j.common;

import org.neo4j.driver.v1.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GraphDatabaseService implements Closeable {

    private Driver driver = null;
    private Session session = null;

    public GraphDatabaseService() {
        driver = GraphDatabase.driver("bolt://localhost:7687");
        session = driver.session();

        List l;
        l.
    }

    public List<Node> getAllNodes() {
        List<Record> records = session.writeTransaction(new TransactionWork<List<Record>>() {
            @Override
            public List<Record> execute(Transaction transaction) {

            }
        })
    }

    public Node addNode(Node newNode) {

        session.writeTransaction(new TransactionWork<Void>() {
            public Void execute(Transaction transaction) {

                String query = "CREATE " + newNode.getCypher("newNode");

                System.out.println(query);

                transaction.run(query);

                StatementResult result;

                return null;
            }
        });
    }

    public Node updateNode(Node node) {

    }

    public Node deleteNode(Node node) {

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


    @Override
    public void close() throws IOException {
        session.close();
        driver.close();
    }
}
