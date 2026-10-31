package com.library.test;

import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.Member;
import com.library.persistence.CsvStorage;
import com.library.service.BookService;
import com.library.service.CirculationService;
import com.library.service.LibraryContext;
import com.library.service.ReservationService;
import com.library.service.ReturnResult;
import com.library.service.UndoService;
import com.library.util.LibraryException;
import java.io.File;

/**
 * Self-test suite verifying business logic and circulation services against SRS section 11.2 scenarios 7 to 13.
 */
public class ServiceSelfTest {

    private static int totalChecks = 0;
    private static int failedChecks = 0;

    /**
     * Helper to verify a condition and report PASS or FAIL.
     * Complexity: O(1)
     *
     * @param name      the name of the check
     * @param condition true if passed, false if failed
     */
    public static void check(String name, boolean condition) {
        totalChecks++;
        if (condition) {
            System.out.println("PASS: " + name);
        } else {
            System.out.println("FAIL: " + name);
            failedChecks++;
        }
    }

    /**
     * Executes all service test scenarios and exits with non-zero code on failure.
     * Complexity: O(n)
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== Starting Service Self-Tests ===");
        testScenario7();
        testScenario8();
        testScenario9();
        testScenario10();
        testScenario11();
        testScenario12();
        testScenario13();
        testScenario14Persistence();

        System.out.println("------------------------------------");
        System.out.println("Service Self-Test Summary: " + (totalChecks - failedChecks)
                + " / " + totalChecks + " checks passed.");
        if (failedChecks > 0) {
            System.out.println("FAILURE: " + failedChecks + " test(s) failed.");
            System.exit(1);
        } else {
            System.out.println("ALL SERVICE TESTS PASSED SUCCESSFULLY.");
        }
    }

    private static void testScenario7() {
        LibraryContext ctx = new LibraryContext();
        BookService bs = new BookService(ctx);
        CirculationService cs = new CirculationService(ctx);
        Book b = bs.addBook("Test Book", "Author", "ISBN-001", 2);
        Member m = new Member("M001", "Member 1", "m1@test.com", "1234567890");
        ctx.getMembersById().put(m.getId(), m);

        Loan loan = cs.issueBook(b.getId(), m.getId());
        ReturnResult res = cs.returnBook(b.getId(), m.getId());
        check("Scenario 7: Issue and return on time, fine is 0", res.getFine() == 0.0);
        check("Scenario 7: Available copies restored", b.getAvailableCopies() == 2);
        check("Scenario 7: Member loans cleared", m.getLoans().isEmpty());
    }

    private static void testScenario8() {
        LibraryContext ctx = new LibraryContext();
        BookService bs = new BookService(ctx);
        CirculationService cs = new CirculationService(ctx);
        Book b = bs.addBook("Late Book", "Author", "ISBN-002", 1);
        Member m = new Member("M001", "Member 1", "m1@test.com", "1234567890");
        ctx.getMembersById().put(m.getId(), m);

        cs.issueBook(b.getId(), m.getId());
        ctx.getClockService().advanceDays(19);
        ReturnResult res = cs.returnBook(b.getId(), m.getId());
        check("Scenario 8: Return 5 days late yields Rs 10 fine", res.getFine() == 10.0);
        check("Scenario 8: Member fines collected updated", m.getFinesCollected() == 10.0);
    }

    private static void testScenario9() {
        LibraryContext ctx = new LibraryContext();
        BookService bs = new BookService(ctx);
        CirculationService cs = new CirculationService(ctx);
        Member m = new Member("M001", "Member 1", "m1@test.com", "1234567890");
        ctx.getMembersById().put(m.getId(), m);

        Book b1 = bs.addBook("Book 1", "Author", "ISBN-B1", 1);
        Book b2 = bs.addBook("Book 2", "Author", "ISBN-B2", 1);
        Book b3 = bs.addBook("Book 3", "Author", "ISBN-B3", 1);
        Book b4 = bs.addBook("Book 4", "Author", "ISBN-B4", 1);

        cs.issueBook(b1.getId(), m.getId());
        cs.issueBook(b2.getId(), m.getId());
        cs.issueBook(b3.getId(), m.getId());

        boolean rejected = false;
        try {
            cs.issueBook(b4.getId(), m.getId());
        } catch (LibraryException e) {
            rejected = true;
        }
        check("Scenario 9: 4th loan rejected at limit of 3", rejected);
    }

    private static void testScenario10() {
        LibraryContext ctx = new LibraryContext();
        BookService bs = new BookService(ctx);
        CirculationService cs = new CirculationService(ctx);
        ReservationService rs = new ReservationService(ctx);

        Book b = bs.addBook("Single Copy Book", "Author", "ISBN-SCB", 1);
        Member mA = new Member("MA", "Member A", "a@test.com", "1234567890");
        Member mB = new Member("MB", "Member B", "b@test.com", "1234567891");
        Member mC = new Member("MC", "Member C", "c@test.com", "1234567892");
        ctx.getMembersById().put(mA.getId(), mA);
        ctx.getMembersById().put(mB.getId(), mB);
        ctx.getMembersById().put(mC.getId(), mC);

        cs.issueBook(b.getId(), mA.getId());
        int pos = rs.reserveBook(b.getId(), mB.getId());
        check("Scenario 10: Member B reserved at position 1", pos == 1);

        cs.returnBook(b.getId(), mA.getId());

        boolean cRejected = false;
        try {
            cs.issueBook(b.getId(), mC.getId());
        } catch (LibraryException e) {
            cRejected = true;
        }
        check("Scenario 10: Member C rejected when B is at front of queue", cRejected);

        Loan bLoan = cs.issueBook(b.getId(), mB.getId());
        check("Scenario 10: Member B successfully issued book", bLoan != null);
    }

    private static void testScenario11() {
        LibraryContext ctx = new LibraryContext();
        BookService bs = new BookService(ctx);
        CirculationService cs = new CirculationService(ctx);
        ReservationService rs = new ReservationService(ctx);
        UndoService us = new UndoService(ctx);

        Book b = bs.addBook("Queue Undo Book", "Author", "ISBN-QUB", 1);
        Member mA = new Member("MA", "Member A", "a@test.com", "1234567890");
        Member mB = new Member("MB", "Member B", "b@test.com", "1234567891");
        ctx.getMembersById().put(mA.getId(), mA);
        ctx.getMembersById().put(mB.getId(), mB);

        cs.issueBook(b.getId(), mA.getId());
        rs.reserveBook(b.getId(), mB.getId());
        cs.returnBook(b.getId(), mA.getId());

        cs.issueBook(b.getId(), mB.getId());
        check("Scenario 11: Queue empty after issue to B", b.getReservations().isEmpty());

        us.undoLastAction();
        check("Scenario 11: Undo of issue restores B to front of queue",
                !b.getReservations().isEmpty() && "MB".equals(b.getReservations().peek()));
        check("Scenario 11: Available copy restored to 1", b.getAvailableCopies() == 1);
        check("Scenario 11: Member B loan removed", mB.getLoans().isEmpty());
    }

    private static void testScenario12() {
        LibraryContext ctx = new LibraryContext();
        BookService bs = new BookService(ctx);
        CirculationService cs = new CirculationService(ctx);
        UndoService us = new UndoService(ctx);

        Book b = bs.addBook("Return Undo Book", "Author", "ISBN-RUB", 1);
        Member m = new Member("M001", "Member 1", "m1@test.com", "1234567890");
        ctx.getMembersById().put(m.getId(), m);

        Loan loan = cs.issueBook(b.getId(), m.getId());
        ctx.getClockService().advanceDays(19);
        cs.returnBook(b.getId(), m.getId());
        check("Scenario 12: Fine recorded prior to undo", m.getFinesCollected() == 10.0);

        us.undoLastAction();
        check("Scenario 12: Undo of return restores loan", m.getLoans().size() == 1);
        check("Scenario 12: Restored loan has same due date",
                m.getLoans().get(0).getDueDate().equals(loan.getDueDate()));
        check("Scenario 12: Available copy decreased back to 0", b.getAvailableCopies() == 0);
        check("Scenario 12: Fine deducted back to 0", m.getFinesCollected() == 0.0);
    }

    private static void testScenario13() {
        LibraryContext ctx = new LibraryContext();
        BookService bs = new BookService(ctx);
        CirculationService cs = new CirculationService(ctx);

        Book b = bs.addBook("Cannot Remove Book", "Author", "ISBN-CRB", 1);
        Member m = new Member("M001", "Member 1", "m1@test.com", "1234567890");
        ctx.getMembersById().put(m.getId(), m);

        cs.issueBook(b.getId(), m.getId());

        boolean rejected = false;
        try {
            bs.removeBook(b.getId());
        } catch (LibraryException e) {
            rejected = true;
        }
        check("Scenario 13: Remove book with active loan rejected (BR-8)", rejected);
    }

    private static void testScenario14Persistence() {
        LibraryContext ctx = new LibraryContext();
        BookService bs = new BookService(ctx);
        CirculationService cs = new CirculationService(ctx);
        Book b = bs.addBook("Persistent Book", "Writer", "ISBN-P1", 2);
        Member m = new Member("M001", "Member P", "p@test.com", "1234567890");
        ctx.getMembersById().put(m.getId(), m);
        cs.issueBook(b.getId(), m.getId());

        String testDir = "data_test";
        CsvStorage storage = new CsvStorage();
        storage.save(ctx, testDir);

        LibraryContext loadedCtx = new LibraryContext();
        storage.load(loadedCtx, testDir);

        check("Scenario 14: CSV round-trip book restored", loadedCtx.getBooksById().containsKey("B001"));
        check("Scenario 14: CSV round-trip member restored", loadedCtx.getMembersById().containsKey("M001"));
        check("Scenario 14: CSV round-trip loan restored", loadedCtx.getMembersById().get("M001").getLoans().size() == 1);
        check("Scenario 14: New book ID increments past loaded", "B002".equals(loadedCtx.getIdGenerator().nextBookId()));

        deleteTestDir(new File(testDir));
        boolean missingFailed = false;
        try {
            storage.load(loadedCtx, "non_existent_dir");
        } catch (LibraryException e) {
            missingFailed = true;
        }
        check("Scenario 14: Loading missing directory fails cleanly", missingFailed);
    }

    private static void deleteTestDir(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) f.delete();
        }
        dir.delete();
    }
}
