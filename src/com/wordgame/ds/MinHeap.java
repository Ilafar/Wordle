package com.wordgame.ds;

import java.util.NoSuchElementException;
import java.util.Objects;

public class MinHeap<T extends Comparable<T>> {
    private final DynamicArray<T> heap;

    public MinHeap() {
        this.heap = new DynamicArray<>();
    }

    public static <E extends Comparable<E>> MinHeap<E> heapify(DynamicArray<E> items) {
        MinHeap<E> minHeap = new MinHeap<>();
        for (int i = 0; i < items.size(); i++) {
            minHeap.heap.add(Objects.requireNonNull(items.get(i)));
        }
        for (int i = (minHeap.size() / 2) - 1; i >= 0; i--) {
            minHeap.siftDown(i);
        }
        return minHeap;
    }

    public void insert(T item) {
        Objects.requireNonNull(item, "Item must not be null");
        heap.add(item);
        siftUp(heap.size() - 1);
    }

    public T peekMin() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        return heap.get(0);
    }

    public T extractMin() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        T min = heap.get(0);
        T last = heap.removeLast();
        if (!heap.isEmpty()) {
            heap.set(0, last);
            siftDown(0);
        }
        return min;
    }

    public DynamicArray<T> topK(int k) {
        if (k < 0) {
            throw new IllegalArgumentException("k must be non-negative");
        }
        MinHeap<T> copy = heapify(heap);
        int limit = Math.min(k, copy.size());
        DynamicArray<T> result = new DynamicArray<>(Math.max(1, limit));
        for (int i = 0; i < limit; i++) {
            result.add(copy.extractMin());
        }
        return result;
    }

    public int size() {
        return heap.size();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    private void siftUp(int index) {
        int current = index;
        while (current > 0) {
            int parent = (current - 1) / 2;
            if (heap.get(current).compareTo(heap.get(parent)) >= 0) {
                break;
            }
            swap(current, parent);
            current = parent;
        }
    }

    private void siftDown(int index) {
        int current = index;
        int n = heap.size();
        while (true) {
            int left = 2 * current + 1;
            int right = 2 * current + 2;
            int smallest = current;

            if (left < n && heap.get(left).compareTo(heap.get(smallest)) < 0) {
                smallest = left;
            }
            if (right < n && heap.get(right).compareTo(heap.get(smallest)) < 0) {
                smallest = right;
            }
            if (smallest == current) {
                break;
            }
            swap(current, smallest);
            current = smallest;
        }
    }

    private void swap(int i, int j) {
        T tmp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, tmp);
    }
}

