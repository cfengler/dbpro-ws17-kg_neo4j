package de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel;

import java.util.ArrayList;
import java.util.List;

import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbProId;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class CellViewModel extends Pane {

    private String name;

    private DbProId dbproid;

    private CellTypeViewModel cellType;

    private List<CellViewModel> children = new ArrayList<>();
    private List<CellViewModel> parents = new ArrayList<>();

    Node view;

    public CellViewModel(String name, DbProId dbproid, CellTypeViewModel cellType) {
        this.name = name;
        this.dbproid = dbproid;
        this.cellType = cellType;
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

    public CellTypeViewModel getCellType() {
        return cellType;
    }

    public void setView(Node view) {

        this.view = view;
        getChildren().add(view);

    }

    public Node getView() {
        return this.view;
    }

    public String getName() {
        return name;
    }

    public DbProId getDbproid() {
        return dbproid;
    }

    public void setDbproid(DbProId dbproid) {
        this.dbproid = dbproid;
    }
}