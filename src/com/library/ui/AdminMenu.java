package com.library.ui;

import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.Member;
import com.library.model.Transaction;
import com.library.model.TransactionType;
import com.library.persistence.CsvStorage;
import com.library.service.LibraryContext;
import com.library.util.DemoData;
import com.library.util.LibraryException;

/**
 * Menu screen handling administrative options, simulation clock, demo data, and persistence.
 */
public class AdminMenu {

    private static final String DATA_DIR = "data";
    private final LibraryContext context;
    private final ConsoleIO io;
    private final CsvStorage csvStorage;

    /**
     * Constructs an AdminMenu instance.
     * Complexity: O(1)
     *
     * @param context shared library context
     * @param io      console IO helper
     */
    public AdminMenu(LibraryContext context, ConsoleIO io) {
        this.context = context;
        this.io = io;
        this.csvStorage = new CsvStorage();
    }

    /**
     * Displays the administrative menu loop.
     * Complexity: O(n)
     */
    public void display() {
        while (true) {
            System.out.println("--- Admin Operations ---");
            System.out.println(" 1. Show Simulation Date");
            System.out.println(" 2. Advance Simulation Date");
            System.out.println(" 3. Load Demo Data (Allowed once)");
            System.out.println(" 4. Save Data to CSV");
            System.out.println(" 5. Load Data from CSV");
            System.out.println(" 0. Back");
            int choice = io.readIntInRange("Enter choice: ", 0, 5);
            if (choice == 0) return;
            handleChoice(choice);
            System.out.println();
        }
    }

    private void handleChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> System.out.println("[INFO] Current simulation date: "
                        + context.getClockService().getSimDate());
                case 2 -> handleAdvanceDate();
                case 3 -> handleLoadDemoData();
                case 4 -> handleSaveCsv();
                case 5 -> handleLoadCsv();
            }
        } catch (LibraryException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void handleAdvanceDate() {
        int days = io.readIntInRange("Enter days to advance (1 to 365): ", 1, 365);
        context.getClockService().advanceDays(days);
        System.out.println("[OK] Simulation date advanced to: "
                + context.getClockService().getSimDate());
    }

    private void handleLoadDemoData() {
        if (context.isDemoDataLoaded()) {
            throw new LibraryException("Demo data has already been loaded in this session (FR-A3).");
        }
        for (Book b : DemoData.createDemoBooks()) {
            context.getBooksById().put(b.getId(), b);
            context.getBooksByIsbn().put(b.getIsbn(), b);
            context.getCatalog().addLast(b);
        }
        for (Member m : DemoData.createDemoMembers()) {
            context.getMembersById().put(m.getId(), m);
        }
        for (Loan l : DemoData.createDemoLoans(context.getClockService().getSimDate())) {
            Member m = context.getMembersById().get(l.getMemberId());
            Book b = context.getBooksById().get(l.getBookId());
            if (m != null && b != null) {
                m.getLoans().addLast(l);
                b.setAvailableCopies(b.getAvailableCopies() - 1);
                b.incrementBorrowCount();
                String txId = context.getIdGenerator().nextTransactionId();
                context.getTransactionHistory().addLast(new Transaction(txId, TransactionType.ISSUE,
                        b.getId(), m.getId(), l.getIssueDate(), 0.0, "Demo initial loan"));
            }
        }
        context.getIdGenerator().ensureBookCounterAtLeast(9);
        context.getIdGenerator().ensureMemberCounterAtLeast(5);
        context.setDemoDataLoaded(true);
        System.out.println("[OK] Demo data loaded successfully (8 books, 4 members, 2 loans).");
    }

    private void handleSaveCsv() {
        csvStorage.save(context, DATA_DIR);
        System.out.println("[OK] Data successfully saved to " + DATA_DIR + "/ directory.");
    }

    private void handleLoadCsv() {
        csvStorage.load(context, DATA_DIR);
        System.out.println("[OK] Data successfully loaded from " + DATA_DIR + "/ directory.");
    }
}
