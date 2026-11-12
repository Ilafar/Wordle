package com.wordgame.model;

public enum GameMode {
    WORDLE("Wordle");

    private final String displayName;

    GameMode(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}

