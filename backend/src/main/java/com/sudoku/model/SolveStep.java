package com.sudoku.model;

import java.util.List;

// one step in the arc consistency trace so we can show it in the ui
public class SolveStep {
    private int row;
    private int col;
    private int neighborRow;
    private int neighborCol;
    private int removedValue;
    private int assignedValue; // 0 if not assigned yet
    private List<Integer> remainingDomain;
    private String action; // "REVISE" or "ASSIGN"

    public SolveStep() {}

    public SolveStep(int row, int col, int neighborRow, int neighborCol,
                     int removedValue, int assignedValue,
                     List<Integer> remainingDomain, String action) {
        this.row = row;
        this.col = col;
        this.neighborRow = neighborRow;
        this.neighborCol = neighborCol;
        this.removedValue = removedValue;
        this.assignedValue = assignedValue;
        this.remainingDomain = remainingDomain;
        this.action = action;
    }

    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }
    public int getCol() { return col; }
    public void setCol(int col) { this.col = col; }
    public int getNeighborRow() { return neighborRow; }
    public void setNeighborRow(int neighborRow) { this.neighborRow = neighborRow; }
    public int getNeighborCol() { return neighborCol; }
    public void setNeighborCol(int neighborCol) { this.neighborCol = neighborCol; }
    public int getRemovedValue() { return removedValue; }
    public void setRemovedValue(int removedValue) { this.removedValue = removedValue; }
    public int getAssignedValue() { return assignedValue; }
    public void setAssignedValue(int assignedValue) { this.assignedValue = assignedValue; }
    public List<Integer> getRemainingDomain() { return remainingDomain; }
    public void setRemainingDomain(List<Integer> remainingDomain) { this.remainingDomain = remainingDomain; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}
