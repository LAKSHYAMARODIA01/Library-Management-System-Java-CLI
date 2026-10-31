package com.library.util;

/**
 * Global constant values used across the library management system.
 */
public final class Constants {

    private Constants() {
        // Prevent instantiation
    }

    /** Loan duration in days (BR-1). */
    public static final int LOAN_PERIOD_DAYS = 14;

    /** Overdue fine rate per day in Rupees (BR-2). */
    public static final double FINE_RATE_PER_DAY = 2.0;

    /** Maximum concurrent active loans per member (BR-3). */
    public static final int MAX_LOANS_PER_MEMBER = 3;

    /** Minimum allowable copies for a book (FR-B1). */
    public static final int MIN_COPIES = 1;

    /** Maximum allowable copies for a book (FR-B1). */
    public static final int MAX_COPIES = 100;

    /** Initial capacity for CustomHashMap. */
    public static final int INITIAL_HASH_CAPACITY = 16;

    /** Load factor threshold triggering CustomHashMap resizing. */
    public static final double HASH_LOAD_FACTOR_THRESHOLD = 0.75;

    /** Initial capacity for CustomStack. */
    public static final int INITIAL_STACK_CAPACITY = 8;
}
