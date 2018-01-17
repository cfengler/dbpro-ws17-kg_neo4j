package de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel;

import de.tuberlin.dbpro.ws17.kg_neo4j.application.view.UserApplicationView;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.Company;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class ParentCellViewModel extends CellViewModel {

    public ParentCellViewModel(Company company) {
        super(company.name, company.dbProId, CellTypeViewModel.PARENT);

        Rectangle view = new Rectangle( 50,50);

        view.setStroke(Color.RED);
        view.setFill(Color.RED);

        StackPane pane = new StackPane();

        Ellipse ellipse = new Ellipse();
        ellipse.setRadiusX(100);
        ellipse.setRadiusY(50);
        ellipse.setFill(Color.RED);
        ellipse.setOnMouseClicked(event -> {
            UserApplicationView.displayCompany(company);
        });

        Label lbl = new Label(company.name);
        lbl.setFont(new Font("Helvetica Neue", 15));
        lbl.setTextFill(Color.WHITE);
        lbl.applyCss();
        lbl.layout();
        lbl.addEventHandler(MouseEvent.ANY, e -> ellipse.fireEvent(e));

        pane.getChildren().add(ellipse);
        pane.getChildren().add(lbl);

        setView(pane);
    }

}
