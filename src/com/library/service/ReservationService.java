package com.library.service;

import com.library.ds.CustomQueue;
import com.library.model.Action;
import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.Member;
import com.library.model.Transaction;
import com.library.model.TransactionType;
import com.library.util.LibraryException;
import com.library.util.Validator;

/**
 * Service managing FIFO book reservation queues and cancellations.
 */
public class ReservationService {

    private final LibraryContext context;

    /**
     * Constructs ReservationService with the shared library context.
     * Complexity: O(1)
     *
     * @param context shared library context
     */
    public ReservationService(LibraryContext context) {
        this.context = context;
    }

    /**
     * Places a member on the reservation waitlist for a book (FR-R1), enforcing BR-7.
     * Complexity: O(loans count + queue size)
     *
     * @param bookId   book identifier
     * @param memberId member identifier
     * @return 1-based queue position
     */
    public int reserveBook(String bookId, String memberId) {
        Validator.validateNonEmpty(bookId, "Book ID");
        Validator.validateNonEmpty(memberId, "Member ID");
        Book book = context.getBooksById().get(bookId);
        if (book == null) throw new LibraryException("Book " + bookId + " not found.");
        Member member = context.getMembersById().get(memberId);
        if (member == null) throw new LibraryException("Member " + memberId + " not found.");

        validateEligibilityForReservation(book, member);

        book.getReservations().enqueue(memberId);
        int position = book.getReservations().size();

        String txId = context.getIdGenerator().nextTransactionId();
        Transaction tx = new Transaction(txId, TransactionType.RESERVE, bookId, memberId,
                context.getClockService().getSimDate(), 0.0, "Reserved at queue position " + position);
        context.getTransactionHistory().addLast(tx);

        Action action = new Action(TransactionType.RESERVE, bookId, memberId, null, null,
                0.0, false, txId);
        context.getUndoStack().push(action);

        return position;
    }

    /**
     * Cancels an existing reservation for a member (FR-R2).
     * Complexity: O(queue size)
     *
     * @param bookId   book identifier
     * @param memberId member identifier
     */
    public void cancelReservation(String bookId, String memberId) {
        Validator.validateNonEmpty(bookId, "Book ID");
        Validator.validateNonEmpty(memberId, "Member ID");
        Book book = context.getBooksById().get(bookId);
        if (book == null) throw new LibraryException("Book " + bookId + " not found.");
        Member member = context.getMembersById().get(memberId);
        if (member == null) throw new LibraryException("Member " + memberId + " not found.");

        boolean removed = book.getReservations().remove(memberId);
        if (!removed) {
            throw new LibraryException("Member " + memberId
                    + " is not in the reservation queue for book " + bookId + ".");
        }

        String txId = context.getIdGenerator().nextTransactionId();
        Transaction tx = new Transaction(txId, TransactionType.CANCEL_RESERVE, bookId, memberId,
                context.getClockService().getSimDate(), 0.0, "Cancelled reservation.");
        context.getTransactionHistory().addLast(tx);
    }

    /**
     * Returns the reservation queue of member IDs for a book in FIFO order (FR-R3).
     * Complexity: O(1)
     *
     * @param bookId book identifier
     * @return FIFO reservation queue
     */
    public CustomQueue<String> getReservationQueue(String bookId) {
        Validator.validateNonEmpty(bookId, "Book ID");
        Book book = context.getBooksById().get(bookId);
        if (book == null) throw new LibraryException("Book " + bookId + " not found.");
        return book.getReservations();
    }

    private void validateEligibilityForReservation(Book book, Member member) {
        if (book.getAvailableCopies() > 0) {
            throw new LibraryException("Cannot reserve book " + book.getId()
                    + ": copies are currently available (BR-7).");
        }
        for (Loan loan : member.getLoans()) {
            if (loan.getBookId().equals(book.getId())) {
                throw new LibraryException("Member " + member.getId()
                        + " already holds a copy of book " + book.getId() + " (BR-7).");
            }
        }
        if (book.getReservations().contains(member.getId())) {
            throw new LibraryException("Member " + member.getId()
                    + " is already in the reservation queue for book " + book.getId() + " (BR-7).");
        }
    }
}
