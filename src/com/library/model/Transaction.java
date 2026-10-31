package com.library.model;

import com.library.util.Validator;
import java.time.LocalDate;

/**
 * Audit record representing a library event in transaction history.
 */
public class Transaction {

    private final String id;
    private final TransactionType type;
    private final String bookId;
    private final String memberId;
    private final LocalDate date;
    private final double amount;
    private final String note;

    /**
     * Constructs a new Transaction audit entry.
     * Complexity: O(1)
     *
     * @param id       transaction ID (e.g. T0001)
     * @param type     operation type
     * @param bookId   affected book ID (or null/empty if not applicable)
     * @param memberId affected member ID (or null/empty if not applicable)
     * @param date     transaction date
     * @param amount   monetary amount involved (e.g. fine)
     * @param note     human-readable notes
     */
    public Transaction(String id, TransactionType type, String bookId, String memberId,
                       LocalDate date, double amount, String note) {
        Validator.validateNonEmpty(id, "Transaction ID");
        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null.");
        }
        if (date == null) {
            throw new IllegalArgumentException("Transaction date cannot be null.");
        }
        this.id = id;
        this.type = type;
        this.bookId = bookId != null ? bookId : "";
        this.memberId = memberId != null ? memberId : "";
        this.date = date;
        this.amount = amount;
        this.note = note != null ? note : "";
    }

    /**
     * Returns the transaction identifier.
     * Complexity: O(1)
     *
     * @return transaction ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the transaction type.
     * Complexity: O(1)
     *
     * @return transaction type
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
     * Returns the date the transaction occurred.
     * Complexity: O(1)
     *
     * @return transaction date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns any monetary amount associated with the transaction.
     * Complexity: O(1)
     *
     * @return transaction amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the transaction note or description.
     * Complexity: O(1)
     *
     * @return note
     */
    public String getNote() {
        return note;
    }

    /**
     * Returns a string representation of the transaction.
     * Complexity: O(1)
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "[" + id + "] " + date + " | " + type + " | Book: " + bookId
                + " | Member: " + memberId
                + (amount > 0 ? " | Amount: Rs " + String.format("%.0f", amount) : "")
                + " | " + note;
    }
}
