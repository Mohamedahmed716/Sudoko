package com.sudoku.model;

import java.util.List;

public class SolveResponse {
    private boolean solved;
    private boolean arcConsistent;
    private int[][] solvedBoard;
    private List<SolveStep> steps;
    private long timeMs;
    private String message;

    public SolveResponse() {}

    public SolveResponse(boolean solved, boolean arcConsistent, int[][] solvedBoard,
                         List<SolveStep> steps, long timeMs, String message) {
        this.solved = solved;
        this.arcConsistent = arcConsistent;
        this.solvedBoard = solvedBoard;
        this.steps = steps;
        this.timeMs = timeMs;
        this.message = message;
    }

    public boolean isSolved() { return solved; }
    public void setSolved(boolean solved) { this.solved = solved; }
    public boolean isArcConsistent() { return arcConsistent; }
    public void setArcConsistent(boolean arcConsistent) { this.arcConsistent = arcConsistent; }
    public int[][] getSolvedBoard() { return solvedBoard; }
    public void setSolvedBoard(int[][] solvedBoard) { this.solvedBoard = solvedBoard; }
    public List<SolveStep> getSteps() { return steps; }
    public void setSteps(List<SolveStep> steps) { this.steps = steps; }
    public long getTimeMs() { return timeMs; }
    public void setTimeMs(long timeMs) { this.timeMs = timeMs; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
