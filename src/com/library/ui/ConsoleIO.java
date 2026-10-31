package com.library.ui;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Console input wrapper providing robust validation and graceful EOF termination.
 */
public class ConsoleIO {

    /**
     * Unchecked exception thrown when input stream ends unexpectedly (EOF).
     */
    public static class EofException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        /**
         * Constructs an EofException.
         * Complexity: O(1)
         */
        public EofException() {
            super("End of input stream reached.");
        }
    }

    private final Scanner scanner;

    /**
     * Constructs a ConsoleIO instance wrapping System.in.
     * Complexity: O(1)
     */
    public ConsoleIO() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Constructs a ConsoleIO instance wrapping a specified Scanner.
     * Complexity: O(1)
     *
     * @param scanner scanner source
     */
    public ConsoleIO(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Reads a line of text with a prompt.
     * Complexity: O(n)
     *
     * @param prompt prompt message
     * @return entered string
     * @throws EofException if EOF reached
     */
    public String readLine(String prompt) {
        System.out.print(prompt);
        try {
            if (!scanner.hasNextLine()) {
                throw new EofException();
            }
            return scanner.nextLine().trim();
        } catch (NoSuchElementException e) {
            throw new EofException();
        }
    }

    /**
     * Reads a non-empty string, re-prompting on empty input.
     * Complexity: O(n)
     *
     * @param prompt    prompt message
     * @param fieldName field name for error reporting
     * @return non-empty trimmed string
     */
    public String readNonEmpty(String prompt, String fieldName) {
        while (true) {
            String val = readLine(prompt);
            if (!val.isEmpty()) {
                return val;
            }
            System.out.println("[ERROR] " + fieldName + " cannot be empty.");
        }
    }

    /**
     * Reads an integer value with error re-prompting.
     * Complexity: O(1)
     *
     * @param prompt prompt message
     * @return parsed integer
     */
    public int readInt(String prompt) {
        while (true) {
            String text = readLine(prompt);
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Invalid number. Please enter an integer.");
            }
        }
    }

    /**
     * Reads an integer within an inclusive range [min, max].
     * Complexity: O(1)
     *
     * @param prompt prompt message
     * @param min    inclusive minimum
     * @param max    inclusive maximum
     * @return validated integer
     */
    public int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int val = readInt(prompt);
            if (val >= min && val <= max) {
                return val;
            }
            System.out.println("[ERROR] Choice must be between " + min + " and " + max + ".");
        }
    }

    /**
     * Prints an informational message to the standard output.
     * Complexity: O(1)
     *
     * @param message informational text
     */
    public static void printInfo(String message) {
        System.out.println(message);
    }

    /**
     * Prints an error message to the standard output.
     * Complexity: O(1)
     *
     * @param message error text
     */
    public static void printError(String message) {
        System.out.println(message);
    }

    /**
     * Prompts the user with a yes/no question.
     * Complexity: O(1)
     *
     * @param prompt prompt message
     * @return true if user answered 'y' or 'yes'
     */
    public boolean confirm(String prompt) {
        String answer = readLine(prompt).toLowerCase();
        return answer.equals("y") || answer.equals("yes");
    }
}
