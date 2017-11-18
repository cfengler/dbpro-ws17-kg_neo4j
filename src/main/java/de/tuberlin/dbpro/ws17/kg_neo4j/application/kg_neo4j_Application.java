package de.tuberlin.dbpro.ws17.kg_neo4j.application;

import de.tuberlin.dbpro.ws17.kg_neo4j.importation.SmartDataWebImporter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class kg_neo4j_Application extends Application {

    private static ArrayList<String> labelsString;
    private static ArrayList<Button> labelsButton;
    private static ArrayList<String> nodesString;
    private static ArrayList<Button> nodesButton;
    private static ArrayList<String> relationsString;
    private static ArrayList<Button> relationsButton;
    private static boolean labelsHidden;
    private static boolean nodesHidden;
    private static boolean relationsHidden;
    private static BorderPane paneFoundation;
    private static GridPane paneSelection;
    private static VBox paneLabels;
    private static VBox paneNodes;
    private static VBox paneRelations;
    private static FlowPane paneResult;
    private static Label labelLabels;
    private static Label labelNodes;
    private static Label labelRelations;
    private static Label labelHeadline;
    private static Button showLabels;
    private static Button showNodes;
    private static Button showRelations;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        labelsString = new ArrayList<String>();
        labelsButton = new ArrayList<Button>();
        nodesString = new ArrayList<String>();
        nodesButton = new ArrayList<Button>();
        relationsString = new ArrayList<String>();
        relationsButton = new ArrayList<Button>();
        labelsHidden = false;
        nodesHidden = false;
        relationsHidden = false;
        primaryStage.setTitle("DBPRO");

        initPanes();

        initButtons();

        initData();

        primaryStage.setScene(new Scene(paneFoundation, 500, 500));
        primaryStage.show();
    }

    private static void initPanes() {
        paneFoundation = new BorderPane();
        Label labelHeadline = new Label("DBPRO Neo4J Knowledge Graph");
        paneFoundation.setTop(labelHeadline);

        paneSelection = new GridPane();
        paneSelection.setHgap(10);
        paneSelection.setVgap(10);
        paneSelection.setPadding(new Insets(5, 5, 5, 5));
        paneFoundation.setLeft(paneSelection);

        paneLabels = new VBox();
        paneLabels.setPadding(new Insets(5, 5, 5, 5));
        paneSelection.add(paneLabels, 0, 0);
        labelLabels = new Label("Labels");
        paneLabels.getChildren().add(labelLabels);

        paneRelations = new VBox();
        paneRelations.setPadding(new Insets(5, 5, 5, 5));
        paneSelection.add(paneRelations, 2, 0);
        labelRelations = new Label("Relationen");
        paneRelations.getChildren().add(labelRelations);

        paneNodes = new VBox();
        paneNodes.setPadding(new Insets(5, 5, 5, 5));
        paneSelection.add(paneNodes, 1, 0);
        labelNodes = new Label("Nodes");
        paneNodes.getChildren().add(labelNodes);

        paneResult = new FlowPane();
        paneResult.setVgap(4);
        paneResult.setHgap(4);
        paneResult.setPadding(new Insets(5, 5, 5, 5));
        paneResult.setPrefWrapLength(500);
        paneFoundation.setCenter(paneResult);
    }

    private static void initData() {
        SmartDataWebImporter importer = new SmartDataWebImporter();
        importer.Connect();
        List<String> importedLabels = importer.getAllLabels();
        importer.Disconnect();

        for(String s: importedLabels) {
            addLabel(s);
        }

        for(int i = 0;i < 10;i++) {
            addNode("Node " + (i + 1));
        }

        for(int i = 0;i < 10;i++) {
            addRelation("Relation " + (i + 1));
        }

        Circle circle = new Circle(20, 20f, 7);
        circle.setFill(Color.RED);
        paneResult.getChildren().add(circle);

        Ellipse ellipse = new Ellipse();
        ellipse.setRadiusX(50);
        ellipse.setRadiusY(25);
        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(3);
        ellipse.setFill(Color.TRANSPARENT);
        paneResult.getChildren().add(ellipse);
    }

    private static void initButtons() {
        showLabels = new Button("anzeigen");
        showLabels.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Label werden angezeigt");
                unhideLabels();
            }
        });
        showNodes = new Button("anzeigen");
        showNodes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Nodes werden angezeigt");
                unhideNodes();
            }
        });
        showRelations = new Button("anzeigen");
        showRelations.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Relations werden angezeigt");
                unhideRelations();
            }
        });
    }

    public static void addLabel(String label) {
        labelsString.add(label);
        Button btn = new Button(label);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Label wurde gewählt");
                hideLabels();
            }
        });
        labelsButton.add(btn);
        if(!labelsHidden) {
            paneLabels.getChildren().add(btn);
        }
    }

    public static void addNode(String node) {
        nodesString.add(node);
        Button btn = new Button(node);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Node wurde gewählt");
                hideNodes();
            }
        });
        nodesButton.add(btn);
        if(!nodesHidden) {
            paneNodes.getChildren().add(btn);
        }
    }

    public static void addRelation(String relation) {
        relationsString.add(relation);
        Button btn = new Button(relation);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Relation wurde gewählt");
                hideRelations();
            }
        });
        relationsButton.add(btn);
        if(!relationsHidden) {
            paneRelations.getChildren().add(btn);
        }
    }

    private static void hideLabels() {
        paneLabels.getChildren().remove(0, paneLabels.getChildren().size());
        paneLabels.getChildren().add(labelLabels);
        paneLabels.getChildren().add(new Label("Label ausgewählt"));
        paneLabels.getChildren().add(showLabels);
        labelsHidden = true;
    }

    private static void hideNodes() {
        paneNodes.getChildren().remove(0, paneNodes.getChildren().size());
        paneNodes.getChildren().add(labelNodes);
        paneNodes.getChildren().add(new Label("Node ausgewählt"));
        paneNodes.getChildren().add(showNodes);
        nodesHidden = true;
    }

    private static void hideRelations() {
        paneRelations.getChildren().remove(0, paneRelations.getChildren().size());
        paneRelations.getChildren().add(labelRelations);
        paneRelations.getChildren().add(new Label("Relation ausgewählt"));
        paneRelations.getChildren().add(showRelations);
        relationsHidden = true;
    }

    private static void unhideLabels() {
        paneLabels.getChildren().remove(0, paneLabels.getChildren().size());
        paneLabels.getChildren().add(labelLabels);
        for(Button btn: labelsButton) {
            paneLabels.getChildren().add(btn);
        }
        labelsHidden = true;
    }

    private static void unhideNodes() {
        paneNodes.getChildren().remove(0, paneNodes.getChildren().size());
        paneNodes.getChildren().add(labelNodes);
        for(Button btn: nodesButton) {
            paneNodes.getChildren().add(btn);
        }
        nodesHidden = true;
    }

    private static void unhideRelations() {
        paneRelations.getChildren().remove(0, paneRelations.getChildren().size());
        paneRelations.getChildren().add(labelRelations);
        for(Button btn: relationsButton) {
            paneRelations.getChildren().add(btn);
        }
        relationsHidden = true;
    }
}
