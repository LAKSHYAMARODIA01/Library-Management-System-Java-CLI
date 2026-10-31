package com.library.model;

import com.library.util.Validator;
import java.time.LocalDate;

/**
 * Domain model representing an active book loan issued to a member.
 */
public class Loan {

    private final String bookId;
    private final String memberId;
    private final LocalDate issueDate;
    private final LocalDate dueDate;

    /**
     * Constructs a Loan with the specified attributes.
     * Complexity: O(1)
     *
     * @param bookId    issued book ID
     * @param memberId  borrowing member ID
     * @param issueDate date of issue
     * @param dueDate   date when loan is due
     */
    public Loan(String bookId, String memberId, LocalDate issueDate, LocalDate dueDate) {
        Validator.validateNonEmpty(bookId, "Book ID");
        Validator.validateNonEmpty(memberId, "Member ID");
        if (issueDate == null) {
            throw new IllegalArgumentException("Issue date cannot be null.");
        }
        if (dueDate == null) {
            throw new IllegalArgumentException("Due date cannot be null.");
        }
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    /**
     * Returns the issued book ID.
     * Complexity: O(1)
     *
     * @return book ID
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * Returns the borrowing member ID.
     * Complexity: O(1)
     *
     * @return member ID
     */
    public String getMemberId() {
        return memberId;
    }

    /**
     * Returns the issue date.
     * Complexity: O(1)
     *
     * @return issue date
     */
    public LocalDate getIssueDate() {
        return issueDate;
    }

    /**
     * Returns the due date.
     * Complexity: O(1)
     *
     * @return due date
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Checks equality based on bookId and memberId.
     * Complexity: O(1)
     *
     * @param obj object to compare with
     * @return true if equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Loan other)) return false;
        return bookId.equals(other.bookId) && memberId.equals(other.memberId);
    }

    /**
     * Returns hash code based on bookId and memberId.
     * Complexity: O(1)
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return bookId.hashCode() * 31 + memberId.hashCode();
    }

    /**
     * Returns a string representation of the loan.
     * Complexity: O(1)
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "Loan [Book: " + bookId + ", Member: " + memberId
                + ", Issued: " + issueDate + ", Due: " + dueDate + "]";
    }
}
