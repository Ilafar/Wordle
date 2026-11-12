package com.wordgame.ds;

import java.util.NoSuchElementException;

public class LinkedStack<T> {
    private static final class Node<E> {
        final E value;
        final Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<T> head;
    private int size;

    public void push(T item) {
        head = new Node<>(item, head);
        size++;
    }

    public T pop() {
        if (head == null) {
            throw new NoSuchElementException("Stack is empty");
        }
        T value = head.value;
        head = head.next;
        size--;
        return value;
    }

    public T peek() {
        if (head == null) {
            throw new NoSuchElementException("Stack is empty");
        }
        return head.value;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        head = null;
        size = 0;
    }
}

