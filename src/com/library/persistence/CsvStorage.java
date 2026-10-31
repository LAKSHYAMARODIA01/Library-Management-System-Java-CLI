package com.library.persistence;

import com.library.ds.CustomLinkedList;
import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.Member;
import com.library.model.Transaction;
import com.library.model.TransactionType;
import com.library.service.LibraryContext;
import com.library.util.LibraryException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Handles CSV persistence for books, members, loans, reservations, and transactions.
 */
public class CsvStorage {

    /**
     * Saves library context state to CSV files in the specified directory (FR-A4).
     * Complexity: O(total records)
     *
     * @param context library context
     * @param dirPath target directory path
     * @throws LibraryException if save fails
     */
    public void save(LibraryContext context, String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new LibraryException("Could not create directory: " + dirPath);
        }
        try {
            saveBooks(context, new File(dir, "books.csv"));
            saveMembers(context, new File(dir, "members.csv"));
            saveLoans(context, new File(dir, "loans.csv"));
            saveReservations(context, new File(dir, "reservations.csv"));
            saveTransactions(context, new File(dir, "transactions.csv"));
        } catch (IOException e) {
            throw new LibraryException("Failed to save data: " + e.getMessage());
        }
    }

    /**
     * Loads library context state from CSV files, replacing existing state atomically (FR-A5).
     * Complexity: O(total records)
     *
     * @param context library context
     * @param dirPath source directory path
     * @throws LibraryException if any file is missing or invalid
     */
    public void load(LibraryContext context, String dirPath) {
        File dir = new File(dirPath);
        File booksFile = new File(dir, "books.csv");
        File membersFile = new File(dir, "members.csv");
        File loansFile = new File(dir, "loans.csv");
        File resFile = new File(dir, "reservations.csv");
        File txFile = new File(dir, "transactions.csv");

        verifyFilesExist(booksFile, membersFile, loansFile, resFile, txFile);

        LibraryContext tempCtx = new LibraryContext();
        loadBooks(tempCtx, booksFile);
        loadMembers(tempCtx, membersFile);
        loadLoans(tempCtx, loansFile);
        loadReservations(tempCtx, resFile);
        loadTransactions(tempCtx, txFile);

        applyLoadedState(context, tempCtx);
    }

    private void verifyFilesExist(File... files) {
        for (File f : files) {
            if (!f.exists() || !f.isFile()) {
                throw new LibraryException("Required data file missing: " + f.getName());
            }
        }
    }

    private void saveBooks(LibraryContext ctx, File file) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("id,isbn,title,author,totalCopies,availableCopies,borrowCount");
            for (Book b : ctx.getCatalog()) {
                pw.println(b.getId() + "," + b.getIsbn() + ","
                        + CsvHelper.escape(b.getTitle()) + "," + CsvHelper.escape(b.getAuthor()) + ","
                        + b.getTotalCopies() + "," + b.getAvailableCopies() + ","
                        + b.getBorrowCount());
            }
        }
    }

    private void saveMembers(LibraryContext ctx, File file) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("id,name,email,phone,finesCollected");
            for (Member m : ctx.getMembersById().values()) {
                pw.println(m.getId() + "," + CsvHelper.escape(m.getName()) + ","
                        + CsvHelper.escape(m.getEmail()) + "," + CsvHelper.escape(m.getPhone()) + ","
                        + m.getFinesCollected());
            }
        }
    }

    private void saveLoans(LibraryContext ctx, File file) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("bookId,memberId,issueDate,dueDate");
            for (Member m : ctx.getMembersById().values()) {
                for (Loan loan : m.getLoans()) {
                    pw.println(loan.getBookId() + "," + loan.getMemberId() + ","
                            + loan.getIssueDate() + "," + loan.getDueDate());
                }
            }
        }
    }

    private void saveReservations(LibraryContext ctx, File file) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("bookId,memberId");
            for (Book b : ctx.getCatalog()) {
                for (String memberId : b.getReservations()) {
                    pw.println(b.getId() + "," + memberId);
                }
            }
        }
    }

    private void saveTransactions(LibraryContext ctx, File file) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("id,type,bookId,memberId,date,amount,note");
            for (Transaction tx : ctx.getTransactionHistory()) {
                pw.println(tx.getId() + "," + tx.getType().name() + ","
                        + tx.getBookId() + "," + tx.getMemberId() + ","
                        + tx.getDate() + "," + tx.getAmount() + "," + CsvHelper.escape(tx.getNote()));
            }
        }
    }

    private void loadBooks(LibraryContext ctx, File file) {
        try (Scanner sc = new Scanner(file)) {
            if (sc.hasNextLine()) sc.nextLine();
            int maxId = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                CustomLinkedList<String> f = CsvHelper.parseCsvLine(line);
                Book b = new Book(f.get(0), f.get(1), f.get(2), f.get(3),
                        Integer.parseInt(f.get(4)), Integer.parseInt(f.get(5)), Integer.parseInt(f.get(6)));
                ctx.getBooksById().put(b.getId(), b);
                ctx.getBooksByIsbn().put(b.getIsbn(), b);
                ctx.getCatalog().addLast(b);
                maxId = Math.max(maxId, CsvHelper.extractNumericId(b.getId()));
            }
            ctx.getIdGenerator().ensureBookCounterAtLeast(maxId + 1);
        } catch (Exception e) {
            throw new LibraryException("Corrupt books data: " + e.getMessage());
        }
    }

    private void loadMembers(LibraryContext ctx, File file) {
        try (Scanner sc = new Scanner(file)) {
            if (sc.hasNextLine()) sc.nextLine();
            int maxId = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                CustomLinkedList<String> f = CsvHelper.parseCsvLine(line);
                Member m = new Member(f.get(0), f.get(1), f.get(2), f.get(3), Double.parseDouble(f.get(4)));
                ctx.getMembersById().put(m.getId(), m);
                maxId = Math.max(maxId, CsvHelper.extractNumericId(m.getId()));
            }
            ctx.getIdGenerator().ensureMemberCounterAtLeast(maxId + 1);
        } catch (Exception e) {
            throw new LibraryException("Corrupt members data: " + e.getMessage());
        }
    }

    private void loadLoans(LibraryContext ctx, File file) {
        try (Scanner sc = new Scanner(file)) {
            if (sc.hasNextLine()) sc.nextLine();
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                CustomLinkedList<String> f = CsvHelper.parseCsvLine(line);
                Loan loan = new Loan(f.get(0), f.get(1), LocalDate.parse(f.get(2)), LocalDate.parse(f.get(3)));
                Member member = ctx.getMembersById().get(f.get(1));
                if (member != null) member.getLoans().addLast(loan);
            }
        } catch (Exception e) {
            throw new LibraryException("Corrupt loans data: " + e.getMessage());
        }
    }

    private void loadReservations(LibraryContext ctx, File file) {
        try (Scanner sc = new Scanner(file)) {
            if (sc.hasNextLine()) sc.nextLine();
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                CustomLinkedList<String> f = CsvHelper.parseCsvLine(line);
                Book book = ctx.getBooksById().get(f.get(0));
                if (book != null) book.getReservations().enqueue(f.get(1));
            }
        } catch (Exception e) {
            throw new LibraryException("Corrupt reservations data: " + e.getMessage());
        }
    }

    private void loadTransactions(LibraryContext ctx, File file) {
        try (Scanner sc = new Scanner(file)) {
            if (sc.hasNextLine()) sc.nextLine();
            int maxId = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                CustomLinkedList<String> f = CsvHelper.parseCsvLine(line);
                String note = f.size() > 6 ? f.get(6) : "";
                Transaction tx = new Transaction(f.get(0), TransactionType.valueOf(f.get(1)),
                        f.get(2), f.get(3), LocalDate.parse(f.get(4)), Double.parseDouble(f.get(5)), note);
                ctx.getTransactionHistory().addLast(tx);
                maxId = Math.max(maxId, CsvHelper.extractNumericId(f.get(0)));
            }
            ctx.getIdGenerator().ensureTransactionCounterAtLeast(maxId + 1);
        } catch (Exception e) {
            throw new LibraryException("Corrupt transactions data: " + e.getMessage());
        }
    }

    private void applyLoadedState(LibraryContext dest, LibraryContext src) {
        dest.getBooksById().clear();
        dest.getBooksByIsbn().clear();
        dest.getMembersById().clear();
        dest.getCatalog().clear();
        dest.getTransactionHistory().clear();
        while (!dest.getUndoStack().isEmpty()) dest.getUndoStack().pop();

        for (Book b : src.getCatalog()) {
            dest.getBooksById().put(b.getId(), b);
            dest.getBooksByIsbn().put(b.getIsbn(), b);
            dest.getCatalog().addLast(b);
        }
        for (Member m : src.getMembersById().values()) {
            dest.getMembersById().put(m.getId(), m);
        }
        for (Transaction tx : src.getTransactionHistory()) {
            dest.getTransactionHistory().addLast(tx);
        }
        dest.getIdGenerator().copyFrom(src.getIdGenerator());
    }
}
