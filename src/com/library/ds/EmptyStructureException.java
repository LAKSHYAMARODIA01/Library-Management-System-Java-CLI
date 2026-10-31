package com.library.ds;

/**
 * Unchecked exception thrown when an operation is performed on an empty data structure.
 */
public class EmptyStructureException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new EmptyStructureException with the specified detail message.
     * Complexity: O(1)
     *
     * @param message the detail message
     */
    public EmptyStructureException(String message) {
        super(message);
    }
}
