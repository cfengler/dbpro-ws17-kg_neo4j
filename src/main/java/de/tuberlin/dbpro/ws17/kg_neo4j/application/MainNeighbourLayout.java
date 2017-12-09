package de.tuberlin.dbpro.ws17.kg_neo4j.application;

import de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel.*;

import java.util.List;
import java.util.Random;

public class MainNeighbourLayout extends Layout {

    GraphViewModel graph;

    Random rnd = new Random();

    public MainNeighbourLayout(GraphViewModel graph) {

        this.graph = graph;

    }

    public void execute() {

        List<CellViewModel> cells = graph.getModel().getAllCells();

        int numberNeighbours = 0;
        double length = 200.0;

        for (CellViewModel cell : cells) {
            if(cell.getCellType() == CellTypeViewModel.NEIGHBOUR) {
                numberNeighbours++;
            }
        }

        int indexNeighbour = 0;

        for (CellViewModel cell : cells) {

            double x;
            double y;

            switch(cell.getCellType()) {
                case MAIN:
                    x = 250;
                    y = 250;
                    break;
                case NEIGHBOUR:
                    double rad = Math.toRadians(((double)indexNeighbour / (double)numberNeighbours) * 360.0);
                    x = 250 + Math.sin(rad) * length;
                    y = 250 + Math.cos(rad) * length;
                    indexNeighbour++;
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