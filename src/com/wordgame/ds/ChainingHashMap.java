package com.wordgame.ds;

import java.util.Objects;

public class ChainingHashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double MAX_LOAD_FACTOR = 0.75;

    private static final class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Entry<K, V>[] buckets;
    private int size;
    private int rehashCount;

    @SuppressWarnings("unchecked")
    public ChainingHashMap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.buckets = (Entry<K, V>[]) new Entry[initialCapacity];
    }

    public ChainingHashMap() {
        this(DEFAULT_CAPACITY);
    }

    public void put(K key, V value) {
        Objects.requireNonNull(key, "Key must not be null");
        if ((double) (size + 1) / buckets.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        int index = bucketIndex(key, buckets.length);
        for (Entry<K, V> curr = buckets[index]; curr != null; curr = curr.next) {
            if (curr.key.equals(key)) {
                curr.value = value;
                return;
            }
        }
        buckets[index] = new Entry<>(key, value, buckets[index]);
        size++;
    }

    public V get(K key) {
        Objects.requireNonNull(key, "Key must not be null");
        int index = bucketIndex(key, buckets.length);
        for (Entry<K, V> curr = buckets[index]; curr != null; curr = curr.next) {
            if (curr.key.equals(key)) {
                return curr.value;
            }
        }
        return null;
    }

    public V getOrDefault(K key, V defaultValue) {
        V value = get(key);
        return value != null || containsKey(key) ? value : defaultValue;
    }

    public boolean containsKey(K key) {
        Objects.requireNonNull(key, "Key must not be null");
        int index = bucketIndex(key, buckets.length);
        for (Entry<K, V> curr = buckets[index]; curr != null; curr = curr.next) {
            if (curr.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public V remove(K key) {
        Objects.requireNonNull(key, "Key must not be null");
        int index = bucketIndex(key, buckets.length);
        Entry<K, V> prev = null;
        Entry<K, V> curr = buckets[index];
        while (curr != null) {
            if (curr.key.equals(key)) {
                if (prev == null) {
                    buckets[index] = curr.next;
                } else {
                    prev.next = curr.next;
                }
                size--;
                return curr.value;
            }
            prev = curr;
            curr = curr.next;
        }
        return null;
    }

    public DynamicArray<K> keys() {
        DynamicArray<K> result = new DynamicArray<>(Math.max(1, size));
        for (Entry<K, V> bucket : buckets) {
            for (Entry<K, V> curr = bucket; curr != null; curr = curr.next) {
                result.add(curr.key);
            }
        }
        return result;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int bucketCount() {
        return buckets.length;
    }

    public double loadFactor() {
        return (double) size / buckets.length;
    }

    public int rehashCount() {
        return rehashCount;
    }

    public int collisionCount() {
        int collisions = 0;
        for (Entry<K, V> bucket : buckets) {
            if (bucket != null) {
                int chainLen = 0;
                for (Entry<K, V> curr = bucket; curr != null; curr = curr.next) {
                    chainLen++;
                }
                if (chainLen > 1) {
                    collisions += chainLen - 1;
                }
            }
        }
        return collisions;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        Entry<K, V>[] oldBuckets = buckets;
        buckets = (Entry<K, V>[]) new Entry[oldBuckets.length * 2];
        rehashCount++;
        for (Entry<K, V> bucket : oldBuckets) {
            Entry<K, V> curr = bucket;
            while (curr != null) {
                Entry<K, V> next = curr.next;
                int index = bucketIndex(curr.key, buckets.length);
                curr.next = buckets[index];
                buckets[index] = curr;
                curr = next;
            }
        }
    }

    private int bucketIndex(K key, int capacity) {
        int h = key.hashCode();
        h ^= (h >>> 16);
        return (h & 0x7fffffff) % capacity;
    }
}

