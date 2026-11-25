import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameMode } from '../../models/sudoku.models';

// TODO: Member 2 - implement custom board input (Mode 2) and interactive play mode (Bonus)
@Component({
  selector: 'app-custom-input-panel',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './custom-input-panel.html',
  styleUrl: './custom-input-panel.css'
})
export class CustomInputPanelComponent {
  @Input() mode: GameMode = 'custom-input';
  @Input() validationMessage = '';
  @Input() isBoardValid: boolean | null = null;
  @Input() mistakes = 0;

  @Output() clearBoardClicked = new EventEmitter<void>();
  @Output() validateAndSolveClicked = new EventEmitter<void>();
  @Output() newPlayPuzzleClicked = new EventEmitter<void>();

  // TODO: Member 2 - add any helper state or handlers needed for Mode 2 & Bonus
  onClearBoard(): void {
    this.clearBoardClicked.emit();
  }

  onValidateAndSolve(): void {
    this.validateAndSolveClicked.emit();
  }

  onNewPlayPuzzle(): void {
    this.newPlayPuzzleClicked.emit();
  }
}
