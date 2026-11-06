package com.wordgame.game;

import com.wordgame.model.GameMode;
import com.wordgame.model.MoveOutcome;

public interface WordGame {
    GameMode mode();

    String instructions();

    MoveOutcome submitGuess(String rawInput);

    boolean canUndo();

    MoveOutcome undo();

    String nextHint();

    boolean isFinished();

    boolean isWon();

    String secretWord();

    int attemptsUsed();

    int maxAttempts();

    String renderBoard();
}

