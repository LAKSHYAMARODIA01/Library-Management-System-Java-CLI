package com.library.service;

import com.library.ds.CustomLinkedList;
import com.library.model.Action;
import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.Member;
import com.library.model.Transaction;
import com.library.model.TransactionType;
import com.library.util.Constants;
import com.library.util.LibraryException;
import com.library.util.Validator;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Service managing book circulation, issuing, returns, overdue calculations, and fines.
 */
public class CirculationService {

    private final LibraryContext context;

    /**
     * Constructs CirculationService with the shared library context.
     * Complexity: O(1)
     *
     * @param context shared library context
     */
    public CirculationService(LibraryContext context) {
        this.context = context;
    }

    /**
     * Issues a book copy to a member (FR-C1), enforcing BR-3, BR-4, BR-5, and BR-6.
     * Complexity: O(loans count)
     *
     * @param bookId   book identifier
     * @param memberId member identifier
     * @return created Loan instance
     */
    public Loan issueBook(String bookId, String memberId) {
        Book book = validateBookForIssue(bookId);
        Member member = validateMemberForIssue(memberId, bookId);
        boolean dequeued = handleQueueForIssue(book, memberId);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        book.incrementBorrowCount();

        LocalDate issueDate = context.getClockService().getSimDate();
        LocalDate dueDate = issueDate.plusDays(Constants.LOAN_PERIOD_DAYS);
        Loan loan = new Loan(bookId, memberId, issueDate, dueDate);
        member.getLoans().addLast(loan);

        recordIssueAudit(bookId, memberId, issueDate, dueDate, dequeued);
        return loan;
    }

    /**
     * Returns an issued book copy (FR-C2), assessing overdue fines (BR-2) and checking queue (BR-11).
     * Complexity: O(loans count)
     *
     * @param bookId   book identifier
     * @param memberId member identifier
     * @return ReturnResult summarizing fines and queue state
     */
    public ReturnResult returnBook(String bookId, String memberId) {
        Validator.validateNonEmpty(bookId, "Book ID");
        Validator.validateNonEmpty(memberId, "Member ID");
        Book book = context.getBooksById().get(bookId);
        if (book == null) throw new LibraryException("Book " + bookId + " not found.");
        Member member = context.getMembersById().get(memberId);
        if (member == null) throw new LibraryException("Member " + memberId + " not found.");

        Loan targetLoan = findMemberLoan(member, bookId);
        member.getLoans().remove(targetLoan);
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        LocalDate simDate = context.getClockService().getSimDate();
        long daysLate = ChronoUnit.DAYS.between(targetLoan.getDueDate(), simDate);
        double fine = daysLate > 0 ? daysLate * Constants.FINE_RATE_PER_DAY : 0.0;
        if (fine > 0) member.addFines(fine);

        recordReturnAudit(bookId, memberId, targetLoan, fine, daysLate, simDate);
        String nextMember = book.getReservations().isEmpty() ? null : book.getReservations().peek();
        return new ReturnResult(bookId, memberId, fine, Math.max(0, daysLate), nextMember, targetLoan.getDueDate());
    }

