package com.library.model;

/**
 * Enumeration of library operations and transaction events.
 */
public enum TransactionType {
    /** Adding a new book to the library catalog. */
    ADD_BOOK,

    /** Issuing a book copy to a library member. */
    ISSUE,

    /** Returning an issued book copy to the library. */
    RETURN,

    /** Reserving an unavailable book copy. */
    RESERVE,

    /** Cancelling an existing book reservation. */
    CANCEL_RESERVE
}
