package com.library;

import com.library.service.LibraryContext;
import com.library.ui.ConsoleIO;
import com.library.ui.MainMenu;

/**
 * Entry point for the Library Management System CLI application.
 */
public class Main {

    /**
     * Application main entry point with top-level fault tolerance.
     * Complexity: O(n)
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        LibraryContext context = new LibraryContext();
        ConsoleIO io = new ConsoleIO();
        MainMenu menu = new MainMenu(context, io);

        try {
            menu.run();
        } catch (ConsoleIO.EofException e) {
            ConsoleIO.printInfo("\n[INFO] Input stream closed. Exiting Library Management System. Goodbye!");
        } catch (Throwable t) {
            ConsoleIO.printError("[ERROR] Unexpected system error: " + t.getMessage());
        }
    }
}
