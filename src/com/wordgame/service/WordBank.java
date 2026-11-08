package com.wordgame.service;

import com.wordgame.ds.ArrayQueue;
import com.wordgame.ds.ChainingHashMap;
import com.wordgame.ds.DynamicArray;
import com.wordgame.model.GameMode;

import java.util.Random;

public class WordBank {
    private static final String[] WORDLE_WORDS = {
            "APPLE", "BRAVE", "CHAIR", "CRANE", "DANCE", "EARTH", "FLAME", "GRAPE",
            "HOUSE", "INDEX", "JELLY", "KNIFE", "LIGHT", "MAGIC", "NIGHT", "OCEAN",
            "PEARL", "QUEEN", "RIVER", "STONE", "TABLE", "UNITY", "VOICE", "WATER",
            "YOUTH", "ZEBRA", "STACK", "QUEUE", "ARRAY", "GRAPH", "NODES", "TREES",
            "PRIME", "SCALE", "SOLID", "CLEAN", "LOGIC", "SMART", "WORLD", "CLOUD"
    };

    private final ChainingHashMap<GameMode, DynamicArray<String>> pools = new ChainingHashMap<>();
    private final ChainingHashMap<GameMode, ArrayQueue<String>> queues = new ChainingHashMap<>();
    private final ChainingHashMap<String, Boolean> dictionary = new ChainingHashMap<>();
    private final Random random;

    public WordBank() {
        this(new Random());
    }

    public WordBank(Random random) {
        this.random = random;
        registerPool(GameMode.WORDLE, WORDLE_WORDS);
    }

    public String nextWord(GameMode mode) {
        ArrayQueue<String> queue = queues.get(mode);
        if (queue == null) {
            throw new IllegalArgumentException("Unsupported mode: " + mode);
        }
        if (queue.isEmpty()) {
            refillQueue(mode, queue);
        }
        return queue.dequeue();
    }

    public boolean containsWord(String word) {
        return word != null && dictionary.containsKey(word.toUpperCase());
    }

    public DynamicArray<String> allWordsFor(GameMode mode) {
        return pools.get(mode);
    }

    public ChainingHashMap<String, Boolean> dictionaryView() {
        return dictionary;
    }

    private void registerPool(GameMode mode, String[] words) {
        DynamicArray<String> list = new DynamicArray<>(words.length);
        for (String word : words) {
            list.add(word);
            dictionary.put(word, Boolean.TRUE);
        }
        pools.put(mode, list);
        ArrayQueue<String> queue = new ArrayQueue<>(words.length);
        refillQueue(mode, queue);
        queues.put(mode, queue);
    }

    private void refillQueue(GameMode mode, ArrayQueue<String> queue) {
        DynamicArray<String> pool = pools.get(mode);
        DynamicArray<String> copy = new DynamicArray<>(pool.size());
        for (int i = 0; i < pool.size(); i++) {
            copy.add(pool.get(i));
        }
        for (int i = copy.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            String tmp = copy.get(i);
            copy.set(i, copy.get(j));
            copy.set(j, tmp);
        }
        for (int i = 0; i < copy.size(); i++) {
            queue.enqueue(copy.get(i));
        }
    }
}

