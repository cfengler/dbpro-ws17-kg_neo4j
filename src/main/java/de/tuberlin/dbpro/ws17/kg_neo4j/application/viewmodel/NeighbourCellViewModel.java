package de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel;

import de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel.CellViewModel;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;

public class NeighbourCellViewModel extends CellViewModel {

    public NeighbourCellViewModel( String id, CellTypeViewModel cellType) {
        super( id, cellType);

        double width = 50;
        double height = 50;

        Polygon view = new Polygon( width / 2, 0, width, height, 0, height);

        view.setStroke(Color.RED);
        view.setFill(Color.RED);

        StackPane pane = new StackPane();

        Ellipse ellipse = new Ellipse();
        ellipse.setRadiusX(100);
        ellipse.setRadiusY(50);
        ellipse.setFill(Color.BLUE);

        Label lbl = new Label(id);
        lbl.setFont(new Font("Helvetica Neue", 20));
        lbl.setTextFill(Color.WHITE);

        pane.getChildren().add(ellipse);
        pane.getChildren().add(lbl);

        setView(pane);

    }

}