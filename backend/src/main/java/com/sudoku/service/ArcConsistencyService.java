package com.sudoku.service;

import com.sudoku.model.Arc;
import com.sudoku.model.SolveResponse;
import com.sudoku.model.SolveStep;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ArcConsistencyService {

    private final BacktrackingService backtrackingService;

    public ArcConsistencyService(BacktrackingService backtrackingService) {
        this.backtrackingService = backtrackingService;
    }

    public SolveResponse solve(int[][] initialBoard) {
        long startTime = System.nanoTime();
        int[][] board = copyBoard(initialBoard);
        List<SolveStep> steps = new ArrayList<>();

        // init domains for all 81 cells
        @SuppressWarnings("unchecked")
        Set<Integer>[][] domains = new HashSet[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                domains[r][c] = new HashSet<>();
                if (board[r][c] != 0) {
                    domains[r][c].add(board[r][c]);
                } else {
                    for (int v = 1; v <= 9; v++) {
                        domains[r][c].add(v);
                    }
                    // initial domain reduction from pre-filled neighbors
                    for (int[] nb : getNeighbors(r, c)) {
                        int nbVal = board[nb[0]][nb[1]];
                        if (nbVal != 0) {
                            domains[r][c].remove(nbVal);
                        }
                    }
                    if (domains[r][c].isEmpty()) {
                        long timeMs = (System.nanoTime() - startTime) / 1_000_000;
                        return new SolveResponse(false, false, board, steps, timeMs, "Inconsistency found during initial domain reduction.");
                    }
                    if (domains[r][c].size() == 1) {
                        int val = domains[r][c].iterator().next();
                        board[r][c] = val;
                        steps.add(new SolveStep(r, c, r, c, 0, val, new ArrayList<>(domains[r][c]), "ASSIGN"));
                    }
                }
            }
        }

        // build initial queue with all binary arcs
        Queue<Arc> queue = new LinkedList<>();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                for (int[] nb : getNeighbors(r, c)) {
                    queue.add(new Arc(r, c, nb[0], nb[1]));
                }
            }
        }

        boolean consistent = runAc3(domains, queue, board, steps);
        if (!consistent) {
            long timeMs = (System.nanoTime() - startTime) / 1_000_000;
            return new SolveResponse(false, false, board, steps, timeMs, "Inconsistency found during AC-3.");
        }

        boolean fullySolved = isBoardComplete(board);

        // if ac-3 reduced domains but didnt finish (hard puzzles), try backtracking fallback
        if (!fullySolved) {
            boolean btSolved = backtrackingService.solveWithBacktracking(board);
            if (btSolved) {
                long timeMs = (System.nanoTime() - startTime) / 1_000_000;
                return new SolveResponse(true, true, board, steps, timeMs,
                        "Solved using AC-3 + Backtracking fallback.");
            } else {
                long timeMs = (System.nanoTime() - startTime) / 1_000_000;
                String msg = steps.isEmpty()
                        ? "No further AC-3 reductions possible; needs Backtracking fallback (Member 2)."
                        : "AC-3 finished reducing domains, but remaining cells need Backtracking fallback (Member 2).";
                return new SolveResponse(false, true, board, steps, timeMs, msg);
            }
        }

        long timeMs = (System.nanoTime() - startTime) / 1_000_000;
        return new SolveResponse(true, true, board, steps, timeMs, "Solved completely using AC-3.");
    }

    private boolean runAc3(Set<Integer>[][] domains, Queue<Arc> queue, int[][] board, List<SolveStep> steps) {
        while (!queue.isEmpty()) {
            Arc arc = queue.poll();
            int r1 = arc.getR1(), c1 = arc.getC1();
            int r2 = arc.getR2(), c2 = arc.getC2();

            if (revise(domains, r1, c1, r2, c2, steps)) {
                if (domains[r1][c1].isEmpty()) {
                    return false;
                }

                // if only 1 value left in domain, assign it to the board
                if (domains[r1][c1].size() == 1 && board[r1][c1] == 0) {
                    int val = domains[r1][c1].iterator().next();
                    board[r1][c1] = val;
                    steps.add(new SolveStep(r1, c1, r2, c2, 0, val, new ArrayList<>(domains[r1][c1]), "ASSIGN"));
                }

                for (int[] nb : getNeighbors(r1, c1)) {
                    if (nb[0] != r2 || nb[1] != c2) {
                        queue.add(new Arc(nb[0], nb[1], r1, c1));
                    }
                }
            }
        }
        return true;
    }

    private boolean revise(Set<Integer>[][] domains, int r1, int c1, int r2, int c2, List<SolveStep> steps) {
        boolean revised = false;
        Iterator<Integer> it = domains[r1][c1].iterator();
        while (it.hasNext()) {
            int x = it.next();
            // check if there is at least one y in D_j such that x != y
            boolean hasSupport = false;
            for (int y : domains[r2][c2]) {
                if (x != y) {
                    hasSupport = true;
                    break;
                }
            }
            if (!hasSupport) {
                it.remove();
                revised = true;
                steps.add(new SolveStep(r1, c1, r2, c2, x, 0, new ArrayList<>(domains[r1][c1]), "REVISE"));
            }
        }
        return revised;
    }

    // returns all cells sharing row, col, or 3x3 box with (r, c)
    private List<int[]> getNeighbors(int r, int c) {
        List<int[]> neighbors = new ArrayList<>();
        boolean[][] seen = new boolean[9][9];
        seen[r][c] = true;

        for (int i = 0; i < 9; i++) {
            if (!seen[r][i]) {
                neighbors.add(new int[]{r, i});
                seen[r][i] = true;
            }
            if (!seen[i][c]) {
                neighbors.add(new int[]{i, c});
                seen[i][c] = true;
            }
        }

        int boxR = (r / 3) * 3;
        int boxC = (c / 3) * 3;
        for (int i = boxR; i < boxR + 3; i++) {
            for (int j = boxC; j < boxC + 3; j++) {
                if (!seen[i][j]) {
                    neighbors.add(new int[]{i, j});
                    seen[i][j] = true;
                }
            }
        }
        return neighbors;
    }

    private boolean isBoardComplete(int[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) return false;
            }
        }
        return true;
    }

    private int[][] copyBoard(int[][] src) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(src[i], 0, copy[i], 0, 9);
        }
        return copy;
    }
}
