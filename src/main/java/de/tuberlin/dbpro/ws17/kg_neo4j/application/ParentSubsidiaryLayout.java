package de.tuberlin.dbpro.ws17.kg_neo4j.application;

import de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel.*;

import java.util.List;
import java.util.Random;

public class ParentSubsidiaryLayout extends Layout {

    GraphViewModel graph;

    Random rnd = new Random();

    public ParentSubsidiaryLayout(GraphViewModel graph) {

        this.graph = graph;

    }

    public void execute() {
        List<CellViewModel> cells = graph.getModel().getAllCells();

        double length = 200.0;
        int indexSubsidiary = 1;

        for (CellViewModel cell : cells) {
            double x;
            double y;
            switch(cell.getCellType()) {
                case MAIN:
                    x = 250;
                    y = 250;
                    break;
                case PARENT:
                    x = 250;
                    y = 250 - length;
                    break;
                case SUBSIDIARY:
                    double rad = Math.toRadians(((double)indexSubsidiary / (double)cells.size()) * 360.0);
                    x = 250 + Math.sin(rad) * length;
                    y = 250 + Math.cos(rad) * length;
                    indexSubsidiary++;
                    break;
                default:
                    x = 0.0;
                    y = 0.0;
                    break;
            }
            cell.relocate(x, y);
        }
    }

}