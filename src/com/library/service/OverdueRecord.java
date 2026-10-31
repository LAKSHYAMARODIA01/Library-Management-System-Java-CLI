package com.library.service;

import java.time.LocalDate;

/**
 * Report record capturing an overdue loan across the library.
 */
public class OverdueRecord {

    private final String bookId;
    private final String bookTitle;
    private final String memberId;
    private final String memberName;
    private final LocalDate dueDate;
    private final long daysLate;
    private final double fineSoFar;

    /**
     * Constructs an OverdueRecord with the specified metrics.
     * Complexity: O(1)
     *
     * @param bookId     overdue book ID
     * @param bookTitle  overdue book title
     * @param memberId   borrowing member ID
     * @param memberName borrowing member name
     * @param dueDate    loan due date
     * @param daysLate   days overdue
     * @param fineSoFar  accumulated fine so far
     */
    public OverdueRecord(String bookId, String bookTitle, String memberId, String memberName,
                         LocalDate dueDate, long daysLate, double fineSoFar) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.memberId = memberId;
        this.memberName = memberName;
        this.dueDate = dueDate;
        this.daysLate = daysLate;
        this.fineSoFar = fineSoFar;
    }

    /**
     * Returns the book ID.
     * Complexity: O(1)
     *
     * @return book ID
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * Returns the book title.
     * Complexity: O(1)
     *
     * @return book title
     */
    public String getBookTitle() {
        return bookTitle;
    }

    /**
     * Returns the member ID.
     * Complexity: O(1)
     *
     * @return member ID
     */
    public String getMemberId() {
        return memberId;
    }

    /**
     * Returns the member name.
     * Complexity: O(1)
     *
     * @return member name
     */
    public String getMemberName() {
        return memberName;
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
     * Returns the number of days overdue.
     * Complexity: O(1)
     *
     * @return days late
     */
    public long getDaysLate() {
        return daysLate;
    }

    /**
     * Returns the fine accrued so far.
     * Complexity: O(1)
     *
     * @return fine accrued
     */
    public double getFineSoFar() {
        return fineSoFar;
    }
}
