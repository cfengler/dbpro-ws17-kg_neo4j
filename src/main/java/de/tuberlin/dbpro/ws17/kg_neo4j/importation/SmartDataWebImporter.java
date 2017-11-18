package de.tuberlin.dbpro.ws17.kg_neo4j.importation;

import org.neo4j.driver.v1.*;

import static org.neo4j.driver.v1.Values.parameters;

import java.util.List;

public class SmartDataWebImporter {

    public void doIt() {
        Driver driver = GraphDatabase.driver("bolt://localhost:7687");

        Session session = driver.session();

        List<Record> result = session.writeTransaction(new TransactionWork<List<Record>>() {
            public List<Record> execute(Transaction transaction) {
                StatementResult result = transaction.run(
                    "CREATE (Christian:Person {name:$name}) " +
                        "RETURN Christian",
                    parameters( "name", "Christian" )
                );
                return result.list();
            }
        });

        for ( Record record: result ) {
            System.out.println( record.get(0).get("name") );
        }


        driver.close();
    }


}
