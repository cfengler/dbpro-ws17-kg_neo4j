package tub.dbpro.ws1718.kg_neo4j;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.configuration.BoltConnector;
import org.neo4j.kernel.impl.factory.GraphDatabaseFacade;

import java.io.File;
import java.util.Iterator;

public class App 
{
    private static enum DbRelationshipType implements RelationshipType
    {
        WORK_WITH
    }

    private static GraphDatabaseService graphDatabaseService = null;

    public static void main( String[] args )
    {
        //lokale GraphDB
        File dbFile = new File("testGraph.neo4jdb");



        BoltConnector boltConnector = new BoltConnector("bolt");
        graphDatabaseService = new GraphDatabaseFactory().addURLAccessRule("bolt", )

        //graphDatabaseService = new GraphDatabaseFactory().newEmbeddedDatabase(dbFile);


        createPersonsWithRelations();

        Label labelPerson = Label.label("Person");
        ResourceIterator<Node> nodes = graphDatabaseService.findNodes(labelPerson);
        while (nodes.hasNext()) {
            Node node = nodes.next();

            Iterator<Label> labels = node.getLabels().iterator();
            System.out.print("< ");
            while (labels.hasNext()) {
                Label label = labels.next();
                System.out.print(label.name() + " ");
            }
            System.out.println("> " + node.getProperty("name").toString());
        }

        removePersonsWithRelations();


//        Driver driver = GraphDatabase.driver("bolt://localhost:7687");
//
//        Session session = driver.session();
//
//        List<Record> result = session.writeTransaction(new TransactionWork<List<Record>>() {
//            public List<Record> execute(Transaction transaction) {
//                StatementResult result = transaction.run(
//                    "MATCH (Christian:Person {name:$name}) " +
//                    "RETURN Christian",
//                    parameters( "name", "Christian" )
//                );
//                return result.list();
//            }
//        });
//
//        for ( Record record: result ) {
//            System.out.println( record.get(0).get("name") );
//        }
//
//
//        driver.close();
    }

    private static void createPersonsWithRelations() {

        Label labelPerson = Label.label("Person");

        //Alle für einen und einer für alle.
        try ( Transaction tx = graphDatabaseService.beginTx() )
        {
            Node nodeChristian = graphDatabaseService.createNode(labelPerson);
            nodeChristian.addLabel(Label.label("Profi"));
            nodeChristian.setProperty("name", "Christian");
            System.out.println(nodeChristian.toString());

            Node nodeJulian = graphDatabaseService.createNode(labelPerson);
            nodeJulian.setProperty("name", "Julian");
            System.out.println(nodeJulian.toString());

            Node nodeMagnus = graphDatabaseService.createNode(labelPerson);
            nodeMagnus.setProperty("name", "Magnus");
            System.out.println(nodeMagnus.toString());

            Relationship relationshipChristianJulian = nodeChristian.createRelationshipTo(nodeJulian, DbRelationshipType.WORK_WITH);
            Relationship relationshipJulianMagnus = nodeJulian.createRelationshipTo(nodeMagnus, DbRelationshipType.WORK_WITH);
            Relationship relationshipMagnusChristian = nodeMagnus.createRelationshipTo(nodeChristian, DbRelationshipType.WORK_WITH);

            tx.success();
        }
    }

    private static void removePersonsWithRelations() {

        Label labelPerson = Label.label("Person");

        try ( Transaction tx = graphDatabaseService.beginTx() ){
            graphDatabaseService.findNodes(labelPerson);

            ResourceIterator<Node> nodes = graphDatabaseService.findNodes(labelPerson);
            while (nodes.hasNext()) {
                Node node = nodes.next();

                Iterator<Relationship> relationships = node.getRelationships().iterator();
                while (relationships.hasNext()) {
                    Relationship relationship = relationships.next();

                    relationship.delete();
                    System.out.println("relationship.delete();");
                }

                node.delete();
                System.out.println("node.delete();");
            }

            tx.success();
        }
    }

//    private static String OnlyNode(Node node) {
//        String text = "(";
//
//        Iterator<Label> nodeLabels = node.getLabels().iterator();
//        for (Label label = nodeLabels.next(); nodeLabels.hasNext(); ) {
//            text += ":" + label;
//        }
//        text += ")" + System.lineSeparator();
//
//
//    }
}
