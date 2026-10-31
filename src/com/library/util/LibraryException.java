package com.library.util;

/**
 * Unchecked exception thrown for business rule violations and service layer validation failures.
 */
public class LibraryException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new LibraryException with the specified error message.
     * Complexity: O(1)
     *
     * @param message human-readable error description
     */
    public LibraryException(String message) {
        super(message);
    }
}
