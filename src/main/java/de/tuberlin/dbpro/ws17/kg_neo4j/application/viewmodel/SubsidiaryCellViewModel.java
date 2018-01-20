package de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel;

import de.tuberlin.dbpro.ws17.kg_neo4j.application.view.UserApplicationView;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.Company;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class SubsidiaryCellViewModel extends CellViewModel {

    public SubsidiaryCellViewModel(Company company) {
        super(company.name, company.dbProId, CellTypeViewModel.SUBSIDIARY);

        Rectangle view = new Rectangle( 50,50);

        view.setStroke(Color.DODGERBLUE);
        view.setFill(Color.DODGERBLUE);

        StackPane pane = new StackPane();

        Ellipse ellipse = new Ellipse();
        ellipse.setRadiusX(100);
        ellipse.setRadiusY(50);
        ellipse.setFill(Color.BLUE);
        ellipse.setOnMousePressed(event -> {
            UserApplicationView.setTimestamp();
        });
        ellipse.setOnMouseReleased(event -> {
            if(UserApplicationView.isClick()) {
                UserApplicationView.displayCompany(company);
            }
        });

        Label lbl = new Label(company.name);
        lbl.setFont(new Font("Helvetica Neue", 20));
        lbl.setTextFill(Color.WHITE);
        lbl.addEventHandler(MouseEvent.ANY, e -> ellipse.fireEvent(e));

        pane.getChildren().add(ellipse);
        pane.getChildren().add(lbl);

        setView(pane);

    }

}
