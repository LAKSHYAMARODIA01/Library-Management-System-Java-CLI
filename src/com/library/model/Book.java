package com.library.model;

import com.library.ds.CustomQueue;
import com.library.util.Validator;

/**
 * Domain model representing a book in the library catalog.
 */
public class Book {

    private final String id;
    private final String isbn;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;
    private int borrowCount;
    private final CustomQueue<String> reservations;

    /**
     * Constructs a new Book instance with validated parameters.
     * Complexity: O(1)
     *
     * @param id          unique book identifier (e.g. B001)
     * @param isbn        unique ISBN
     * @param title       book title
     * @param author      book author
     * @param totalCopies initial total copies
     */
    public Book(String id, String isbn, String title, String author, int totalCopies) {
        Validator.validateNonEmpty(id, "Book ID");
        Validator.validateNonEmpty(isbn, "ISBN");
        Validator.validateNonEmpty(title, "Title");
        Validator.validateNonEmpty(author, "Author");
        Validator.validateCopies(totalCopies);

        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.borrowCount = 0;
        this.reservations = new CustomQueue<>();
    }

    /**
     * Full constructor for restoring state during persistence or demo loading.
     * Complexity: O(1)
     *
     * @param id              unique book identifier
     * @param isbn            unique ISBN
     * @param title           book title
     * @param author          book author
     * @param totalCopies     total copies
     * @param availableCopies available copies
     * @param borrowCount     lifetime borrow count
     */
    public Book(String id, String isbn, String title, String author, int totalCopies,
                int availableCopies, int borrowCount) {
        Validator.validateNonEmpty(id, "Book ID");
        Validator.validateNonEmpty(isbn, "ISBN");
        Validator.validateNonEmpty(title, "Title");
        Validator.validateNonEmpty(author, "Author");
        Validator.validateCopies(totalCopies);

        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.borrowCount = borrowCount;
        this.reservations = new CustomQueue<>();
    }

    /**
     * Returns the book identifier.
     * Complexity: O(1)
     *
     * @return book ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the book ISBN.
     * Complexity: O(1)
     *
     * @return ISBN
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Returns the book title.
     * Complexity: O(1)
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Updates the book title.
     * Complexity: O(1)
     *
     * @param title new title
     */
    public void setTitle(String title) {
        Validator.validateNonEmpty(title, "Title");
        this.title = title;
    }

    /**
     * Returns the author of the book.
     * Complexity: O(1)
     *
     * @return author name
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Updates the author of the book.
     * Complexity: O(1)
     *
     * @param author new author name
     */
    public void setAuthor(String author) {
        Validator.validateNonEmpty(author, "Author");
        this.author = author;
    }

    /**
     * Returns the total number of copies owned.
     * Complexity: O(1)
     *
     * @return total copies
     */
    public int getTotalCopies() {
        return totalCopies;
    }

    /**
     * Updates the total copies count.
     * Complexity: O(1)
     *
     * @param totalCopies new total copies
     */
    public void setTotalCopies(int totalCopies) {
        Validator.validateCopies(totalCopies);
        this.totalCopies = totalCopies;
    }

    /**
     * Returns the number of currently available copies.
     * Complexity: O(1)
     *
     * @return available copies
     */
    public int getAvailableCopies() {
        return availableCopies;
    }

    /**
     * Updates the available copies count.
     * Complexity: O(1)
     *
     * @param availableCopies new available copies
     */
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    /**
     * Returns the lifetime borrow count.
     * Complexity: O(1)
     *
     * @return borrow count
     */
    public int getBorrowCount() {
        return borrowCount;
    }

    /**
     * Updates the lifetime borrow count.
     * Complexity: O(1)
     *
     * @param borrowCount new borrow count
     */
    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;
    }

    /**
     * Increments the lifetime borrow count by one.
     * Complexity: O(1)
     */
    public void incrementBorrowCount() {
        this.borrowCount++;
    }

    /**
     * Decrements the lifetime borrow count by one (e.g. for undo).
     * Complexity: O(1)
     */
    public void decrementBorrowCount() {
        if (this.borrowCount > 0) {
            this.borrowCount--;
        }
    }

    /**
     * Returns the reservation queue of member IDs.
     * Complexity: O(1)
     *
     * @return reservation queue
     */
    public CustomQueue<String> getReservations() {
        return reservations;
    }

    /**
     * Returns a string representation of the book.
     * Complexity: O(1)
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "[" + id + "] " + title + " by " + author + " (ISBN: " + isbn
                + ", Available: " + availableCopies + "/" + totalCopies
                + ", Reserved: " + reservations.size() + ")";
    }
}
