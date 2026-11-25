package com.sudoku.controller;

import com.sudoku.model.*;
import com.sudoku.service.ArcConsistencyService;
import com.sudoku.service.BacktrackingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sudoku")
public class SudokuController {

    private final ArcConsistencyService arcConsistencyService;
    private final BacktrackingService backtrackingService;

    public SudokuController(ArcConsistencyService arcConsistencyService,
                            BacktrackingService backtrackingService) {
        this.arcConsistencyService = arcConsistencyService;
        this.backtrackingService = backtrackingService;
    }

    @PostMapping("/solve")
    public SolveResponse solveWithAc3(@RequestBody BoardRequest request) {
        return arcConsistencyService.solve(request.getBoard());
    }

    @GetMapping("/generate")
    public int[][] generatePuzzle(@RequestParam(defaultValue = "easy") String difficulty) {
        return backtrackingService.generatePuzzle(difficulty);
    }

    @PostMapping("/validate")
    public ValidateResponse validateBoard(@RequestBody BoardRequest request) {
        boolean ok = backtrackingService.isSolvable(request.getBoard());
        String msg = ok ? "Board is valid and solvable." : "Board is invalid or unsolvable.";
        return new ValidateResponse(ok, msg);
    }

    @PostMapping("/check-move")
    public MoveCheckResponse checkMove(@RequestBody MoveCheckRequest request) {
        String conflict = backtrackingService.checkMoveConflict(
                request.getBoard(), request.getRow(), request.getCol(), request.getValue());
        if (conflict == null) {
            return new MoveCheckResponse(true, "");
        }
        return new MoveCheckResponse(false, conflict);
    }
}
