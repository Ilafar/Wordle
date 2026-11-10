package com.wordgame.service;

import com.wordgame.ds.BinarySearchTree;
import com.wordgame.ds.ChainingHashMap;
import com.wordgame.ds.DynamicArray;
import com.wordgame.ds.MinHeap;
import com.wordgame.model.GameMode;
import com.wordgame.model.ScoreEntry;

public class ProgressTracker {
    private final BinarySearchTree<String> solvedWords = new BinarySearchTree<>();
    private final MinHeap<ScoreEntry> bestScores = new MinHeap<>();
    private final ChainingHashMap<GameMode, Integer> gamesPlayed = new ChainingHashMap<>();
    private final ChainingHashMap<GameMode, Integer> gamesWon = new ChainingHashMap<>();

    public void recordRound(GameMode mode, String word, boolean won, int attemptsUsed, long durationMs) {
        gamesPlayed.put(mode, gamesPlayed.getOrDefault(mode, 0) + 1);
        if (won) {
            gamesWon.put(mode, gamesWon.getOrDefault(mode, 0) + 1);
            solvedWords.insert(word);
            bestScores.insert(new ScoreEntry(mode, word, attemptsUsed, durationMs));
        }
    }

    public DynamicArray<ScoreEntry> topScores(int k) {
        return bestScores.topK(k);
    }

    public DynamicArray<String> solvedAlphabetical() {
        return solvedWords.inOrder();
    }

    public String formatStats(WordBank wordBank) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== PLAYER STATS ===\n");
        for (GameMode mode : GameMode.values()) {
            int played = gamesPlayed.getOrDefault(mode, 0);
            int won = gamesWon.getOrDefault(mode, 0);
            sb.append(String.format("%-8s: %d won / %d played%n", mode.displayName(), won, played));
        }

        sb.append("\nTop 5 Best Rounds (Min-Heap):\n");
        DynamicArray<ScoreEntry> top = topScores(5);
        if (top.isEmpty()) {
            sb.append("  (No wins recorded yet)\n");
        } else {
            for (int i = 0; i < top.size(); i++) {
                sb.append("  ").append(i + 1).append(". ").append(top.get(i)).append('\n');
            }
        }

        DynamicArray<String> words = solvedAlphabetical();
        sb.append("\nSolved Words BST (size=").append(solvedWords.size())
                .append(", height=").append(solvedWords.height()).append("): ");
        if (words.isEmpty()) {
            sb.append("none");
        } else {
            for (int i = 0; i < words.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(words.get(i));
            }
        }

        ChainingHashMap<String, Boolean> dict = wordBank.dictionaryView();
        sb.append(String.format("%nDictionary HashMap: %d words, %d buckets, load=%.2f, collisions=%d, rehashes=%d%n",
                dict.size(), dict.bucketCount(), dict.loadFactor(), dict.collisionCount(), dict.rehashCount()));
        return sb.toString();
    }
}

