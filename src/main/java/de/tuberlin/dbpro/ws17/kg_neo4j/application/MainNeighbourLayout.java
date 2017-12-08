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

        for (CellViewModel cell : cells) {

            double x = rnd.nextDouble() * 500;
            double y = rnd.nextDouble() * 500;

            cell.relocate(x, y);

        }

    }

}