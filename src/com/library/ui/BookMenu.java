package com.library.ui;

import com.library.ds.CustomLinkedList;
import com.library.model.Book;
import com.library.service.BookService;
import com.library.util.Constants;
import com.library.util.LibraryException;

/**
 * Menu screen handling book management operations (FR-B1 to FR-B8).
 */
public class BookMenu {

    private final BookService bookService;
    private final ConsoleIO io;

    /**
     * Constructs a BookMenu instance.
     * Complexity: O(1)
     *
     * @param bookService book service
     * @param io          console IO helper
     */
    public BookMenu(BookService bookService, ConsoleIO io) {
        this.bookService = bookService;
        this.io = io;
    }

    /**
     * Displays the book management menu loop.
     * Complexity: O(n)
     */
    public void display() {
        while (true) {
            System.out.println("--- Book Management ---");
            System.out.println(" 1. Add New Book");
            System.out.println(" 2. Add Copies to Book");
            System.out.println(" 3. Update Book Details");
            System.out.println(" 4. Remove Book");
            System.out.println(" 5. Search Book by ID");
            System.out.println(" 6. Search Book by ISBN");
            System.out.println(" 7. Search Books by Keyword");
            System.out.println(" 8. List All Books");
            System.out.println(" 0. Back");
            int choice = io.readIntInRange("Enter choice: ", 0, 8);
            if (choice == 0) return;
            handleChoice(choice);
            System.out.println();
        }
    }

    private void handleChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> handleAddBook();
                case 2 -> handleAddCopies();
                case 3 -> handleUpdateBook();
                case 4 -> handleRemoveBook();
                case 5 -> handleFindById();
                case 6 -> handleFindByIsbn();
                case 7 -> handleSearchKeyword();
                case 8 -> handleListAll();
            }
        } catch (LibraryException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void handleAddBook() {
        String title = io.readNonEmpty("Enter title: ", "Title");
        String author = io.readNonEmpty("Enter author: ", "Author");
        String isbn = io.readNonEmpty("Enter ISBN: ", "ISBN");
        int copies = io.readIntInRange("Enter copies (" + Constants.MIN_COPIES + "-"
                + Constants.MAX_COPIES + "): ", Constants.MIN_COPIES, Constants.MAX_COPIES);
        Book book = bookService.addBook(title, author, isbn, copies);
        System.out.println("[OK] Book added successfully: " + book.getId());
    }

    private void handleAddCopies() {
        String id = io.readNonEmpty("Enter book ID: ", "Book ID");
        int copies = io.readInt("Enter copies to add: ");
        bookService.addCopies(id, copies);
        System.out.println("[OK] Copies added successfully.");
    }

    private void handleUpdateBook() {
        String id = io.readNonEmpty("Enter book ID: ", "Book ID");
        String title = io.readLine("Enter new title (blank to skip): ");
        String author = io.readLine("Enter new author (blank to skip): ");
        bookService.updateBook(id, title, author);
        System.out.println("[OK] Book updated successfully.");
    }

    private void handleRemoveBook() {
        String id = io.readNonEmpty("Enter book ID to remove: ", "Book ID");
        bookService.removeBook(id);
        System.out.println("[OK] Book " + id + " removed successfully.");
    }

    private void handleFindById() {
        String id = io.readNonEmpty("Enter book ID: ", "Book ID");
        Book book = bookService.findById(id);
        printBookDetail(book);
    }

    private void handleFindByIsbn() {
        String isbn = io.readNonEmpty("Enter ISBN: ", "ISBN");
        Book book = bookService.findByIsbn(isbn);
        printBookDetail(book);
    }

    private void handleSearchKeyword() {
        String kw = io.readNonEmpty("Enter search keyword: ", "Keyword");
        CustomLinkedList<Book> books = bookService.searchByKeyword(kw);
        printBookTable(books);
    }

    private void handleListAll() {
        CustomLinkedList<Book> books = bookService.getAllBooks();
        printBookTable(books);
    }

    private void printBookDetail(Book b) {
        System.out.println("[OK] Found Book:");
        System.out.println("  ID:        " + b.getId());
        System.out.println("  Title:     " + b.getTitle());
        System.out.println("  Author:    " + b.getAuthor());
        System.out.println("  ISBN:      " + b.getIsbn());
        System.out.println("  Available: " + b.getAvailableCopies() + " / " + b.getTotalCopies());
        System.out.println("  Reserved:  " + b.getReservations().size());
        System.out.println("  Borrows:   " + b.getBorrowCount());
    }

    private void printBookTable(CustomLinkedList<Book> books) {
        if (books.isEmpty()) {
            System.out.println("[INFO] No books found.");
            return;
        }
        System.out.println(Format.pad("ID", 7) + Format.pad("Title", 28) + Format.pad("Author", 20)
                + Format.pad("ISBN", 16) + Format.pad("Avail", 8) + Format.pad("Total", 8)
                + Format.pad("Reserved", 9));
        System.out.println(Format.divider(96));
        for (Book b : books) {
            System.out.println(Format.pad(b.getId(), 7)
                    + Format.pad(b.getTitle(), 28)
                    + Format.pad(b.getAuthor(), 20)
                    + Format.pad(b.getIsbn(), 16)
                    + Format.pad(String.valueOf(b.getAvailableCopies()), 8)
                    + Format.pad(String.valueOf(b.getTotalCopies()), 8)
                    + Format.pad(String.valueOf(b.getReservations().size()), 9));
        }
    }
}
