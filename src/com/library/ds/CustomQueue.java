package com.library.ds;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Singly-linked FIFO queue with front, rear, and front-insertion capabilities.
 *
 * @param <T> the type of elements held in this queue
 */
public class CustomQueue<T> implements Iterable<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> front;
    private Node<T> rear;
    private int size;

    /**
     * Constructs an empty queue.
     * Complexity: O(1)
     */
    public CustomQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    /**
     * Inserts the specified element at the rear of this queue.
     * Complexity: O(1)
     *
     * @param item element to add
     */
    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    /**
     * Removes and returns the element at the front of this queue.
     * Complexity: O(1)
     *
     * @return the front element
     * @throws EmptyStructureException if this queue is empty
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new EmptyStructureException("Cannot dequeue from an empty queue.");
        }
        T data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }

    /**
     * Retrieves, but does not remove, the front element of this queue.
     * Complexity: O(1)
     *
     * @return the front element
     * @throws EmptyStructureException if this queue is empty
     */
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStructureException("Cannot peek into an empty queue.");
        }
        return front.data;
    }

    /**
     * Inserts the specified element at the very front of this queue.
     * Complexity: O(1)
     *
     * @param item element to insert at the front
     */
    public void enqueueFront(T item) {
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            newNode.next = front;
            front = newNode;
        }
        size++;
    }

    /**
     * Removes the first occurrence of the specified element from the queue.
     * Complexity: O(n)
     *
     * @param item element to remove
     * @return true if removed, false otherwise
     */
    public boolean remove(T item) {
        if (isEmpty()) {
            return false;
        }
        if (matches(front.data, item)) {
            front = front.next;
            if (front == null) {
                rear = null;
            }
            size--;
            return true;
        }
        Node<T> prev = front;
        Node<T> current = front.next;
        while (current != null) {
            if (matches(current.data, item)) {
                prev.next = current.next;
                if (current == rear) {
                    rear = prev;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    /**
     * Returns true if this queue contains the specified element.
     * Complexity: O(n)
     *
     * @param item element to check
     * @return true if found, false otherwise
     */
    public boolean contains(T item) {
        Node<T> current = front;
        while (current != null) {
            if (matches(current.data, item)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Returns the 1-based position of the specified element from the front, or -1 if not found.
     * Complexity: O(n)
     *
     * @param item element to find position of
     * @return 1-based position, or -1
     */
    public int positionOf(T item) {
        int pos = 1;
        Node<T> current = front;
        while (current != null) {
            if (matches(current.data, item)) {
                return pos;
            }
            pos++;
            current = current.next;
        }
        return -1;
    }

    /**
     * Returns the number of elements in this queue.
     * Complexity: O(1)
     *
     * @return queue size
     */
    public int size() {
        return size;
    }

    /**
     * Checks if this queue is empty.
     * Complexity: O(1)
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns an iterator over the elements in this queue in FIFO order.
     * Complexity: O(1)
     *
     * @return queue iterator
     */
    @Override
    public Iterator<T> iterator() {
        return new CustomQueueIterator();
    }

    private boolean matches(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }

    private class CustomQueueIterator implements Iterator<T> {
        private Node<T> current = front;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (current == null) {
                throw new NoSuchElementException("No more elements in queue iterator.");
            }
            T data = current.data;
            current = current.next;
            return data;
        }
    }
}
