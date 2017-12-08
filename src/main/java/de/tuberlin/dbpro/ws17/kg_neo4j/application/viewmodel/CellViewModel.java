package de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class CellViewModel extends Pane {

    String cellId;

    List<CellViewModel> children = new ArrayList<>();
    List<CellViewModel> parents = new ArrayList<>();

    Node view;

    public CellViewModel(String cellId) {
        this.cellId = cellId;
    }

    public void addCellChild(CellViewModel cell) {
        children.add(cell);
    }

    public List<CellViewModel> getCellChildren() {
        return children;
    }

    public void addCellParent(CellViewModel cell) {
        parents.add(cell);
    }

    public List<CellViewModel> getCellParents() {
        return parents;
    }

    public void removeCellChild(CellViewModel cell) {
        children.remove(cell);
    }

    public void setView(Node view) {

        this.view = view;
        getChildren().add(view);

    }

    public Node getView() {
        return this.view;
    }

    public String getCellId() {
        return cellId;
    }
}