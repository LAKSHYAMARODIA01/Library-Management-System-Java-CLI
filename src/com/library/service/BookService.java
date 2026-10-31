package com.library.service;

import com.library.ds.CustomLinkedList;
import com.library.model.Action;
import com.library.model.Book;
import com.library.model.Transaction;
import com.library.model.TransactionType;
import com.library.util.LibraryException;
import com.library.util.Validator;

/**
 * Service managing book catalog operations, search, and inventory.
 */
public class BookService {

    private final LibraryContext context;

    /**
     * Constructs a BookService with the given library context.
     * Complexity: O(1)
     *
     * @param context shared library context
     */
    public BookService(LibraryContext context) {
        this.context = context;
    }

    /**
     * Adds a new book to the catalog (FR-B1), enforcing unique ISBN (BR-10).
     * Complexity: O(1) average
     *
     * @param title  book title
     * @param author author name
     * @param isbn   unique ISBN
     * @param copies number of copies (1-100)
     * @return created Book instance
     */
    public Book addBook(String title, String author, String isbn, int copies) {
        Validator.validateNonEmpty(title, "Title");
        Validator.validateNonEmpty(author, "Author");
        Validator.validateNonEmpty(isbn, "ISBN");
        Validator.validateCopies(copies);

        if (context.getBooksByIsbn().containsKey(isbn)) {
            throw new LibraryException("Book with ISBN " + isbn + " already exists (BR-10).");
        }

        String bookId = context.getIdGenerator().nextBookId();
        Book book = new Book(bookId, isbn, title, author, copies);
        context.getBooksById().put(bookId, book);
        context.getBooksByIsbn().put(isbn, book);
        context.getCatalog().addLast(book);

        String txId = context.getIdGenerator().nextTransactionId();
        Transaction tx = new Transaction(txId, TransactionType.ADD_BOOK, bookId, "",
                context.getClockService().getSimDate(), 0.0, "Added book: " + title);
        context.getTransactionHistory().addLast(tx);

        Action action = new Action(TransactionType.ADD_BOOK, bookId, "", null, null, 0.0, false, txId);
        context.getUndoStack().push(action);

        return book;
    }

    /**
     * Adds additional copies to an existing book (FR-B2).
     * Complexity: O(1) average
     *
     * @param bookId identifier of the book
     * @param copies count of copies to add
     */
    public void addCopies(String bookId, int copies) {
        Validator.validateNonEmpty(bookId, "Book ID");
        if (copies <= 0) {
            throw new LibraryException("Copies to add must be positive.");
        }
        Book book = findById(bookId);
        int newTotal = book.getTotalCopies() + copies;
        Validator.validateCopies(newTotal);
        book.setTotalCopies(newTotal);
        book.setAvailableCopies(book.getAvailableCopies() + copies);
    }

    /**
     * Updates the title and/or author of an existing book (FR-B3).
     * Complexity: O(1) average
     *
     * @param bookId identifier of the book
     * @param title  new title (or null to keep unchanged)
     * @param author new author (or null to keep unchanged)
     */
    public void updateBook(String bookId, String title, String author) {
        Book book = findById(bookId);
        if (title != null && !title.trim().isEmpty()) {
            book.setTitle(title.trim());
        }
        if (author != null && !author.trim().isEmpty()) {
            book.setAuthor(author.trim());
        }
    }

    /**
     * Removes a book from the catalog (FR-B4), enforcing BR-8.
     * Complexity: O(n)
     *
     * @param bookId identifier of the book
     */
    public void removeBook(String bookId) {
        Book book = findById(bookId);
        if (book.getTotalCopies() != book.getAvailableCopies()) {
            throw new LibraryException("Cannot remove book " + bookId
                    + ": copies are currently on loan (BR-8).");
        }
        if (!book.getReservations().isEmpty()) {
            throw new LibraryException("Cannot remove book " + bookId
                    + ": reservation queue is not empty (BR-8).");
        }
        context.getBooksById().remove(book.getId());
        context.getBooksByIsbn().remove(book.getIsbn());
        context.getCatalog().remove(book);
    }

    /**
     * Searches for a book by its identifier (FR-B5).
     * Complexity: O(1) average
     *
     * @param bookId book identifier
     * @return matching Book instance
     */
    public Book findById(String bookId) {
        Validator.validateNonEmpty(bookId, "Book ID");
        Book book = context.getBooksById().get(bookId);
        if (book == null) {
            throw new LibraryException("Book " + bookId + " not found.");
        }
        return book;
    }

    /**
     * Searches for a book by its ISBN (FR-B6).
     * Complexity: O(1) average
     *
     * @param isbn book ISBN
     * @return matching Book instance
     */
    public Book findByIsbn(String isbn) {
        Validator.validateNonEmpty(isbn, "ISBN");
        Book book = context.getBooksByIsbn().get(isbn);
        if (book == null) {
            throw new LibraryException("Book with ISBN " + isbn + " not found.");
        }
        return book;
    }

    /**
     * Searches books by title or author containing keyword (FR-B7).
     * Complexity: O(n)
     *
     * @param keyword search term
     * @return list of matching books
     */
    public CustomLinkedList<Book> searchByKeyword(String keyword) {
        Validator.validateNonEmpty(keyword, "Keyword");
        String lowerKeyword = keyword.toLowerCase();
        CustomLinkedList<Book> matches = new CustomLinkedList<>();
        for (Book book : context.getCatalog()) {
            if (book.getTitle().toLowerCase().contains(lowerKeyword)
                    || book.getAuthor().toLowerCase().contains(lowerKeyword)) {
                matches.addLast(book);
            }
        }
        return matches;
    }

    /**
     * Returns all books in catalog insertion order (FR-B8).
     * Complexity: O(1)
     *
     * @return catalog linked list
     */
    public CustomLinkedList<Book> getAllBooks() {
        return context.getCatalog();
    }
}
