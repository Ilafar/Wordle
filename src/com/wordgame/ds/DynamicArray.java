package com.wordgame.ds;

import java.util.Arrays;
import java.util.Objects;

public class DynamicArray<T> {
    private static final int DEFAULT_CAPACITY = 8;

    private Object[] data;
    private int size;
    private int resizeCount;

    public DynamicArray() {
        this(DEFAULT_CAPACITY);
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.data = new Object[initialCapacity];
    }

    public void add(T item) {
        ensureCapacity(size + 1);
        data[size++] = item;
    }

    public void add(int index, T item) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity(size + 1);
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = item;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) data[index];
    }

    @SuppressWarnings("unchecked")
    public T set(int index, T item) {
        checkIndex(index);
        T previous = (T) data[index];
        data[index] = item;
        return previous;
    }

    @SuppressWarnings("unchecked")
    public T removeLast() {
        if (size == 0) {
            throw new IllegalStateException("DynamicArray is empty");
        }
        T removed = (T) data[--size];
        data[size] = null;
        return removed;
    }

    @SuppressWarnings("unchecked")
    public T removeAt(int index) {
        checkIndex(index);
        T removed = (T) data[index];
        int moved = size - index - 1;
        if (moved > 0) {
            System.arraycopy(data, index + 1, data, index, moved);
        }
        data[--size] = null;
        return removed;
    }

    public int indexOf(T item) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(data[i], item)) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(T item) {
        return indexOf(item) >= 0;
    }

    public void clear() {
        Arrays.fill(data, 0, size, null);
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int capacity() {
        return data.length;
    }

    public int getResizeCount() {
        return resizeCount;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= data.length) {
            return;
        }
        int newCapacity = Math.max(data.length * 2, minCapacity);
        data = Arrays.copyOf(data, newCapacity);
        resizeCount++;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}