    /**
     * Lists all currently overdue loans across all members (FR-C3).
     * Complexity: O(total members * loans)
     *
     * @return list of overdue records
     */
    public CustomLinkedList<OverdueRecord> getOverdueLoans() {
        LocalDate simDate = context.getClockService().getSimDate();
        CustomLinkedList<OverdueRecord> overdueList = new CustomLinkedList<>();
        for (Member member : context.getMembersById().values()) {
            for (Loan loan : member.getLoans()) {
                if (simDate.isAfter(loan.getDueDate())) {
                    long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), simDate);
                    double fineSoFar = daysLate * Constants.FINE_RATE_PER_DAY;
                    Book book = context.getBooksById().get(loan.getBookId());
                    String title = book != null ? book.getTitle() : "Unknown Title";
                    overdueList.addLast(new OverdueRecord(loan.getBookId(), title, member.getId(),
                            member.getName(), loan.getDueDate(), daysLate, fineSoFar));
                }
            }
        }
        return overdueList;
    }

    /**
     * Returns all active loans for a specific member (FR-C4).
     * Complexity: O(1)
     *
     * @param memberId member identifier
     * @return member's loan list
     */
    public CustomLinkedList<Loan> getMemberLoans(String memberId) {
        Validator.validateNonEmpty(memberId, "Member ID");
        Member member = context.getMembersById().get(memberId);
        if (member == null) {
            throw new LibraryException("Member " + memberId + " not found.");
        }
        return member.getLoans();
    }

    private Book validateBookForIssue(String bookId) {
        Validator.validateNonEmpty(bookId, "Book ID");
        Book book = context.getBooksById().get(bookId);
        if (book == null) throw new LibraryException("Book " + bookId + " not found.");
        if (book.getAvailableCopies() <= 0) {
            throw new LibraryException("No copies available for book " + bookId + " (BR-5).");
        }
        return book;
    }

    private Member validateMemberForIssue(String memberId, String bookId) {
        Validator.validateNonEmpty(memberId, "Member ID");
        Member member = context.getMembersById().get(memberId);
        if (member == null) throw new LibraryException("Member " + memberId + " not found.");
        if (member.getLoans().size() >= Constants.MAX_LOANS_PER_MEMBER) {
            throw new LibraryException("Member " + memberId + " has reached maximum loan limit of "
                    + Constants.MAX_LOANS_PER_MEMBER + " (BR-3).");
        }
        for (Loan l : member.getLoans()) {
            if (l.getBookId().equals(bookId)) {
                throw new LibraryException("Member " + memberId
                        + " already holds a copy of book " + bookId + " (BR-4).");
            }
        }
        return member;
    }

    private boolean handleQueueForIssue(Book book, String memberId) {
        if (!book.getReservations().isEmpty()) {
            String front = book.getReservations().peek();
            if (!front.equals(memberId)) {
                throw new LibraryException("Book " + book.getId()
                        + " is reserved. Next in queue is member " + front + " (BR-6).");
            }
            book.getReservations().dequeue();
            return true;
        }
        return false;
    }

    private Loan findMemberLoan(Member member, String bookId) {
        for (Loan l : member.getLoans()) {
            if (l.getBookId().equals(bookId)) return l;
        }
        throw new LibraryException("Member " + member.getId()
                + " does not hold a loan for book " + bookId + ".");
    }

    private void recordIssueAudit(String bookId, String memberId, LocalDate issueDate,
                                  LocalDate dueDate, boolean dequeued) {
        String txId = context.getIdGenerator().nextTransactionId();
        Transaction tx = new Transaction(txId, TransactionType.ISSUE, bookId, memberId,
                issueDate, 0.0, "Issued. Due: " + dueDate);
        context.getTransactionHistory().addLast(tx);
        Action action = new Action(TransactionType.ISSUE, bookId, memberId, issueDate, dueDate,
                0.0, dequeued, txId);
        context.getUndoStack().push(action);
    }

    private void recordReturnAudit(String bookId, String memberId, Loan loan, double fine,
                                   long daysLate, LocalDate simDate) {
        String txId = context.getIdGenerator().nextTransactionId();
        String note = fine > 0 ? "Returned " + daysLate + " days late. Fine: Rs "
                + String.format("%.0f", fine) : "Returned on time.";
        Transaction tx = new Transaction(txId, TransactionType.RETURN, bookId, memberId,
                simDate, fine, note);
        context.getTransactionHistory().addLast(tx);
        Action action = new Action(TransactionType.RETURN, bookId, memberId, loan.getIssueDate(),
                loan.getDueDate(), fine, false, txId);
        context.getUndoStack().push(action);
    }
}
