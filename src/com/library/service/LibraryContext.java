package com.library.service;

import com.library.ds.CustomHashMap;
import com.library.ds.CustomLinkedList;
import com.library.ds.CustomStack;
import com.library.model.Action;
import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Transaction;
import com.library.util.IdGenerator;

/**
 * Shared in-memory data store holding core data structures and services.
 */
public class LibraryContext {

    private final CustomHashMap<String, Book> booksById;
    private final CustomHashMap<String, Book> booksByIsbn;
    private final CustomHashMap<String, Member> membersById;
    private final CustomLinkedList<Book> catalog;
    private final CustomLinkedList<Transaction> transactionHistory;
    private final CustomStack<Action> undoStack;
    private final ClockService clockService;
    private final IdGenerator idGenerator;
    private boolean demoDataLoaded;

    /**
     * Constructs a new LibraryContext with empty structures and initialized services.
     * Complexity: O(1)
     */
    public LibraryContext() {
        this.booksById = new CustomHashMap<>();
        this.booksByIsbn = new CustomHashMap<>();
        this.membersById = new CustomHashMap<>();
        this.catalog = new CustomLinkedList<>();
        this.transactionHistory = new CustomLinkedList<>();
        this.undoStack = new CustomStack<>();
        this.clockService = new ClockService();
        this.idGenerator = new IdGenerator();
        this.demoDataLoaded = false;
    }

    /**
     * Returns the hash map indexing books by their ID.
     * Complexity: O(1)
     *
     * @return books by ID map
     */
    public CustomHashMap<String, Book> getBooksById() {
        return booksById;
    }

    /**
     * Returns the hash map indexing books by their ISBN.
     * Complexity: O(1)
     *
     * @return books by ISBN map
     */
    public CustomHashMap<String, Book> getBooksByIsbn() {
        return booksByIsbn;
    }

    /**
     * Returns the hash map indexing members by their ID.
     * Complexity: O(1)
     *
     * @return members by ID map
     */
    public CustomHashMap<String, Member> getMembersById() {
        return membersById;
    }

    /**
     * Returns the doubly linked list maintaining books in catalog insertion order.
     * Complexity: O(1)
     *
     * @return catalog list
     */
    public CustomLinkedList<Book> getCatalog() {
        return catalog;
    }

    /**
     * Returns the transaction history linked list.
     * Complexity: O(1)
     *
     * @return transaction history
     */
    public CustomLinkedList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    /**
     * Returns the undo stack of reversible actions.
     * Complexity: O(1)
     *
     * @return undo stack
     */
    public CustomStack<Action> getUndoStack() {
        return undoStack;
    }

    /**
     * Returns the system clock service.
     * Complexity: O(1)
     *
     * @return clock service
     */
    public ClockService getClockService() {
        return clockService;
    }

    /**
     * Returns the ID generator.
     * Complexity: O(1)
     *
     * @return ID generator
     */
    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    /**
     * Returns whether demo data has already been loaded.
     * Complexity: O(1)
     *
     * @return true if demo data loaded
     */
    public boolean isDemoDataLoaded() {
        return demoDataLoaded;
    }

    /**
     * Marks whether demo data has been loaded.
     * Complexity: O(1)
     *
     * @param loaded boolean flag
     */
    public void setDemoDataLoaded(boolean loaded) {
        this.demoDataLoaded = loaded;
    }
}
