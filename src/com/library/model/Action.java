package com.library.model;

import java.time.LocalDate;

/**
 * Reversible action record pushed onto the undo stack.
 */
public class Action {

    private final TransactionType type;
    private final String bookId;
    private final String memberId;
    private final LocalDate issueDate;
    private final LocalDate dueDate;
    private final double fine;
    private final boolean dequeuedFromQueue;
    private final String transactionId;

    /**
     * Constructs a new Action record for the undo stack.
     * Complexity: O(1)
     *
     * @param type              action operation type (ADD_BOOK, ISSUE, RETURN, RESERVE)
     * @param bookId            affected book ID
     * @param memberId          affected member ID
     * @param issueDate         loan issue date if applicable
     * @param dueDate           loan due date if applicable
     * @param fine              fine amount assessed if applicable
     * @param dequeuedFromQueue whether this action dequeued a member from reservation
     * @param transactionId     corresponding transaction history ID
     */
    public Action(TransactionType type, String bookId, String memberId, LocalDate issueDate,
                  LocalDate dueDate, double fine, boolean dequeuedFromQueue, String transactionId) {
        if (type == null) {
            throw new IllegalArgumentException("Action type cannot be null.");
        }
        this.type = type;
        this.bookId = bookId != null ? bookId : "";
        this.memberId = memberId != null ? memberId : "";
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.fine = fine;
        this.dequeuedFromQueue = dequeuedFromQueue;
        this.transactionId = transactionId != null ? transactionId : "";
    }

    /**
     * Returns the reversible action type.
     * Complexity: O(1)
     *
     * @return action type
     */
    public TransactionType getType() {
        return type;
    }

    /**
     * Returns the associated book ID.
     * Complexity: O(1)
     *
     * @return book ID
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * Returns the associated member ID.
     * Complexity: O(1)
     *
     * @return member ID
     */
    public String getMemberId() {
        return memberId;
    }

    /**
     * Returns the loan issue date.
     * Complexity: O(1)
     *
     * @return issue date
     */
    public LocalDate getIssueDate() {
        return issueDate;
    }

    /**
     * Returns the loan due date.
     * Complexity: O(1)
     *
     * @return due date
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Returns the fine amount.
     * Complexity: O(1)
     *
     * @return fine
     */
    public double getFine() {
        return fine;
    }

    /**
     * Returns whether the member was dequeued from the reservation queue during this action.
     * Complexity: O(1)
     *
     * @return true if dequeued from queue
     */
    public boolean isDequeuedFromQueue() {
        return dequeuedFromQueue;
    }

    /**
     * Returns the corresponding transaction ID.
     * Complexity: O(1)
     *
     * @return transaction ID
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Returns a string representation of the action.
     * Complexity: O(1)
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "Action: " + type + " (Book: " + bookId
                + (memberId.isEmpty() ? "" : ", Member: " + memberId)
                + (transactionId.isEmpty() ? "" : ", Tx: " + transactionId) + ")";
    }
}
