# Sudoku CSP Solver (Assignment 2 - Part B)

Full-stack Sudoku CSP solver built with **Angular** (frontend) and **Spring Boot** (backend).

## How to Run

### 1. Backend (Spring Boot)
```bash
cd backend
mvn spring-boot:run
```
Runs on `http://localhost:8080`.

### 2. Frontend (Angular)
```bash
cd frontend
npm install
npm start
```
Runs on `http://localhost:4200`.

---


### Member 2 (To Do)
All places for Member 2 are marked with `// TODO: Member 2` and currently have stub returns so the project compiles.

#### 1. Backend: `backend/src/main/java/com/sudoku/service/BacktrackingService.java`
Fill in the 4 methods:
- `isValidPlacement(int[][] board, int row, int col, int num)`: Check if `num` is valid in `row`, `col`, and the 3x3 box.
- `solveWithBacktracking(int[][] board)`: Recursive backtracking solver (also used by AC-3 as fallback on hard puzzles).
- `isSolvable(int[][] board)`: Validate that the initial board has no rule violations and can be solved with backtracking.
- `generatePuzzle(String difficulty)`: Generate a random solvable board using backtracking (replace the preset shuffler for `easy`, `intermediate`, `hard`) and differentiate difficulty levels:
  - **By number of given clues (cells removed):**
    - `easy`: ~36–45 givens (~36–45 empty cells)
    - `intermediate`: ~28–35 givens (~46–53 empty cells)
    - `hard`: ~22–27 givens (~54–59 empty cells)
  - **By CSP solver behavior (AC-3 vs. Backtracking):**
    - `easy`: Solved 100% by AC-3 alone (`0` backtracking calls).
    - `intermediate`: AC-3 reduces domains and fills some cells, then needs light backtracking to finish.
    - `hard`: AC-3 makes little or no progress, requiring heavy backtracking fallback.
  - *(Optional)* Verify uniqueness when removing cells so the generated puzzle has a single valid solution.

#### 2. Frontend: `frontend/src/app/app.ts`
Fill in the 2 stub methods for Mode 2:
- `handleCustomBoardInput(row, col, num)`: Update `board[row][col]` and `initialBoard[row][col]` when the user types/clicks a number in Mode 2.
- `validateAndSolveCustomBoard()`: Call `this.sudokuService.validateBoard(this.board)`. If valid, run `this.solveWithAc3()`; otherwise show an error message in `this.validationMessage` (`this.isBoardValid = false`).

#### 3. Frontend (Optional UI polish):
- `frontend/src/app/components/custom-input-panel/custom-input-panel.ts`
- `frontend/src/app/components/custom-input-panel/custom-input-panel.html`
Add any extra status banners or UI for Mode 2.

#### 4. Report:
- Write the lab report covering:
  - Sample runs and their corresponding Arc consistency trees.
  - Comparison between different initial boards (`easy`, `intermediate`, `hard`) and the time needed to solve them.
  - Data structures and algorithms used, assumptions/details, and any extra work (such as the interactive Play Mode).
