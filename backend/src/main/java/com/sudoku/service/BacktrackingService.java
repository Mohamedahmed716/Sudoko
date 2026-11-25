package com.sudoku.service;

import org.springframework.stereotype.Service;

@Service
public class BacktrackingService {

    // TODO: Member 2 - check if placing num at (row, col) is valid (row, col, 3x3 box)
    public boolean isValidPlacement(int[][] board, int row, int col, int num) {
        // return false for now so project compiles
        return false;
    }

    // TODO: Member 2 - implement recursive backtracking to solve the board in-place
    public boolean solveWithBacktracking(int[][] board) {
        // return false until Member 2 implements backtracking
        return false;
    }

    // TODO: Member 2 - check that current board has no conflicts and is solvable using backtracking
    public boolean isSolvable(int[][] board) {
        return false;
    }

    private int sampleIndex = 0;

    // TODO: Member 2 - generate a random solvable puzzle for easy / intermediate / hard
    // hint: fill random cells or diagonal 3x3 boxes, solve with backtracking, then remove cells
    public int[][] generatePuzzle(String difficulty) {
        // TODO: Member 2 - replace these sample boards with real backtracking random puzzle generator
        int[][][] easyBoards = {
            {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
            },
            {
                {0, 0, 0, 2, 6, 0, 7, 0, 1},
                {6, 8, 0, 0, 7, 0, 0, 9, 0},
                {1, 9, 0, 0, 0, 4, 5, 0, 0},
                {8, 2, 0, 1, 0, 0, 0, 4, 0},
                {0, 0, 4, 6, 0, 2, 9, 0, 0},
                {0, 5, 0, 0, 0, 3, 0, 2, 8},
                {0, 0, 9, 3, 0, 0, 0, 7, 4},
                {0, 4, 0, 0, 5, 0, 0, 3, 6},
                {7, 0, 3, 0, 1, 8, 0, 0, 0}
            }
        };

        int[][][] mediumBoards = {
            {
                {0, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 0, 0, 0, 7, 9}
            },
            {
                {0, 0, 0, 2, 6, 0, 7, 0, 0},
                {6, 8, 0, 0, 7, 0, 0, 9, 0},
                {1, 9, 0, 0, 0, 4, 5, 0, 0},
                {8, 2, 0, 1, 0, 0, 0, 4, 0},
                {0, 0, 4, 6, 0, 2, 9, 0, 0},
                {0, 5, 0, 0, 0, 3, 0, 2, 8},
                {0, 0, 9, 3, 0, 0, 0, 7, 4},
                {0, 4, 0, 0, 5, 0, 0, 3, 6},
                {7, 0, 3, 0, 1, 8, 0, 0, 0}
            }
        };

        int[][][] hardBoards = {
            {
                {0, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 0, 0, 0, 7, 9}
            },
            {
                {0, 0, 0, 2, 6, 0, 7, 0, 0},
                {6, 8, 0, 0, 7, 0, 0, 9, 0},
                {1, 9, 0, 0, 0, 4, 5, 0, 0},
                {8, 2, 0, 1, 0, 0, 0, 4, 0},
                {0, 0, 4, 6, 0, 2, 9, 0, 0},
                {0, 5, 0, 0, 0, 3, 0, 2, 8},
                {0, 0, 9, 3, 0, 0, 0, 7, 4},
                {0, 4, 0, 0, 5, 0, 0, 3, 6},
                {7, 0, 3, 0, 1, 8, 0, 0, 0}
            }
        };

        sampleIndex++;
        int[][] base;
        if ("hard".equalsIgnoreCase(difficulty)) {
            base = hardBoards[sampleIndex % hardBoards.length];
        } else if ("intermediate".equalsIgnoreCase(difficulty)) {
            base = mediumBoards[sampleIndex % mediumBoards.length];
        } else {
            base = easyBoards[sampleIndex % easyBoards.length];
        }
        return randomizeBoard(base);
    }

    // permute digits and row/col bands so New Puzzle gives a fresh board every click
    private int[][] randomizeBoard(int[][] base) {
        java.util.List<Integer> digits = new java.util.ArrayList<>();
        for (int i = 1; i <= 9; i++) digits.add(i);
        java.util.Collections.shuffle(digits);

        int[] rowMap = shuffledIndices();
        int[] colMap = shuffledIndices();

        int[][] out = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = base[rowMap[r]][colMap[c]];
                out[r][c] = val == 0 ? 0 : digits.get(val - 1);
            }
        }
        return out;
    }

    private int[] shuffledIndices() {
        java.util.List<Integer> bands = new java.util.ArrayList<>(java.util.List.of(0, 1, 2));
        java.util.Collections.shuffle(bands);
        int[] res = new int[9];
        int idx = 0;
        for (int b : bands) {
            java.util.List<Integer> inner = new java.util.ArrayList<>(java.util.List.of(0, 1, 2));
            java.util.Collections.shuffle(inner);
            for (int i : inner) {
                res[idx++] = b * 3 + i;
            }
        }
        return res;
    }

    // check if a user move violates any row, col, or 3x3 box constraint
    public String checkMoveConflict(int[][] board, int row, int col, int value) {
        if (value == 0) {
            return null;
        }
        for (int c = 0; c < 9; c++) {
            if (c != col && board[row][c] == value) {
                return "Row " + (row + 1) + " already has " + value + ".";
            }
        }
        for (int r = 0; r < 9; r++) {
            if (r != row && board[r][col] == value) {
                return "Column " + (col + 1) + " already has " + value + ".";
            }
        }
        int boxR = (row / 3) * 3;
        int boxC = (col / 3) * 3;
        for (int r = boxR; r < boxR + 3; r++) {
            for (int c = boxC; c < boxC + 3; c++) {
                if ((r != row || c != col) && board[r][c] == value) {
                    return "3x3 box already contains " + value + ".";
                }
            }
        }
        return null;
    }
}
