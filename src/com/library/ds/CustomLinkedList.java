package com.library.ds;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Generic doubly linked list implementation with head and tail pointers.
 *
 * @param <T> the type of elements held in this list
 */
public class CustomLinkedList<T> implements Iterable<T> {

    private static class Node<T> {
        T data;
        Node<T> prev;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    /**
     * Constructs an empty linked list.
     * Complexity: O(1)
     */
    public CustomLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Inserts the specified element at the beginning of this list.
     * Complexity: O(1)
     *
     * @param data element to be added
     */
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    /**
     * Appends the specified element to the end of this list.
     * Complexity: O(1)
     *
     * @param data element to be added
     */
    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    /**
     * Removes and returns the first element from this list.
     * Complexity: O(1)
     *
     * @return the first element in this list
     * @throws EmptyStructureException if this list is empty
     */
    public T removeFirst() {
        if (isEmpty()) {
            throw new EmptyStructureException("Cannot remove from an empty list.");
        }
        T data = head.data;
        head = head.next;
        if (head != null) {
            head.prev = null;
        } else {
            tail = null;
        }
        size--;
        return data;
    }

    /**
     * Removes and returns the last element from this list.
     * Complexity: O(1)
     *
     * @return the last element in this list
     * @throws EmptyStructureException if this list is empty
     */
    public T removeLast() {
        if (isEmpty()) {
            throw new EmptyStructureException("Cannot remove from an empty list.");
        }
        T data = tail.data;
        tail = tail.prev;
        if (tail != null) {
            tail.next = null;
        } else {
            head = null;
        }
        size--;
        return data;
    }

    /**
     * Removes the first occurrence of the specified element from this list.
     * Complexity: O(n)
     *
     * @param data element to be removed
     * @return true if the element was present and removed, false otherwise
     */
    public boolean remove(T data) {
        Node<T> current = head;
        while (current != null) {
            if (matches(current.data, data)) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Returns the element at the specified position in this list.
     * Complexity: O(n)
     *
     * @param index index of the element to return
     * @return the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for size " + size);
        }
        Node<T> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current.data;
    }

    /**
     * Returns true if this list contains the specified element.
     * Complexity: O(n)
     *
     * @param data element whose presence in this list is to be tested
     * @return true if this list contains the specified element
     */
    public boolean contains(T data) {
        Node<T> current = head;
        while (current != null) {
            if (matches(current.data, data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Returns the number of elements in this list.
     * Complexity: O(1)
     *
     * @return the number of elements
     */
    public int size() {
        return size;
    }

    /**
     * Returns true if this list contains no elements.
     * Complexity: O(1)
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all elements from this list.
     * Complexity: O(1)
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     * Complexity: O(1)
     *
     * @return an iterator over the elements in this list
     */
    @Override
    public Iterator<T> iterator() {
        return new CustomLinkedListIterator();
    }

    private void unlink(Node<T> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        size--;
    }

    private boolean matches(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }

    private class CustomLinkedListIterator implements Iterator<T> {
        private Node<T> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (current == null) {
                throw new NoSuchElementException("No more elements in list iterator.");
            }
            T data = current.data;
            current = current.next;
            return data;
        }
    }
}
