package com.sudoku;

import com.sudoku.model.SolveResponse;
import com.sudoku.service.ArcConsistencyService;
import com.sudoku.service.BacktrackingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArcConsistencyTest {

    @Test
    void easyBoardsSolveWithAc3() {
        BacktrackingService bt = new BacktrackingService();
        ArcConsistencyService ac3 = new ArcConsistencyService(bt);

        int[][] board1 = bt.generatePuzzle("easy");
        SolveResponse res1 = ac3.solve(board1);
        System.out.println("Board 1 solved=" + res1.isSolved() + " steps=" + res1.getSteps().size() + " msg=" + res1.getMessage());

        int[][] board2 = bt.generatePuzzle("easy");
        SolveResponse res2 = ac3.solve(board2);
        System.out.println("Easy 2 solved=" + res2.isSolved() + " steps=" + res2.getSteps().size() + " timeMs=" + res2.getTimeMs() + " msg=" + res2.getMessage());

        int[][] med = bt.generatePuzzle("intermediate");
        SolveResponse resMed = ac3.solve(med);
        System.out.println("Medium solved=" + resMed.isSolved() + " steps=" + resMed.getSteps().size() + " timeMs=" + resMed.getTimeMs() + " msg=" + resMed.getMessage());

        int[][] hard = bt.generatePuzzle("hard");
        SolveResponse resHard = ac3.solve(hard);
        System.out.println("Hard solved=" + resHard.isSolved() + " steps=" + resHard.getSteps().size() + " timeMs=" + resHard.getTimeMs() + " msg=" + resHard.getMessage());

        assertTrue(res1.getSteps().size() > 0);
        assertTrue(res2.getSteps().size() > 0);
    }
}
