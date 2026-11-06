package com.wordgame.model;

public record ScoreEntry(
        GameMode mode,
        String word,
        int attemptsUsed,
        long durationMs
) implements Comparable<ScoreEntry> {

    @Override
    public int compareTo(ScoreEntry other) {
        int byAttempts = Integer.compare(this.attemptsUsed, other.attemptsUsed);
        if (byAttempts != 0) {
            return byAttempts;
        }
        int byDuration = Long.compare(this.durationMs, other.durationMs);
        if (byDuration != 0) {
            return byDuration;
        }
        return this.word.compareTo(other.word);
    }

    @Override
    public String toString() {
        return String.format("%-8s | %-10s | %d tries | %.2fs",
                mode.displayName(), word, attemptsUsed, durationMs / 1000.0);
    }
}

