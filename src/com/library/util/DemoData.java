package com.library.util;

import com.library.ds.CustomLinkedList;
import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.Member;
import java.time.LocalDate;

/**
 * Factory for initial demo dataset containing 8 books, 4 members, and 2 loans.
 */
public final class DemoData {

    private DemoData() {
        // Prevent instantiation
    }

    /**
     * Constructs and returns the 8 predefined demo books.
     * Complexity: O(1)
     *
     * @return list of demo books
     */
    public static CustomLinkedList<Book> createDemoBooks() {
        CustomLinkedList<Book> books = new CustomLinkedList<>();
        books.addLast(new Book("B001", "978-0132350884", "Clean Code", "Robert C. Martin", 3));
        books.addLast(new Book("B002", "978-0262033848", "Introduction to Algorithms", "Thomas H. Cormen", 2));
        books.addLast(new Book("B003", "978-0134685991", "Effective Java", "Joshua Bloch", 4));
        books.addLast(new Book("B004", "978-0201633610", "Design Patterns", "Erich Gamma", 2));
        addMoreBooks(books);
        return books;
    }

    private static void addMoreBooks(CustomLinkedList<Book> books) {
        books.addLast(new Book("B005", "978-0262510875", "SICP", "Harold Abelson", 1));
        books.addLast(new Book("B006", "978-0136042594", "AI: A Modern Approach", "Stuart Russell", 3));
        books.addLast(new Book("B007", "978-1118063330", "Operating System Concepts", "Abraham Silberschatz", 2));
        books.addLast(new Book("B008", "978-0133594140", "Computer Networking", "James Kurose", 3));
    }

    /**
     * Constructs and returns the 4 predefined demo members.
     * Complexity: O(1)
     *
     * @return list of demo members
     */
    public static CustomLinkedList<Member> createDemoMembers() {
        CustomLinkedList<Member> members = new CustomLinkedList<>();
        members.addLast(new Member("M001", "Alice Johnson", "alice@example.com", "9876543210"));
        members.addLast(new Member("M002", "Bob Smith", "bob@example.com", "9876543211"));
        members.addLast(new Member("M003", "Charlie Brown", "charlie@example.com", "9876543212"));
        members.addLast(new Member("M004", "Diana Prince", "diana@example.com", "9876543213"));
        return members;
    }

    /**
     * Constructs and returns the 2 predefined demo loans based on the reference date.
     * Complexity: O(1)
     *
     * @param simDate current simulation date
     * @return list of demo loans
     */
    public static CustomLinkedList<Loan> createDemoLoans(LocalDate simDate) {
        CustomLinkedList<Loan> loans = new CustomLinkedList<>();
        LocalDate issueDate = simDate != null ? simDate : LocalDate.of(2026, 10, 26);
        LocalDate dueDate = issueDate.plusDays(Constants.LOAN_PERIOD_DAYS);
        loans.addLast(new Loan("B001", "M001", issueDate, dueDate));
        loans.addLast(new Loan("B003", "M002", issueDate, dueDate));
        return loans;
    }
}
