package com.wordgame;

import com.wordgame.ds.ChainingHashMap;
import com.wordgame.game.WordGame;
import com.wordgame.game.WordleGame;
import com.wordgame.model.GameMode;
import com.wordgame.service.ProgressTracker;
import com.wordgame.service.WordBank;
import com.wordgame.ui.ConsoleRunner;

import java.util.Scanner;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        WordBank wordBank = new WordBank();
        ProgressTracker tracker = new ProgressTracker();

        ChainingHashMap<GameMode, Supplier<WordGame>> factories = new ChainingHashMap<>();
        factories.put(GameMode.WORDLE, () -> new WordleGame(wordBank.nextWord(GameMode.WORDLE)));

        try (Scanner scanner = new Scanner(System.in)) {
            new ConsoleRunner(wordBank, tracker, factories, scanner).run();
        }
    }
}

