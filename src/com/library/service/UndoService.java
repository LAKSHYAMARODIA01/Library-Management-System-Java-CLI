package com.library.service;

import com.library.model.Action;
import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.Member;
import com.library.model.Transaction;
import com.library.model.TransactionType;
import com.library.util.LibraryException;

/**
 * Service managing LIFO undo operations and transactional reversals.
 */
public class UndoService {

    private final LibraryContext context;

    /**
     * Constructs UndoService with the shared library context.
     * Complexity: O(1)
     *
     * @param context shared library context
     */
    public UndoService(LibraryContext context) {
        this.context = context;
    }

    /**
     * Reverses the most recent reversible action on the undo stack (FR-U1).
     * Complexity: O(n)
     *
     * @return the undone Action record, or null if undo stack was empty
     */
    public Action undoLastAction() {
        if (context.getUndoStack().isEmpty()) {
            return null;
        }
        Action action = context.getUndoStack().pop();
        switch (action.getType()) {
            case ADD_BOOK -> undoAddBook(action);
            case ISSUE -> undoIssue(action);
            case RETURN -> undoReturn(action);
            case RESERVE -> undoReserve(action);
            default -> throw new LibraryException("Unsupported undo action: " + action.getType());
        }
        cleanMatchingTransaction(action.getTransactionId());
        return action;
    }

    /**
     * Inspects the top of the undo stack without removing it (FR-U2).
     * Complexity: O(1)
     *
     * @return top Action record, or null if empty
     */
    public Action peek() {
        return context.getUndoStack().isEmpty() ? null : context.getUndoStack().peek();
    }

    /**
     * Returns the count of reversible actions currently available on the undo stack.
     * Complexity: O(1)
     *
     * @return undo stack size
     */
    public int getUndoCount() {
        return context.getUndoStack().size();
    }

    private void undoAddBook(Action action) {
        Book book = context.getBooksById().get(action.getBookId());
        if (book != null) {
            context.getBooksById().remove(book.getId());
            context.getBooksByIsbn().remove(book.getIsbn());
            context.getCatalog().remove(book);
        }
    }

    private void undoIssue(Action action) {
        Book book = context.getBooksById().get(action.getBookId());
        Member member = context.getMembersById().get(action.getMemberId());
        if (book != null) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            book.decrementBorrowCount();
            if (action.isDequeuedFromQueue()) {
                book.getReservations().enqueueFront(action.getMemberId());
            }
        }
        if (member != null) {
            for (Loan loan : member.getLoans()) {
                if (loan.getBookId().equals(action.getBookId())) {
                    member.getLoans().remove(loan);
                    break;
                }
            }
        }
    }

    private void undoReturn(Action action) {
        Book book = context.getBooksById().get(action.getBookId());
        Member member = context.getMembersById().get(action.getMemberId());
        if (book != null) {
            book.setAvailableCopies(book.getAvailableCopies() - 1);
        }
        if (member != null) {
            Loan loan = new Loan(action.getBookId(), action.getMemberId(),
                    action.getIssueDate(), action.getDueDate());
            member.getLoans().addLast(loan);
            if (action.getFine() > 0) {
                member.subtractFines(action.getFine());
            }
        }
    }

    private void undoReserve(Action action) {
        Book book = context.getBooksById().get(action.getBookId());
        if (book != null) {
            book.getReservations().remove(action.getMemberId());
        }
    }

    private void cleanMatchingTransaction(String transactionId) {
        if (transactionId == null || transactionId.isEmpty()) return;
        for (Transaction tx : context.getTransactionHistory()) {
            if (tx.getId().equals(transactionId)) {
                context.getTransactionHistory().remove(tx);
                break;
            }
        }
    }
}
