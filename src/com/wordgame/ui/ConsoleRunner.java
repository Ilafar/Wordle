package com.wordgame.ui;

import com.wordgame.ds.ChainingHashMap;
import com.wordgame.game.WordGame;
import com.wordgame.model.GameMode;
import com.wordgame.model.MoveOutcome;
import com.wordgame.service.ProgressTracker;
import com.wordgame.service.WordBank;

import java.util.Scanner;
import java.util.function.Supplier;

public class ConsoleRunner {
    private final WordBank wordBank;
    private final ProgressTracker tracker;
    private final ChainingHashMap<GameMode, Supplier<WordGame>> gameFactories;
    private final Scanner scanner;

    public ConsoleRunner(
            WordBank wordBank,
            ProgressTracker tracker,
            ChainingHashMap<GameMode, Supplier<WordGame>> gameFactories,
            Scanner scanner
    ) {
        this.wordBank = wordBank;
        this.tracker = tracker;
        this.gameFactories = gameFactories;
        this.scanner = scanner;
    }

    public void run() {
        System.out.println("=== WORDLE CONSOLE EDITION ===");
        while (true) {
            printMenu();
            if (!scanner.hasNextLine()) {
                return;
            }
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> playRound(GameMode.WORDLE);
                case "2" -> System.out.println(tracker.formatStats(wordBank));
                case "0", "q", "quit", "exit" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Choose 0-2.");
            }
        }
    }

    private void printMenu() {
        System.out.println("""
                
                1. Play Wordle
                2. Stats & Top-K Leaderboard
                0. Exit""");
        System.out.print("> ");
    }

    private void playRound(GameMode mode) {
        Supplier<WordGame> factory = gameFactories.get(mode);
        if (factory == null) {
            System.out.println("Mode not configured: " + mode);
            return;
        }

        WordGame game = factory.get();
        long startNs = System.nanoTime();
        System.out.println("\nStarting " + game.mode().displayName() + "!");
        System.out.println(game.instructions());
        System.out.println("Commands: :hint, :undo, :quit");

        while (!game.isFinished()) {
            System.out.print(game.renderBoard());
            System.out.print("Guess > ");
            if (!scanner.hasNextLine()) {
                return;
            }
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase(":quit")) {
                System.out.println("Round abandoned. The word was " + game.secretWord() + ".");
                return;
            }
            if (input.equalsIgnoreCase(":hint")) {
                System.out.println(game.nextHint());
                continue;
            }
            if (input.equalsIgnoreCase(":undo")) {
                MoveOutcome undoOutcome = game.undo();
                System.out.println(undoOutcome.message());
                continue;
            }

            MoveOutcome outcome = game.submitGuess(input);
            System.out.println(outcome.message());
        }

        System.out.print(game.renderBoard());
        long durationMs = (System.nanoTime() - startNs) / 1_000_000;
        tracker.recordRound(game.mode(), game.secretWord(), game.isWon(), game.attemptsUsed(), durationMs);
    }
}

