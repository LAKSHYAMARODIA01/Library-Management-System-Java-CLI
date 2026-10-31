package com.library.util;

/**
 * Validation utilities for user input and domain constraints.
 */
public final class Validator {

    private Validator() {
        // Prevent instantiation
    }

    /**
     * Validates that the given string is neither null nor blank.
     * Complexity: O(n)
     *
     * @param value     string value to inspect
     * @param fieldName name of the field for error reporting
     * @throws LibraryException if the value is null or blank
     */
    public static void validateNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new LibraryException(fieldName + " cannot be empty.");
        }
    }

    /**
     * Validates email address format containing '@' and '.' characters.
     * Complexity: O(n)
     *
     * @param email email string to validate
     * @throws LibraryException if email format is invalid
     */
    public static void validateEmail(String email) {
        validateNonEmpty(email, "Email");
        String trimmed = email.trim();
        int atIndex = trimmed.indexOf('@');
        int dotIndex = trimmed.lastIndexOf('.');
        if (atIndex <= 0 || dotIndex <= atIndex + 1 || dotIndex >= trimmed.length() - 1) {
            throw new LibraryException("Invalid email format. Must contain '@' and '.'");
        }
    }

    /**
     * Validates phone number to ensure exactly 10 numeric digits.
     * Complexity: O(n)
     *
     * @param phone phone number string to validate
     * @throws LibraryException if phone does not match 10 digits
     */
    public static void validatePhone(String phone) {
        validateNonEmpty(phone, "Phone");
        String trimmed = phone.trim();
        if (trimmed.length() != 10) {
            throw new LibraryException("Phone number must be exactly 10 digits.");
        }
        for (int i = 0; i < 10; i++) {
            if (!Character.isDigit(trimmed.charAt(i))) {
                throw new LibraryException("Phone number must contain only numeric digits.");
            }
        }
    }

    /**
     * Validates that book copies fall within the permitted range [1, 100].
     * Complexity: O(1)
     *
     * @param copies number of copies
     * @throws LibraryException if copies is out of range
     */
    public static void validateCopies(int copies) {
        if (copies < Constants.MIN_COPIES || copies > Constants.MAX_COPIES) {
            throw new LibraryException("Copies must be between " + Constants.MIN_COPIES
                    + " and " + Constants.MAX_COPIES + ".");
        }
    }

    /**
     * Validates that an integer falls within the given inclusive bounds.
     * Complexity: O(1)
     *
     * @param val       integer value
     * @param min       minimum allowable value
     * @param max       maximum allowable value
     * @param fieldName name of the field
     * @throws LibraryException if out of bounds
     */
    public static void validateIntRange(int val, int min, int max, String fieldName) {
        if (val < min || val > max) {
            throw new LibraryException(fieldName + " must be between " + min + " and " + max + ".");
        }
    }
}
