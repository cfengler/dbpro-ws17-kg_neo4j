package de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel;

import de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel.CellViewModel;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.Company;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class MainCellViewModel extends CellViewModel {

    public MainCellViewModel(Company company) {
        super(company.name, company.dbProId, CellTypeViewModel.MAIN);

        Rectangle view = new Rectangle( 50,50);

        view.setStroke(Color.DODGERBLUE);
        view.setFill(Color.DODGERBLUE);

        StackPane pane = new StackPane();

        Ellipse ellipse = new Ellipse();
        ellipse.setRadiusX(100);
        ellipse.setRadiusY(50);
        ellipse.setFill(Color.GREEN);

        Label lbl = new Label(company.name);
        lbl.setFont(new Font("Helvetica Neue", 15));
        lbl.setTextFill(Color.WHITE);

        pane.getChildren().add(ellipse);
        pane.getChildren().add(lbl);

        setView(pane);

    }

}