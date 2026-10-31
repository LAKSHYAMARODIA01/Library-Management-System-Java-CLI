package com.library.persistence;

import com.library.ds.CustomLinkedList;

/**
 * Utility helper methods for CSV line tokenization and text escaping.
 */
public final class CsvHelper {

    private CsvHelper() {
        // Prevent instantiation
    }

    /**
     * Escapes text for CSV output, wrapping in quotes if commas or quotes are present.
     * Complexity: O(n)
     *
     * @param text input string
     * @return escaped CSV string
     */
    public static String escape(String text) {
        if (text == null) return "";
        if (text.contains(",") || text.contains("\"")) {
            return "\"" + text.replace("\"", "\"\"") + "\"";
        }
        return text;
    }

    /**
     * Parses a CSV line into an ordered CustomLinkedList of token values.
     * Complexity: O(n)
     *
     * @param line CSV line
     * @return list of parsed field values
     */
    public static CustomLinkedList<String> parseCsvLine(String line) {
        CustomLinkedList<String> tokens = new CustomLinkedList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                    sb.append('\"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                tokens.addLast(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.addLast(sb.toString());
        return tokens;
    }

    /**
     * Extracts all numeric digits from an ID string.
     * Complexity: O(n)
     *
     * @param id identifier string
     * @return parsed numeric value
     */
    public static int extractNumericId(String id) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < id.length(); i++) {
            if (Character.isDigit(id.charAt(i))) {
                sb.append(id.charAt(i));
            }
        }
        return sb.length() > 0 ? Integer.parseInt(sb.toString()) : 0;
    }
}
