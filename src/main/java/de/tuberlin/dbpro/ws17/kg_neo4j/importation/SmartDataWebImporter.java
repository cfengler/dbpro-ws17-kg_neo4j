package de.tuberlin.dbpro.ws17.kg_neo4j.importation;

import de.tuberlin.dbpro.ws17.kg_neo4j.common.GraphDatabaseService;
import de.tuberlin.dbpro.ws17.kg_neo4j.common.Node;
import de.tuberlin.dbpro.ws17.kg_neo4j.common.Relation;
import org.neo4j.driver.v1.*;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class SmartDataWebImporter {

//    private GraphDatabaseService graphDatabaseService = null;
//    private Path dataDirPath;
//    private List<Path> dataFilePathList = new ArrayList<>();
//
//    public SmartDataWebImporter(Path dataDirPath) {
//        this.dataDirPath = dataDirPath;
//        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(dataDirPath.toString()))) {
//            for (Path path : directoryStream) {
//                dataFilePathList.add(path);
//            }
//        } catch (IOException ex) {}
//
//        // Debug ausgaben
//        System.out.println("Path: " + dataDirPath.toAbsolutePath().toString() + "\n Files: " + dataFilePathList.toString());
//        try (BufferedReader br = new BufferedReader(new FileReader(dataFilePathList.get(0).toAbsolutePath().toString()))) {
//            String line;
//            // 82000
//            for(int i = 0; (line = br.readLine()) != null && i < 2000; i++) {
//                System.out.println(line);
//            }
//        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public void connect() {
//        graphDatabaseService = new GraphDatabaseService();
//    }
//
//    public void disconnect() {
//        graphDatabaseService.close();
//    }
//
//    public void createData() {
//
//        Pattern p = Pattern.compile("[<](?<subject>.+?)[>]\\s[<](?<predicate>.+?)[>]\\s[<](?<object>.+?)[>]\\s[<](.+?)[>]\\s.");
//        Matcher m;
//
//        try (BufferedReader br = new BufferedReader(new FileReader(dataFilePathList.get(0).toAbsolutePath().toString()))) {
//            String line;
//            // 82000
//            for(int i = 0; (line = br.readLine()) != null && i < 2000; i++) {
//                m = p.matcher(line);
//                Node subject = new Node();
//                subject.labels = new ArrayList<String>();
//                subject.labels.add(m.group("subject"));
//                subject.properties = new HashMap<String, String>();
//                subject.properties.put("Data", m.group("subject"));
//
//                Node object = new Node();
//                object.labels = new ArrayList<String>();
//                object.labels.add(m.group("object"));
//                object.properties = new HashMap<String, String>();
//                object.properties.put("Data", m.group("object"));
//
//                Relation predicate = new Relation(subject, object);
//                predicate.labels = new ArrayList<String>();
//                predicate.labels.add(m.group("predicate"));
//                predicate.properties = new HashMap<String, String>();
//                predicate.properties.put("Data", m.group("predicate"));
//
//                graphDatabaseService.addNode(subject);
//                graphDatabaseService.addNode(object);
//                graphDatabaseService.addRelation(predicate);
//
//            }
//        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//
//    public List<String> getAllLabels() {
//        return null;
//    }
//
//
////    public List<Node> getAllNodes() {
////        List<Record> records session.writeTransaction(new TransactionWork<List<Record>>() {
////            public
////        });
////    }
//
////    private void CreateNode(List<String> labels, Map<String, Object> properties) {
////
////        List<Record> result = session.writeTransaction(new TransactionWork<List<Record>>() {
////            public List<Record> execute(Transaction transaction) {
////                StatementResult result = transaction.run(
////                    "CREATE (Christian:Person {name:$name}) " +
////
////                        "RETURN Christian",
////                    parameters( "name", "Christian" )
////                );
////                return result.list();
////            }
////        });
////
////    }


}
