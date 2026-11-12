package com.wordgame.ds;

import java.util.Objects;

public class BinarySearchTree<T extends Comparable<T>> {
    private static final class Node<E> {
        final E value;
        Node<E> left;
        Node<E> right;

        Node(E value) {
            this.value = value;
        }
    }

    private Node<T> root;
    private int size;

    public boolean insert(T value) {
        Objects.requireNonNull(value, "Value must not be null");
        if (root == null) {
            root = new Node<>(value);
            size = 1;
            return true;
        }
        Node<T> curr = root;
        while (true) {
            int cmp = value.compareTo(curr.value);
            if (cmp == 0) {
                return false;
            }
            if (cmp < 0) {
                if (curr.left == null) {
                    curr.left = new Node<>(value);
                    size++;
                    return true;
                }
                curr = curr.left;
            } else {
                if (curr.right == null) {
                    curr.right = new Node<>(value);
                    size++;
                    return true;
                }
                curr = curr.right;
            }
        }
    }

    public boolean contains(T value) {
        Objects.requireNonNull(value, "Value must not be null");
        Node<T> curr = root;
        while (curr != null) {
            int cmp = value.compareTo(curr.value);
            if (cmp == 0) {
                return true;
            }
            curr = cmp < 0 ? curr.left : curr.right;
        }
        return false;
    }

    public DynamicArray<T> inOrder() {
        DynamicArray<T> result = new DynamicArray<>(Math.max(1, size));
        inOrder(root, result);
        return result;
    }

    public DynamicArray<T> preOrder() {
        DynamicArray<T> result = new DynamicArray<>(Math.max(1, size));
        preOrder(root, result);
        return result;
    }

    public int height() {
        return height(root);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void inOrder(Node<T> node, DynamicArray<T> out) {
        if (node == null) {
            return;
        }
        inOrder(node.left, out);
        out.add(node.value);
        inOrder(node.right, out);
    }

    private void preOrder(Node<T> node, DynamicArray<T> out) {
        if (node == null) {
            return;
        }
        out.add(node.value);
        preOrder(node.left, out);
        preOrder(node.right, out);
    }

    private int height(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }
}

