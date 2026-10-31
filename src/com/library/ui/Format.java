package com.library.ui;

/**
 * Text formatting, padding, and tabular display helpers for console output.
 */
public final class Format {

    private Format() {
        // Prevent instantiation
    }

    /**
     * Pads a string with trailing spaces to reach the specified width.
     * Complexity: O(width)
     *
     * @param text  input string
     * @param width minimum width
     * @return padded string
     */
    public static String pad(String text, int width) {
        String s = text != null ? text : "";
        if (s.length() >= width) {
            return s.length() > width && width > 3 ? s.substring(0, width - 3) + "..." : s;
        }
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < width) {
            sb.append(' ');
        }
        return sb.toString();
    }

    /**
     * Pads a string with leading spaces to reach the specified width.
     * Complexity: O(width)
     *
     * @param text  input string
     * @param width minimum width
     * @return padded string
     */
    public static String padLeft(String text, int width) {
        String s = text != null ? text : "";
        if (s.length() >= width) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() + s.length() < width) {
            sb.append(' ');
        }
        sb.append(s);
        return sb.toString();
    }

    /**
     * Creates a horizontal divider line of the specified length.
     * Complexity: O(length)
     *
     * @param length character length
     * @return divider string
     */
    public static String divider(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append('-');
        }
        return sb.toString();
    }

    /**
     * Formats a monetary amount into Rupees representation.
     * Complexity: O(1)
     *
     * @param amount currency amount
     * @return formatted currency string
     */
    public static String currency(double amount) {
        return "Rs " + String.format("%.0f", amount);
    }
}
