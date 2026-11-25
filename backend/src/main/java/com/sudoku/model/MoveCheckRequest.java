package com.sudoku.model;

public class MoveCheckRequest {
    private int[][] board;
    private int row;
    private int col;
    private int value;

    public MoveCheckRequest() {}

    public int[][] getBoard() { return board; }
    public void setBoard(int[][] board) { this.board = board; }
    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }
    public int getCol() { return col; }
    public void setCol(int col) { this.col = col; }
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
}
