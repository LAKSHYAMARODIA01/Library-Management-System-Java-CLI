package com.library.ds;

/**
 * Resizable array-backed LIFO stack implementation.
 *
 * @param <T> the type of elements held in this stack
 */
public class CustomStack<T> {

    private static final int INITIAL_CAPACITY = 8;
    private T[] elements;
    private int top;

    /**
     * Constructs an empty stack with the initial capacity.
     * Complexity: O(1)
     */
    @SuppressWarnings("unchecked")
    public CustomStack() {
        this.elements = (T[]) new Object[INITIAL_CAPACITY];
        this.top = 0;
    }

    /**
     * Pushes an element onto the top of this stack.
     * Complexity: O(1) amortized
     *
     * @param item the element to be pushed
     */
    public void push(T item) {
        if (top == elements.length) {
            resize(elements.length * 2);
        }
        elements[top++] = item;
    }

    /**
     * Removes and returns the element at the top of this stack.
     * Complexity: O(1)
     *
     * @return the element at the top
     * @throws EmptyStructureException if this stack is empty
     */
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStructureException("Cannot pop from an empty stack.");
        }
        T item = elements[--top];
        elements[top] = null;
        return item;
    }

    /**
     * Looks at the element at the top of this stack without removing it.
     * Complexity: O(1)
     *
     * @return the element at the top
     * @throws EmptyStructureException if this stack is empty
     */
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStructureException("Cannot peek into an empty stack.");
        }
        return elements[top - 1];
    }

    /**
     * Tests if this stack is empty.
     * Complexity: O(1)
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return top == 0;
    }

    /**
     * Returns the number of elements in this stack.
     * Complexity: O(1)
     *
     * @return the stack size
     */
    public int size() {
        return top;
    }

    /**
     * Returns a snapshot list of stack elements ordered from top to bottom.
     * Complexity: O(n)
     *
     * @return CustomLinkedList of elements from top to bottom
     */
    public CustomLinkedList<T> printTopToBottom() {
        CustomLinkedList<T> list = new CustomLinkedList<>();
        for (int i = top - 1; i >= 0; i--) {
            list.addLast(elements[i]);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        T[] newElements = (T[]) new Object[newCapacity];
        for (int i = 0; i < top; i++) {
            newElements[i] = elements[i];
        }
        this.elements = newElements;
    }
}
