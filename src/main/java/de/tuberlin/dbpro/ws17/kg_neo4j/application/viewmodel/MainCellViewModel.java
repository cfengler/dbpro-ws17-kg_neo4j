package de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel;

import de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel.CellViewModel;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class MainCellViewModel extends CellViewModel {

    public MainCellViewModel(String id, CellTypeViewModel cellType) {
        super( id, cellType);

        Rectangle view = new Rectangle( 50,50);

        view.setStroke(Color.DODGERBLUE);
        view.setFill(Color.DODGERBLUE);

        StackPane pane = new StackPane();

        Ellipse ellipse = new Ellipse();
        ellipse.setRadiusX(100);
        ellipse.setRadiusY(50);
        ellipse.setFill(Color.RED);

        Label lbl = new Label(id);
        lbl.setFont(new Font("Helvetica Neue", 20));
        lbl.setTextFill(Color.WHITE);

        pane.getChildren().add(ellipse);
        pane.getChildren().add(lbl);

        setView(pane);

    }

}