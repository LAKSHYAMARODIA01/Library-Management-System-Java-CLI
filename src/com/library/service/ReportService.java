package com.library.service;

import com.library.ds.CustomLinkedList;
import com.library.model.Book;
import com.library.model.Transaction;
import com.library.util.Validator;

/**
 * Service generating system reports, audit history, and catalog analytics.
 */
public class ReportService {

    private final LibraryContext context;

    /**
     * Constructs ReportService with the shared library context.
     * Complexity: O(1)
     *
     * @param context shared library context
     */
    public ReportService(LibraryContext context) {
        this.context = context;
    }

    /**
     * Returns full transaction history in chronological order (FR-H1).
     * Complexity: O(1)
     *
     * @return transaction history linked list
     */
    public CustomLinkedList<Transaction> getAllTransactions() {
        return context.getTransactionHistory();
    }

    /**
     * Returns transaction history filtered by member ID (FR-H2).
     * Complexity: O(n)
     *
     * @param memberId member identifier
     * @return filtered transaction list
     */
    public CustomLinkedList<Transaction> getTransactionsByMember(String memberId) {
        Validator.validateNonEmpty(memberId, "Member ID");
        CustomLinkedList<Transaction> filtered = new CustomLinkedList<>();
        for (Transaction tx : context.getTransactionHistory()) {
            if (memberId.equals(tx.getMemberId())) {
                filtered.addLast(tx);
            }
        }
        return filtered;
    }

    /**
     * Returns transaction history filtered by book ID (FR-H3).
     * Complexity: O(n)
     *
     * @param bookId book identifier
     * @return filtered transaction list
     */
    public CustomLinkedList<Transaction> getTransactionsByBook(String bookId) {
        Validator.validateNonEmpty(bookId, "Book ID");
        CustomLinkedList<Transaction> filtered = new CustomLinkedList<>();
        for (Transaction tx : context.getTransactionHistory()) {
            if (bookId.equals(tx.getBookId())) {
                filtered.addLast(tx);
            }
        }
        return filtered;
    }

    /**
     * Finds top 3 most borrowed books via single-pass linear scan (FR-H4).
     * Complexity: O(n)
     *
     * @return list containing up to 3 most borrowed books
     */
    public CustomLinkedList<Book> getTop3MostBorrowedBooks() {
        Book first = null;
        Book second = null;
        Book third = null;

        for (Book book : context.getCatalog()) {
            int count = book.getBorrowCount();
            if (first == null || count > first.getBorrowCount()) {
                third = second;
                second = first;
                first = book;
            } else if (second == null || count > second.getBorrowCount()) {
                third = second;
                second = book;
            } else if (third == null || count > third.getBorrowCount()) {
                third = book;
            }
        }

        CustomLinkedList<Book> top3 = new CustomLinkedList<>();
        if (first != null) top3.addLast(first);
        if (second != null) top3.addLast(second);
        if (third != null) top3.addLast(third);
        return top3;
    }
}
