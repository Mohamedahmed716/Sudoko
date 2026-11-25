import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MoveCheckResponse, SolveResponse, ValidateResponse } from '../models/sudoku.models';

@Injectable({
  providedIn: 'root'
})
export class SudokuService {
  private apiUrl = 'http://localhost:8080/api/sudoku';

  constructor(private http: HttpClient) {}

  solveWithAc3(board: number[][]): Observable<SolveResponse> {
    return this.http.post<SolveResponse>(`${this.apiUrl}/solve`, { board });
  }

  generatePuzzle(difficulty: string): Observable<number[][]> {
    return this.http.get<number[][]>(`${this.apiUrl}/generate?difficulty=${difficulty}&t=${Date.now()}`);
  }

  validateBoard(board: number[][]): Observable<ValidateResponse> {
    return this.http.post<ValidateResponse>(`${this.apiUrl}/validate`, { board });
  }

  checkMove(board: number[][], row: number, col: number, value: number): Observable<MoveCheckResponse> {
    return this.http.post<MoveCheckResponse>(`${this.apiUrl}/check-move`, { board, row, col, value });
  }
}
