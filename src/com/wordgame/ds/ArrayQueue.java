package com.wordgame.ds;

import java.util.NoSuchElementException;

public class ArrayQueue<T> {
    private static final int DEFAULT_CAPACITY = 8;

    private Object[] data;
    private int head;
    private int tail;
    private int size;

    public ArrayQueue() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayQueue(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.data = new Object[initialCapacity];
    }

    public void enqueue(T item) {
        if (size == data.length) {
            grow();
        }
        data[tail] = item;
        tail = (tail + 1) % data.length;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        T item = (T) data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        size--;
        return item;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        return (T) data[head];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            data[(head + i) % data.length] = null;
        }
        head = 0;
        tail = 0;
        size = 0;
    }

    private void grow() {
        Object[] resized = new Object[data.length * 2];
        for (int i = 0; i < size; i++) {
            resized[i] = data[(head + i) % data.length];
        }
        data = resized;
        head = 0;
        tail = size;
    }
}

