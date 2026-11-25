import { ChangeDetectorRef, Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SudokuBoardComponent } from './components/sudoku-board/sudoku-board';
import { CustomInputPanelComponent } from './components/custom-input-panel/custom-input-panel';
import { SudokuService } from './services/sudoku.service';
import { Difficulty, GameMode, SolveStep } from './models/sudoku.models';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, SudokuBoardComponent, CustomInputPanelComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit, OnDestroy {
  mode: GameMode = 'ai-solve';
  difficulty: Difficulty = 'easy';

  board: number[][] = [];
  initialBoard: number[][] = [];
  domains: number[][][] = [];
  conflictCells: boolean[][] = [];

  selectedCell: { row: number; col: number } | null = null;
  activeAc3Cell: { row: number; col: number } | null = null;
  showDomains = true;

  // ac-3 solver trace state
  steps: SolveStep[] = [];
  solvedBoard: number[][] | null = null;
  hasAttemptedSolve = false;
  currentStepIndex = 0;
  isPlaying = false;
  solveTimeMs: number | null = null;
  solverMessage = '';
  private playTimer: ReturnType<typeof setInterval> | null = null;

  // member 2 state for mode 2 and bonus
  validationMessage = '';
  isBoardValid: boolean | null = null;
  mistakes = 0;
  private playSolutionBoard: number[][] | null = null;

  digits = [1, 2, 3, 4, 5, 6, 7, 8, 9];

  constructor(
    private sudokuService: SudokuService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.initEmptyGrid();
    this.loadNewPuzzle();
  }

  ngOnDestroy(): void {
    this.stopPlayback();
  }

  switchMode(newMode: GameMode): void {
    this.stopPlayback();
    this.mode = newMode;
    this.selectedCell = null;
    this.activeAc3Cell = null;
    this.solverMessage = '';
    this.validationMessage = '';
    this.isBoardValid = null;

    if (newMode === 'custom-input') {
      this.clearBoard();
    } else {
      this.loadNewPuzzle();
    }
  }

  setDifficulty(diff: Difficulty): void {
    this.difficulty = diff;
    this.loadNewPuzzle();
  }

  loadNewPuzzle(): void {
    this.stopPlayback();
    this.steps = [];
    this.hasAttemptedSolve = false;
    this.currentStepIndex = 0;
    this.solveTimeMs = null;
    this.solverMessage = '';
    this.activeAc3Cell = null;
    this.mistakes = 0;
    this.resetConflicts();

    this.sudokuService.generatePuzzle(this.difficulty).subscribe({
      next: (grid) => {
        this.initialBoard = this.copyGrid(grid);
        this.board = this.copyGrid(grid);
        this.playSolutionBoard = null;
        this.initDomainsFromBoard(this.board);
        this.cdr.detectChanges();

        // store final solved board when in play mode so we can check user answers
        if (this.mode === 'interactive') {
          this.sudokuService.solveWithAc3(this.initialBoard).subscribe({
            next: (res) => {
              if (res.solved && res.solvedBoard) {
                this.playSolutionBoard = this.copyGrid(res.solvedBoard);
              }
            }
          });
        }
      },
      error: () => {
        this.solverMessage = 'Could not reach backend on localhost:8080.';
        this.cdr.detectChanges();
      }
    });
  }

  solveWithAc3(): void {
    this.stopPlayback();
    this.board = this.copyGrid(this.initialBoard);
    this.initDomainsFromBoard(this.board);
    this.steps = [];
    this.solvedBoard = null;
    this.hasAttemptedSolve = false;
    this.currentStepIndex = 0;
    this.activeAc3Cell = null;
    this.cdr.detectChanges();

    this.sudokuService.solveWithAc3(this.initialBoard).subscribe({
      next: (res) => {
        this.steps = res.steps || [];
        this.solvedBoard = res.solvedBoard ? this.copyGrid(res.solvedBoard) : null;
        this.hasAttemptedSolve = true;
        this.solveTimeMs = res.timeMs;
        this.solverMessage = this.steps.length > 0
          ? `${res.message} Use "Next Step" or "Finish All".`
          : res.message;
        this.cdr.detectChanges();
      },
      error: () => {
        this.solverMessage = 'Error calling /api/sudoku/solve.';
        this.cdr.detectChanges();
      }
    });
  }

  startPlayback(): void {
    if (this.currentStepIndex >= this.steps.length) return;
    this.isPlaying = true;
    this.cdr.detectChanges();
    this.playTimer = setInterval(() => {
      if (this.currentStepIndex < this.steps.length) {
        this.stepForward();
      } else {
        this.stopPlayback();
      }
    }, 80);
  }

  stopPlayback(): void {
    this.isPlaying = false;
    if (this.playTimer) {
      clearInterval(this.playTimer);
      this.playTimer = null;
    }
    this.cdr.detectChanges();
  }

  stepForward(): void {
    if (this.currentStepIndex >= this.steps.length) {
      this.stopPlayback();
      this.activeAc3Cell = null;
      this.cdr.detectChanges();
      return;
    }
    const step = this.steps[this.currentStepIndex];
    this.activeAc3Cell = { row: step.row, col: step.col };
    this.domains[step.row][step.col] = [...step.remainingDomain];
    if (step.action === 'ASSIGN' && step.assignedValue !== 0) {
      this.board[step.row][step.col] = step.assignedValue;
      this.removeFromPeerDomains(step.row, step.col, step.assignedValue);
    }
    this.currentStepIndex++;

    if (this.currentStepIndex >= this.steps.length) {
      this.stopPlayback();
      this.activeAc3Cell = null;
    }
    this.cdr.detectChanges();
  }

  finishInstantly(): void {
    this.stopPlayback();
    while (this.currentStepIndex < this.steps.length) {
      const step = this.steps[this.currentStepIndex];
      this.domains[step.row][step.col] = [...step.remainingDomain];
      if (step.action === 'ASSIGN' && step.assignedValue !== 0) {
        this.board[step.row][step.col] = step.assignedValue;
      }
      this.currentStepIndex++;
    }
    if (this.solvedBoard) {
      this.board = this.copyGrid(this.solvedBoard);
      this.initDomainsFromBoard(this.board);
    }
    this.activeAc3Cell = null;
    this.cdr.detectChanges();
  }

  resetBoard(): void {
    this.stopPlayback();
    this.board = this.copyGrid(this.initialBoard);
    this.initDomainsFromBoard(this.board);
    this.steps = [];
    this.solvedBoard = null;
    this.hasAttemptedSolve = false;
    this.currentStepIndex = 0;
    this.solveTimeMs = null;
    this.solverMessage = '';
    this.activeAc3Cell = null;
    this.cdr.detectChanges();
  }

  onCellSelected(pos: { row: number; col: number }): void {
    this.selectedCell = pos;
  }

  onNumberInput(num: number): void {
    if (!this.selectedCell) return;
    const { row, col } = this.selectedCell;

    if (this.mode === 'custom-input') {
      this.handleCustomBoardInput(row, col, num);
    } else if (this.mode === 'interactive') {
      this.handleInteractiveMove(row, col, num);
    }
  }

  @HostListener('window:keydown', ['$event'])
  handleKeyDown(event: KeyboardEvent): void {
    if (!this.selectedCell) return;
    if (event.key >= '1' && event.key <= '9') {
      this.onNumberInput(Number(event.key));
    } else if (event.key === 'Backspace' || event.key === 'Delete' || event.key === '0') {
      this.onNumberInput(0);
    }
  }

  clearBoard(): void {
    this.initEmptyGrid();
    this.steps = [];
    this.currentStepIndex = 0;
    this.solveTimeMs = null;
    this.validationMessage = '';
    this.isBoardValid = null;
  }

  // TODO: Member 2 - let user enter numbers into custom board (Mode 2)
  handleCustomBoardInput(row: number, col: number, num: number): void {
    // fill cell in board and initialBoard
  }

  // TODO: Member 2 - call sudokuService.validateBoard(this.board) and if valid, solve with AC-3
  validateAndSolveCustomBoard(): void {
    this.validationMessage = 'TODO: Member 2 needs to implement validateAndSolveCustomBoard().';
    this.isBoardValid = false;
  }

  // interactive play mode (bonus)
  handleInteractiveMove(row: number, col: number, num: number): void {
    if (this.initialBoard[row][col] !== 0) {
      return;
    }

    if (num === 0) {
      this.board[row][col] = 0;
      this.conflictCells[row][col] = false;
      this.validationMessage = '';
      this.isBoardValid = null;
      this.initDomainsFromBoard(this.board);
      this.cdr.detectChanges();
      return;
    }

    // check against stored final solution first
    if (this.playSolutionBoard) {
      this.board[row][col] = num;
      if (this.playSolutionBoard[row][col] !== num) {
        this.conflictCells[row][col] = true;
        this.mistakes++;
        this.validationMessage = `Wrong number! ${num} does not match the solution at (${row + 1},${col + 1}).`;
        this.isBoardValid = false;
      } else {
        this.conflictCells[row][col] = false;
        this.initDomainsFromBoard(this.board);
        if (this.isInteractiveWin()) {
          this.validationMessage = 'Puzzle completed with ' + this.mistakes + ' mistake(s)!';
          this.isBoardValid = true;
        } else {
          this.validationMessage = '';
          this.isBoardValid = null;
        }
      }
      this.cdr.detectChanges();
      return;
    }

    this.sudokuService.checkMove(this.board, row, col, num).subscribe({
      next: (res) => {
        this.board[row][col] = num;
        if (!res.valid) {
          this.conflictCells[row][col] = true;
          this.mistakes++;
          this.validationMessage = res.conflictReason || 'Constraint violation!';
          this.isBoardValid = false;
        } else {
          this.conflictCells[row][col] = false;
          this.initDomainsFromBoard(this.board);
          if (this.isInteractiveWin()) {
            this.validationMessage = 'Puzzle completed with ' + this.mistakes + ' mistake(s)!';
            this.isBoardValid = true;
          } else {
            this.validationMessage = '';
            this.isBoardValid = null;
          }
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.validationMessage = 'Could not check move with backend.';
        this.isBoardValid = false;
        this.cdr.detectChanges();
      }
    });
  }

  private isInteractiveWin(): boolean {
    for (let r = 0; r < 9; r++) {
      for (let c = 0; c < 9; c++) {
        if (this.board[r][c] === 0 || this.conflictCells[r][c]) {
          return false;
        }
      }
    }
    return true;
  }

  private initEmptyGrid(): void {
    this.board = Array.from({ length: 9 }, () => Array(9).fill(0));
    this.initialBoard = Array.from({ length: 9 }, () => Array(9).fill(0));
    this.initDomainsFromBoard(this.board);
    this.resetConflicts();
  }

  private resetConflicts(): void {
    this.conflictCells = Array.from({ length: 9 }, () => Array(9).fill(false));
  }

  private initDomainsFromBoard(grid: number[][]): void {
    this.domains = Array.from({ length: 9 }, (_, r) =>
      Array.from({ length: 9 }, (_, c) => {
        if (grid[r][c] !== 0) {
          return [grid[r][c]];
        }
        return this.getValidCandidates(grid, r, c);
      })
    );
  }

  private getValidCandidates(grid: number[][], row: number, col: number): number[] {
    const used = new Set<number>();
    for (let i = 0; i < 9; i++) {
      if (grid[row][i] !== 0) used.add(grid[row][i]);
      if (grid[i][col] !== 0) used.add(grid[i][col]);
    }
    const boxR = Math.floor(row / 3) * 3;
    const boxC = Math.floor(col / 3) * 3;
    for (let r = boxR; r < boxR + 3; r++) {
      for (let c = boxC; c < boxC + 3; c++) {
        if (grid[r][c] !== 0) used.add(grid[r][c]);
      }
    }
    return [1, 2, 3, 4, 5, 6, 7, 8, 9].filter((n) => !used.has(n));
  }

  private removeFromPeerDomains(row: number, col: number, value: number): void {
    const boxR = Math.floor(row / 3) * 3;
    const boxC = Math.floor(col / 3) * 3;
    for (let r = 0; r < 9; r++) {
      for (let c = 0; c < 9; c++) {
        if (r === row && c === col) continue;
        const sameBox = r >= boxR && r < boxR + 3 && c >= boxC && c < boxC + 3;
        if (r === row || c === col || sameBox) {
          this.domains[r][c] = this.domains[r][c].filter((v) => v !== value);
        }
      }
    }
  }

  private copyGrid(grid: number[][]): number[][] {
    return grid.map((row) => [...row]);
  }
}
