import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sudoku-board',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sudoku-board.html',
  styleUrl: './sudoku-board.css'
})
export class SudokuBoardComponent {
  @Input() board: number[][] = [];
  @Input() initialBoard: number[][] = [];
  @Input() domains: number[][][] = [];
  @Input() showDomains = false;
  @Input() selectedCell: { row: number; col: number } | null = null;
  @Input() activeCell: { row: number; col: number } | null = null;
  @Input() conflictCells: boolean[][] = [];

  @Output() cellSelected = new EventEmitter<{ row: number; col: number }>();

  rows = [0, 1, 2, 3, 4, 5, 6, 7, 8];
  cols = [0, 1, 2, 3, 4, 5, 6, 7, 8];
  digits = [1, 2, 3, 4, 5, 6, 7, 8, 9];

  selectCell(row: number, col: number): void {
    this.cellSelected.emit({ row, col });
  }

  isInitial(row: number, col: number): boolean {
    return this.initialBoard?.[row]?.[col] !== 0;
  }

  isSelected(row: number, col: number): boolean {
    return this.selectedCell?.row === row && this.selectedCell?.col === col;
  }

  isActiveAc3(row: number, col: number): boolean {
    return this.activeCell?.row === row && this.activeCell?.col === col;
  }

  isConflict(row: number, col: number): boolean {
    return !!this.conflictCells?.[row]?.[col];
  }

  // highlight same row, col, or 3x3 box as selected cell
  isPeer(row: number, col: number): boolean {
    if (!this.selectedCell) return false;
    const sr = this.selectedCell.row;
    const sc = this.selectedCell.col;
    if (sr === row && sc === col) return false;
    const sameBox = Math.floor(sr / 3) === Math.floor(row / 3) &&
                    Math.floor(sc / 3) === Math.floor(col / 3);
    return sr === row || sc === col || sameBox;
  }

  isSameNumber(row: number, col: number): boolean {
    if (!this.selectedCell) return false;
    const selectedVal = this.board?.[this.selectedCell.row]?.[this.selectedCell.col];
    if (!selectedVal || selectedVal === 0) return false;
    return this.board?.[row]?.[col] === selectedVal && !this.isSelected(row, col);
  }

  hasCandidate(row: number, col: number, num: number): boolean {
    const cellDomain = this.domains?.[row]?.[col];
    return Array.isArray(cellDomain) && cellDomain.includes(num);
  }
}
