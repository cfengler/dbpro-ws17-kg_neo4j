package de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel;

import de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel.CellViewModel;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainCellViewModel extends CellViewModel {

    public MainCellViewModel(String id) {
        super( id);

        Rectangle view = new Rectangle( 50,50);

        view.setStroke(Color.DODGERBLUE);
        view.setFill(Color.DODGERBLUE);

        setView( view);

    }

}