package de.tuberlin.dbpro.ws17.kg_neo4j.application;

import de.tuberlin.dbpro.ws17.kg_neo4j.application.viewmodel.*;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.Company;
import de.tuberlin.dbpro.ws17.kg_neo4j.domain.DbProId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {

    CellViewModel graphParent;

    List<CellViewModel> allCells;
    List<CellViewModel> addedCells;
    List<CellViewModel> removedCells;

    List<EdgeViewModel> allEdges;
    List<EdgeViewModel> addedEdges;
    List<EdgeViewModel> removedEdges;

    Map<String,CellViewModel> cellMap; // <id,cell>

    public Model() {

        graphParent = new CellViewModel( "_ROOT_", new DbProId(), CellTypeViewModel.MAIN);

        // clear model, create lists
        clear();
    }

    public void clear() {

        allCells = new ArrayList<>();
        addedCells = new ArrayList<>();
        removedCells = new ArrayList<>();

        allEdges = new ArrayList<>();
        addedEdges = new ArrayList<>();
        removedEdges = new ArrayList<>();

        cellMap = new HashMap<>(); // <id,cell>

    }

    public void clearAddedLists() {
        addedCells.clear();
        addedEdges.clear();
    }

    public List<CellViewModel> getAddedCells() {
        return addedCells;
    }

    public List<CellViewModel> getRemovedCells() {
        return removedCells;
    }

    public List<CellViewModel> getAllCells() {
        return allCells;
    }

    public List<EdgeViewModel> getAddedEdges() {
        return addedEdges;
    }

    public List<EdgeViewModel> getRemovedEdges() {
        return removedEdges;
    }

    public List<EdgeViewModel> getAllEdges() {
        return allEdges;
    }

    public void addCell(Company company, CellTypeViewModel type) {

        switch (type) {
            case MAIN:
                MainCellViewModel mainCell = new MainCellViewModel(company);
                addCell(mainCell);
                break;
            case NEIGHBOUR:
                NeighbourCellViewModel neighbourCell = new NeighbourCellViewModel(company);
                addCell(neighbourCell);
                break;
            case PARENT:
                ParentCellViewModel parentCell = new ParentCellViewModel(company);
                addCell(parentCell);
                break;
            case SUBSIDIARY:
                SubsidiaryCellViewModel subsidiaryCell = new SubsidiaryCellViewModel(company);
                addCell(subsidiaryCell);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported type: " + type);
        }
    }

    private void addCell( CellViewModel cell) {

        addedCells.add(cell);

        cellMap.put( cell.getName(), cell);

    }

    public void addEdge( String sourceId, String targetId) {

        CellViewModel sourceCell = cellMap.get( sourceId);
        CellViewModel targetCell = cellMap.get( targetId);

        EdgeViewModel edge = new EdgeViewModel( sourceCell, targetCell);

        addedEdges.add( edge);

    }

    /**
     * Attach all cells which don't have a parent to graphParent
     * @param cellList
     */
    public void attachOrphansToGraphParent( List<CellViewModel> cellList) {

        for( CellViewModel cell: cellList) {
            if( cell.getCellParents().size() == 0) {
                graphParent.addCellChild( cell);
            }
        }

    }

    /**
     * Remove the graphParent reference if it is set
     * @param cellList
     */
    public void disconnectFromGraphParent( List<CellViewModel> cellList) {

        for( CellViewModel cell: cellList) {
            graphParent.removeCellChild( cell);
        }
    }

    public void merge() {

        // cells
        allCells.addAll( addedCells);
        allCells.removeAll( removedCells);

        addedCells.clear();
        removedCells.clear();

        // edges
        allEdges.addAll( addedEdges);
        allEdges.removeAll( removedEdges);

        addedEdges.clear();
        removedEdges.clear();

    }
}