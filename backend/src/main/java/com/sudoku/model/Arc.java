package com.sudoku.model;

// represents a binary constraint arc between two cells (row1,col1) -> (row2,col2)
public class Arc {
    private int r1;
    private int c1;
    private int r2;
    private int c2;

    public Arc() {}

    public Arc(int r1, int c1, int r2, int c2) {
        this.r1 = r1;
        this.c1 = c1;
        this.r2 = r2;
        this.c2 = c2;
    }

    public int getR1() { return r1; }
    public void setR1(int r1) { this.r1 = r1; }
    public int getC1() { return c1; }
    public void setC1(int c1) { this.c1 = c1; }
    public int getR2() { return r2; }
    public void setR2(int r2) { this.r2 = r2; }
    public int getC2() { return c2; }
    public void setC2(int c2) { this.c2 = c2; }
}
