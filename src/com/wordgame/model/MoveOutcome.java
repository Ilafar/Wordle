package com.wordgame.model;

public record MoveOutcome(boolean accepted, String message) {
    public static MoveOutcome accepted(String message) {
        return new MoveOutcome(true, message);
    }

    public static MoveOutcome rejected(String message) {
        return new MoveOutcome(false, message);
    }
}

