package com.library.util;

/**
 * Sequential identifier generator for books, members, and transactions.
 */
public class IdGenerator {

    private int bookCounter;
    private int memberCounter;
    private int transactionCounter;

    /**
     * Constructs a new IdGenerator initialized to 1 for all counters.
     * Complexity: O(1)
     */
    public IdGenerator() {
        this.bookCounter = 1;
        this.memberCounter = 1;
        this.transactionCounter = 1;
    }

    /**
     * Generates the next sequential book identifier (e.g. B001, B002).
     * Complexity: O(1)
     *
     * @return generated book ID
     */
    public String nextBookId() {
        return String.format("B%03d", bookCounter++);
    }

    /**
     * Generates the next sequential member identifier (e.g. M001, M002).
     * Complexity: O(1)
     *
     * @return generated member ID
     */
    public String nextMemberId() {
        return String.format("M%03d", memberCounter++);
    }

    /**
     * Generates the next sequential transaction identifier (e.g. T0001, T0002).
     * Complexity: O(1)
     *
     * @return generated transaction ID
     */
    public String nextTransactionId() {
        return String.format("T%04d", transactionCounter++);
    }

    /**
     * Copies counter values from another IdGenerator.
     * Complexity: O(1)
     *
     * @param other source IdGenerator
     */
    public void copyFrom(IdGenerator other) {
        if (other != null) {
            this.bookCounter = other.bookCounter;
            this.memberCounter = other.memberCounter;
            this.transactionCounter = other.transactionCounter;
        }
    }

    /**
     * Updates the book counter if the given value is greater than or equal to current.
     * Complexity: O(1)
     *
     * @param value next candidate counter value
     */
    public void ensureBookCounterAtLeast(int value) {
        if (value > bookCounter) {
            bookCounter = value;
        }
    }

    /**
     * Updates the member counter if the given value is greater than or equal to current.
     * Complexity: O(1)
     *
     * @param value next candidate counter value
     */
    public void ensureMemberCounterAtLeast(int value) {
        if (value > memberCounter) {
            memberCounter = value;
        }
    }

    /**
     * Updates the transaction counter if the given value is greater than or equal to current.
     * Complexity: O(1)
     *
     * @param value next candidate counter value
     */
    public void ensureTransactionCounterAtLeast(int value) {
        if (value > transactionCounter) {
            transactionCounter = value;
        }
    }
}
