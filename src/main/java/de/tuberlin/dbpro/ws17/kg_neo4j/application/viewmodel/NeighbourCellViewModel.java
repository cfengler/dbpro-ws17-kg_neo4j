package de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel;

import de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel.CellViewModel;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class NeighbourCellViewModel extends CellViewModel {

    public NeighbourCellViewModel( String id) {
        super( id);

        double width = 50;
        double height = 50;

        Polygon view = new Polygon( width / 2, 0, width, height, 0, height);

        view.setStroke(Color.RED);
        view.setFill(Color.RED);

        setView( view);

    }

}