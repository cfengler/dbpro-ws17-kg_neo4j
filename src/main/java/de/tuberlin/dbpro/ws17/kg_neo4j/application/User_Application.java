package de.tuberlin.dbpro.ws17.kg_neo4j.application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;

public class User_Application extends Application {

    private static final BorderPane pnFoundation = new BorderPane();
    private static final GridPane pnSearch = new GridPane();
    private static final BorderPane pnSearchField = new BorderPane();
    private static final HBox pnSearchType = new HBox();
    private static final VBox pnSelection = new VBox();
    private static final ScrollPane spSelection = new ScrollPane();

    private static final Label lblHeadline = new Label();
    private static final Label lblSelection = new Label();

    private static final Button btnSearch = new Button();

    private static final TextField tfSearch = new TextField();

    private static final RadioButton rbNodes = new RadioButton();
    private static final RadioButton rbLabels = new RadioButton();
    private static final RadioButton rbEdges = new RadioButton();
    private static final ToggleGroup tgSearch = new ToggleGroup();

    public void start(Stage primaryStage) {
        primaryStage.setTitle("DBPRO");

        pnSearch.setAlignment(Pos.TOP_CENTER);
        pnSearch.setVgap(5);
        pnSearchType.setAlignment(Pos.TOP_CENTER);
        pnSearchType.setSpacing(20);

        lblHeadline.setText("DBPRO Neo4J Knowledge Graph");
        lblHeadline.setFont(new Font("Helvetica Neue", 30));
        pnSearch.add(lblHeadline, 0, 0);

        tfSearch.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    search();
                }
            }
        });
        tfSearch.setPromptText("Suchbegriff");
        tfSearch.requestFocus();
        pnSearchField.setCenter(tfSearch);

        btnSearch.setText("suchen");
        btnSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                search();
            }
        });
        pnSearchField.setRight(btnSearch);
        pnSearch.add(pnSearchField, 0, 1);

        rbNodes.setText("Knoten");
        rbNodes.setSelected(true);
        rbNodes.setToggleGroup(tgSearch);
        pnSearchType.getChildren().add(rbNodes);

        rbLabels.setText("Bezeichnung");
        rbLabels.setToggleGroup(tgSearch);
        pnSearchType.getChildren().add(rbLabels);

        rbEdges.setText("Kanten");
        rbEdges.setToggleGroup(tgSearch);
        pnSearchType.getChildren().add(rbEdges);
        pnSearch.add(pnSearchType, 0, 2);

        pnFoundation.setTop(pnSearch);

        lblSelection.setText("Auswahl");
        lblSelection.setFont(new Font("Helvetica Neue", 13));
        pnSelection.getChildren().add(lblSelection);

        spSelection.setContent(pnSelection);
        spSelection.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //spSelection.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        //spSelection.setFitToWidth(true);
        

        pnFoundation.setLeft(spSelection);

        for(int i = 0;i < 10;i++) {
            Button btn = new Button("Testbutton " + (i + 1));
            pnSelection.getChildren().add(btn);
        }

        primaryStage.setScene(new Scene(pnFoundation, 1000, 500));
        primaryStage.show();
    }

    public static void search() {
        System.out.println("suchen...");
        String term = tfSearch.getText();
        int selection;
        if(rbNodes.isSelected()) {
            selection = 0;
        } else if (rbLabels.isSelected()) {
            selection = 1;
        } else {
            selection = 2;
        }

    }
}
