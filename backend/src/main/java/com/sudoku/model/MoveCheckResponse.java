package com.sudoku.model;

public class MoveCheckResponse {
    private boolean valid;
    private String conflictReason;

    public MoveCheckResponse() {}

    public MoveCheckResponse(boolean valid, String conflictReason) {
        this.valid = valid;
        this.conflictReason = conflictReason;
    }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public String getConflictReason() { return conflictReason; }
    public void setConflictReason(String conflictReason) { this.conflictReason = conflictReason; }
}
