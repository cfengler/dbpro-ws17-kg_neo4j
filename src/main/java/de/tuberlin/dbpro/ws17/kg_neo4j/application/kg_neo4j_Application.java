package de.tuberlin.dbpro.ws17.kg_neo4j.application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

import java.util.ArrayList;

public class kg_neo4j_Application extends Application {

    private static ArrayList<String> labelsString;
    private static ArrayList<Button> labelsButton;
    private static ArrayList<String> nodesString;
    private static ArrayList<Button> nodesButton;
    private static boolean labelsHidden;
    private static boolean nodesHidden;
    private static GridPane paneFoundation;
    private static FlowPane paneLabels;
    private static FlowPane paneNodes;
    private static FlowPane paneResult;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        labelsString = new ArrayList<String>();
        labelsButton = new ArrayList<Button>();
        nodesString = new ArrayList<String>();
        nodesButton = new ArrayList<Button>();
        labelsHidden = false;
        nodesHidden = false;
        primaryStage.setTitle("DBPRO");

        paneFoundation = new GridPane();
        paneFoundation.setHgap(10);
        paneFoundation.setVgap(10);
        paneFoundation.setPadding(new Insets(10, 10, 10, 10));

        paneLabels = new FlowPane();
        paneLabels.setVgap(4);
        paneLabels.setHgap(4);
        paneLabels.setPadding(new Insets(5, 5, 5, 5));
        paneLabels.setPrefWrapLength(0);
        paneFoundation.add(paneLabels, 0, 0);

        paneNodes = new FlowPane();
        paneNodes.setVgap(4);
        paneNodes.setHgap(4);
        paneNodes.setPadding(new Insets(5, 5, 5, 5));
        paneNodes.setPrefWrapLength(500);
        paneFoundation.add(paneNodes, 0, 1);

        paneResult = new FlowPane();
        paneResult.setVgap(4);
        paneResult.setHgap(4);
        paneResult.setPadding(new Insets(5, 5, 5, 5));
        paneResult.setPrefWrapLength(500);
        paneFoundation.add(paneResult, 0, 2);

        for(int i = 0;i < 10;i++) {
            addLabel("Label " + (i + 1));
        }

        for(int i = 0;i < 10;i++) {
            addNode("Node " + (i + 1));
        }

        Circle circle = new Circle(20, 20f, 7);
        circle.setFill(Color.RED);

        Ellipse ellipse = new Ellipse();
        ellipse.setRadiusX(50);
        ellipse.setRadiusY(25);
        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(3);
        ellipse.setFill(Color.TRANSPARENT);
        paneResult.getChildren().add(ellipse);

        paneResult.getChildren().add(circle);

        primaryStage.setScene(new Scene(paneFoundation, 500, 500));
        primaryStage.show();
    }

    public static void addLabel(String label) {
        labelsString.add(label);
        Button btn = new Button(label);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Label wurde gewählt");
                paneLabels.getChildren().remove(0, paneLabels.getChildren().size());
                paneLabels.getChildren().add(new Label("Label ausgewählt"));
                labelsHidden = true;
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
                paneNodes.getChildren().remove(0, paneNodes.getChildren().size());
                paneNodes.getChildren().add(new Label("Node ausgewählt"));
                nodesHidden = true;
            }
        });
        nodesButton.add(btn);
        if(!nodesHidden) {
            paneNodes.getChildren().add(btn);
        }
    }

    private static void hideLabels() {

    }
}
