export type GameMode = 'ai-solve' | 'custom-input' | 'interactive';
export type Difficulty = 'easy' | 'intermediate' | 'hard';

export interface SolveStep {
  row: number;
  col: number;
  neighborRow: number;
  neighborCol: number;
  removedValue: number;
  assignedValue: number;
  remainingDomain: number[];
  action: 'REVISE' | 'ASSIGN';
}

export interface SolveResponse {
  solved: boolean;
  arcConsistent: boolean;
  solvedBoard: number[][];
  steps: SolveStep[];
  timeMs: number;
  message: string;
}

export interface ValidateResponse {
  valid: boolean;
  message: string;
}

export interface MoveCheckResponse {
  valid: boolean;
  conflictReason: string;
}
