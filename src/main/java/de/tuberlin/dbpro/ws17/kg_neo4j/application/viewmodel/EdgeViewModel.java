package de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel;

import javafx.scene.Group;
import javafx.scene.shape.Line;

public class EdgeViewModel extends Group {

    protected CellViewModel source;
    protected CellViewModel target;

    Line line;

    public EdgeViewModel(CellViewModel source, CellViewModel target) {

        this.source = source;
        this.target = target;

        source.addCellChild(target);
        target.addCellParent(source);

        line = new Line();

        line.startXProperty().bind( source.layoutXProperty().add(source.getBoundsInParent().getWidth() / 2.0));
        line.startYProperty().bind( source.layoutYProperty().add(source.getBoundsInParent().getHeight() / 2.0));

        line.endXProperty().bind( target.layoutXProperty().add( target.getBoundsInParent().getWidth() / 2.0));
        line.endYProperty().bind( target.layoutYProperty().add( target.getBoundsInParent().getHeight() / 2.0));

        getChildren().add( line);

    }

    public CellViewModel getSource() {
        return source;
    }

    public CellViewModel getTarget() {
        return target;
    }

}