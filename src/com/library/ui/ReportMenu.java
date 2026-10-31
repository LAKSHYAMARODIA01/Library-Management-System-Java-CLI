package com.library.ui;

import com.library.ds.CustomLinkedList;
import com.library.model.Book;
import com.library.model.Transaction;
import com.library.service.ReportService;
import com.library.util.LibraryException;

/**
 * Menu screen handling report generation and audit history inspection (FR-H1 to FR-H4).
 */
public class ReportMenu {

    private final ReportService reportService;
    private final ConsoleIO io;

    /**
     * Constructs a ReportMenu instance.
     * Complexity: O(1)
     *
     * @param reportService report service
     * @param io            console IO helper
     */
    public ReportMenu(ReportService reportService, ConsoleIO io) {
        this.reportService = reportService;
        this.io = io;
    }

    /**
     * Displays the reports and history menu loop.
     * Complexity: O(n)
     */
    public void display() {
        while (true) {
            System.out.println("--- Reports and History ---");
            System.out.println(" 1. Full Transaction History");
            System.out.println(" 2. Transaction History by Member");
            System.out.println(" 3. Transaction History by Book");
            System.out.println(" 4. Top 3 Most Borrowed Books");
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
                case 1 -> printTransactions(reportService.getAllTransactions(), "Full Transaction History");
                case 2 -> {
                    String mid = io.readNonEmpty("Enter member ID: ", "Member ID");
                    printTransactions(reportService.getTransactionsByMember(mid), "History for Member " + mid);
                }
                case 3 -> {
                    String bid = io.readNonEmpty("Enter book ID: ", "Book ID");
                    printTransactions(reportService.getTransactionsByBook(bid), "History for Book " + bid);
                }
                case 4 -> handleTop3();
            }
        } catch (LibraryException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void handleTop3() {
        CustomLinkedList<Book> top3 = reportService.getTop3MostBorrowedBooks();
        if (top3.isEmpty()) {
            System.out.println("[INFO] No books in catalog.");
            return;
        }
        System.out.println("=== Top Most Borrowed Books ===");
        int rank = 1;
        for (Book b : top3) {
            System.out.println(" " + rank + ". [" + b.getId() + "] " + b.getTitle()
                    + " by " + b.getAuthor() + " - Borrowed " + b.getBorrowCount() + " time(s)");
            rank++;
        }
    }

    private void printTransactions(CustomLinkedList<Transaction> txs, String header) {
        if (txs.isEmpty()) {
            System.out.println("[INFO] No transactions found.");
            return;
        }
        System.out.println("=== " + header + " (Total: " + txs.size() + ") ===");
        System.out.println(Format.pad("Tx ID", 8) + Format.pad("Type", 16) + Format.pad("Book", 8)
                + Format.pad("Member", 9) + Format.pad("Date", 12) + Format.pad("Amount", 10)
                + Format.pad("Note", 32));
        System.out.println(Format.divider(95));
        for (Transaction tx : txs) {
            System.out.println(Format.pad(tx.getId(), 8)
                    + Format.pad(tx.getType().name(), 16)
                    + Format.pad(tx.getBookId(), 8)
                    + Format.pad(tx.getMemberId(), 9)
                    + Format.pad(tx.getDate().toString(), 12)
                    + Format.pad(tx.getAmount() > 0 ? Format.currency(tx.getAmount()) : "-", 10)
                    + Format.pad(tx.getNote(), 32));
        }
    }
}
