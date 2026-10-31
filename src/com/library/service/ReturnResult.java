package com.library.service;

import java.time.LocalDate;

/**
 * Encapsulates the outcome of a book return operation.
 */
public class ReturnResult {

    private final String bookId;
    private final String memberId;
    private final double fine;
    private final long daysLate;
    private final String nextMemberId;
    private final LocalDate dueDate;

    /**
     * Constructs a ReturnResult record.
     * Complexity: O(1)
     *
     * @param bookId       book ID returned
     * @param memberId     member ID returning the book
     * @param fine         fine charged
     * @param daysLate     number of days late
     * @param nextMemberId next member in reservation queue, if any
     * @param dueDate      original due date
     */
    public ReturnResult(String bookId, String memberId, double fine, long daysLate,
                        String nextMemberId, LocalDate dueDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.fine = fine;
        this.daysLate = daysLate;
        this.nextMemberId = nextMemberId;
        this.dueDate = dueDate;
    }

    /**
     * Returns the returned book ID.
     * Complexity: O(1)
     *
     * @return book ID
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * Returns the returning member ID.
     * Complexity: O(1)
     *
     * @return member ID
     */
    public String getMemberId() {
        return memberId;
    }

    /**
     * Returns the fine amount charged.
     * Complexity: O(1)
     *
     * @return fine amount
     */
    public double getFine() {
        return fine;
    }

    /**
     * Returns the count of overdue days.
     * Complexity: O(1)
     *
     * @return days late
     */
    public long getDaysLate() {
        return daysLate;
    }

    /**
     * Returns the member ID next in queue, or null if none.
     * Complexity: O(1)
     *
     * @return next member ID or null
     */
    public String getNextMemberId() {
        return nextMemberId;
    }

    /**
     * Returns the original due date of the returned loan.
     * Complexity: O(1)
     *
     * @return due date
     */
    public LocalDate getDueDate() {
        return dueDate;
    }
}
