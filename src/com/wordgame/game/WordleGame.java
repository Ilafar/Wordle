package com.wordgame.game;

import com.wordgame.ds.ArrayQueue;
import com.wordgame.ds.ChainingHashMap;
import com.wordgame.ds.DynamicArray;
import com.wordgame.ds.LinkedStack;
import com.wordgame.model.GameMode;
import com.wordgame.model.MoveOutcome;
import com.wordgame.model.TileState;

import java.util.Locale;

public class WordleGame implements WordGame {
    private static final int WORD_LENGTH = 5;
    private static final int MAX_ATTEMPTS = 6;

    private record GuessRow(String word, TileState[] states) {}

    private final String secret;
    private final DynamicArray<GuessRow> rows = new DynamicArray<>(MAX_ATTEMPTS);
    private final LinkedStack<GuessRow> undoStack = new LinkedStack<>();
    private final ArrayQueue<String> hintQueue = new ArrayQueue<>();
    private boolean won;

    public WordleGame(String secretWord) {
        if (secretWord == null || secretWord.length() != WORD_LENGTH) {
            throw new IllegalArgumentException("Wordle secret must be " + WORD_LENGTH + " letters");
        }
        this.secret = secretWord.toUpperCase(Locale.ROOT);
        seedHints();
    }

    @Override
    public GameMode mode() {
        return GameMode.WORDLE;
    }

    @Override
    public String instructions() {
        return "Guess the 5-letter word in " + MAX_ATTEMPTS + " tries. "
                + "Legend: [+] exact spot, [?] wrong spot, [.] not in word.";
    }

    @Override
    public MoveOutcome submitGuess(String rawInput) {
        if (isFinished()) {
            return MoveOutcome.rejected("Round is already finished.");
        }
        String guess = rawInput == null ? "" : rawInput.trim().toUpperCase(Locale.ROOT);
        if (guess.length() != WORD_LENGTH || !isAlphabetic(guess)) {
            return MoveOutcome.rejected("Enter a valid 5-letter word (A-Z).");
        }

        TileState[] states = evaluate(guess);
        GuessRow row = new GuessRow(guess, states);
        rows.add(row);
        undoStack.push(row);

        if (guess.equals(secret)) {
            won = true;
            return MoveOutcome.accepted("Solved in " + rows.size() + " attempt(s)!");
        }
        if (rows.size() == MAX_ATTEMPTS) {
            return MoveOutcome.accepted("Out of attempts! The word was " + secret + ".");
        }
        return MoveOutcome.accepted("Checked " + guess + ".");
    }

    @Override
    public boolean canUndo() {
        return !isFinished() && !undoStack.isEmpty();
    }

    @Override
    public MoveOutcome undo() {
        if (!canUndo()) {
            return MoveOutcome.rejected("Nothing to undo.");
        }
        GuessRow removed = undoStack.pop();
        rows.removeLast();
        return MoveOutcome.accepted("Undid guess '" + removed.word() + "'.");
    }

    @Override
    public String nextHint() {
        if (hintQueue.isEmpty()) {
            return "No more hints left for this round.";
        }
        return "Hint: " + hintQueue.dequeue();
    }

    @Override
    public boolean isFinished() {
        return won || rows.size() >= MAX_ATTEMPTS;
    }

    @Override
    public boolean isWon() {
        return won;
    }

    @Override
    public String secretWord() {
        return secret;
    }

    @Override
    public int attemptsUsed() {
        return rows.size();
    }

    @Override
    public int maxAttempts() {
        return MAX_ATTEMPTS;
    }

    @Override
    public String renderBoard() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- WORDLE (").append(rows.size()).append("/").append(MAX_ATTEMPTS).append(") ---\n");
        for (int r = 0; r < MAX_ATTEMPTS; r++) {
            if (r < rows.size()) {
                GuessRow row = rows.get(r);
                for (int i = 0; i < WORD_LENGTH; i++) {
                    sb.append(' ').append(row.word().charAt(i))
                            .append('(').append(row.states()[i].symbol()).append(") ");
                }
            } else {
                sb.append(" _(_)  _(_)  _(_)  _(_)  _(_) ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private TileState[] evaluate(String guess) {
        TileState[] states = new TileState[WORD_LENGTH];
        ChainingHashMap<Character, Integer> remaining = new ChainingHashMap<>();

        for (int i = 0; i < WORD_LENGTH; i++) {
            char s = secret.charAt(i);
            if (guess.charAt(i) == s) {
                states[i] = TileState.EXACT;
            } else {
                remaining.put(s, remaining.getOrDefault(s, 0) + 1);
            }
        }

        for (int i = 0; i < WORD_LENGTH; i++) {
            if (states[i] != null) {
                continue;
            }
            char g = guess.charAt(i);
            int count = remaining.getOrDefault(g, 0);
            if (count > 0) {
                states[i] = TileState.PRESENT;
                remaining.put(g, count - 1);
            } else {
                states[i] = TileState.ABSENT;
            }
        }
        return states;
    }

    private void seedHints() {
        hintQueue.enqueue("Starts with '" + secret.charAt(0) + "'.");
        for (int i = 0; i < secret.length(); i++) {
            char c = secret.charAt(i);
            if ("AEIOU".indexOf(c) >= 0) {
                hintQueue.enqueue("Contains vowel '" + c + "'.");
                break;
            }
        }
        hintQueue.enqueue("Ends with '" + secret.charAt(WORD_LENGTH - 1) + "'.");
    }

    private static boolean isAlphabetic(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 'A' || c > 'Z') {
                return false;
            }
        }
        return true;
    }
}

