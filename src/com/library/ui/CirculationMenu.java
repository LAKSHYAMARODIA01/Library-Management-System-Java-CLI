package com.library.ui;

import com.library.ds.CustomLinkedList;
import com.library.model.Loan;
import com.library.service.CirculationService;
import com.library.service.OverdueRecord;
import com.library.service.ReservationService;
import com.library.service.ReturnResult;
import com.library.util.LibraryException;

/**
 * Menu screen handling circulation, issuing, returns, and overdue loan listings.
 */
public class CirculationMenu {

    private final CirculationService circulationService;
    private final ReservationService reservationService;
    private final ConsoleIO io;

    /**
     * Constructs a CirculationMenu instance.
     * Complexity: O(1)
     *
     * @param circulationService circulation service
     * @param reservationService reservation service
     * @param io                 console IO helper
     */
    public CirculationMenu(CirculationService circulationService,
                           ReservationService reservationService, ConsoleIO io) {
        this.circulationService = circulationService;
        this.reservationService = reservationService;
        this.io = io;
    }

    /**
     * Displays the circulation menu loop.
     * Complexity: O(n)
     */
    public void display() {
        while (true) {
            System.out.println("--- Circulation (Issue / Return) ---");
            System.out.println(" 1. Issue Book");
            System.out.println(" 2. Return Book");
            System.out.println(" 3. List Overdue Loans");
            System.out.println(" 4. View Member Loans");
            System.out.println(" 0. Back");
            int choice = io.readIntInRange("Enter choice: ", 0, 4);
            if (choice == 0) return;
            handleChoice(choice);
            System.out.println();
        }
    }

    private void handleChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> handleIssue();
                case 2 -> handleReturn();
                case 3 -> handleListOverdue();
                case 4 -> handleViewMemberLoans();
            }
        } catch (LibraryException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void handleIssue() {
        String bookId = io.readNonEmpty("Enter book ID: ", "Book ID");
        String memberId = io.readNonEmpty("Enter member ID: ", "Member ID");
        try {
            Loan loan = circulationService.issueBook(bookId, memberId);
            System.out.println("[OK] Book " + bookId + " issued to " + memberId
                    + ". Due date: " + loan.getDueDate());
        } catch (LibraryException e) {
            System.out.println("[ERROR] " + e.getMessage());
            if (e.getMessage().contains("No copies available")) {
                if (io.confirm("Reserve instead? (y/n): ")) {
                    try {
                        int pos = reservationService.reserveBook(bookId, memberId);
                        System.out.println("[OK] Reserved book " + bookId
                                + " for member " + memberId + " at position " + pos);
                    } catch (LibraryException re) {
                        System.out.println("[ERROR] " + re.getMessage());
                    }
                }
            }
        }
    }

    private void handleReturn() {
        String bookId = io.readNonEmpty("Enter book ID: ", "Book ID");
        String memberId = io.readNonEmpty("Enter member ID: ", "Member ID");
        ReturnResult res = circulationService.returnBook(bookId, memberId);
        System.out.println("[OK] Book " + bookId + " returned successfully by " + memberId + ".");
        if (res.getFine() > 0) {
            System.out.println("[INFO] Overdue by " + res.getDaysLate() + " days. Fine charged: "
                    + Format.currency(res.getFine()));
        } else {
            System.out.println("[INFO] Returned on time. Fine: Rs 0");
        }
        if (res.getNextMemberId() != null) {
            System.out.println("[INFO] Next member in reservation queue: " + res.getNextMemberId());
        }
    }

    private void handleListOverdue() {
        CustomLinkedList<OverdueRecord> list = circulationService.getOverdueLoans();
        if (list.isEmpty()) {
            System.out.println("[INFO] No overdue loans at present.");
            return;
        }
        System.out.println(Format.pad("Book ID", 9) + Format.pad("Title", 26)
                + Format.pad("Member ID", 11) + Format.pad("Member Name", 20)
                + Format.pad("Due Date", 12) + Format.pad("Days Late", 11) + Format.pad("Fine So Far", 12));
        System.out.println(Format.divider(101));
        for (OverdueRecord r : list) {
            System.out.println(Format.pad(r.getBookId(), 9)
                    + Format.pad(r.getBookTitle(), 26)
                    + Format.pad(r.getMemberId(), 11)
                    + Format.pad(r.getMemberName(), 20)
                    + Format.pad(r.getDueDate().toString(), 12)
                    + Format.pad(String.valueOf(r.getDaysLate()), 11)
                    + Format.pad(Format.currency(r.getFineSoFar()), 12));
        }
    }

    private void handleViewMemberLoans() {
        String memberId = io.readNonEmpty("Enter member ID: ", "Member ID");
        CustomLinkedList<Loan> loans = circulationService.getMemberLoans(memberId);
        if (loans.isEmpty()) {
            System.out.println("[INFO] No active loans for member " + memberId + ".");
            return;
        }
        System.out.println(Format.pad("Book ID", 10) + Format.pad("Issue Date", 14)
                + Format.pad("Due Date", 14));
        System.out.println(Format.divider(38));
        for (Loan l : loans) {
            System.out.println(Format.pad(l.getBookId(), 10)
                    + Format.pad(l.getIssueDate().toString(), 14)
                    + Format.pad(l.getDueDate().toString(), 14));
        }
    }
}
