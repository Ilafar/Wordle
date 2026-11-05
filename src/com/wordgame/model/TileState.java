package com.wordgame.model;

public enum TileState {
    EXACT('+'),
    PRESENT('?'),
    ABSENT('.');

    private final char symbol;

    TileState(char symbol) {
        this.symbol = symbol;
    }

    public char symbol() {
        return symbol;
    }
}

