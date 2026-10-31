package com.library.ui;

import com.library.ds.CustomLinkedList;
import com.library.ds.CustomQueue;
import com.library.ds.HashStats;
import com.library.model.Action;
import com.library.model.Book;
import com.library.service.LibraryContext;
import com.library.util.LibraryException;

/**
 * Menu screen providing DSA profiling and internal structure inspections (FR-D1 to FR-D4).
 */
public class InsightsMenu {

    private final LibraryContext context;
    private final ConsoleIO io;

    /**
     * Constructs an InsightsMenu instance.
     * Complexity: O(1)
     *
     * @param context shared library context
     * @param io      console IO helper
     */
    public InsightsMenu(LibraryContext context, ConsoleIO io) {
        this.context = context;
        this.io = io;
    }

    /**
     * Displays the DSA insights menu loop.
     * Complexity: O(n)
     */
    public void display() {
        while (true) {
            System.out.println("--- DSA Insights ---");
            System.out.println(" 1. Hash Table Statistics (Books by ID)");
            System.out.println(" 2. Hash Table Bucket Dump");
            System.out.println(" 3. Undo Stack Snapshot (Top to Bottom)");
            System.out.println(" 4. Book Reservation Queue (Front to Rear)");
            System.out.println(" 0. Back");
            int choice = io.readIntInRange("Enter choice: ", 0, 4);
            if (choice == 0) return;
            handleChoice(choice);
            System.out.println();
        }
    }

    private void handleChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> handleHashStats();
                case 2 -> handleBucketDump();
                case 3 -> handleStackSnapshot();
                case 4 -> handleQueueSnapshot();
            }
        } catch (LibraryException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void handleHashStats() {
        HashStats stats = context.getBooksById().stats();
        System.out.println("=== CustomHashMap Statistics (Books by ID) ===");
        System.out.println("  Capacity:           " + stats.getCapacity());
        System.out.println("  Size (Entries):     " + stats.getSize());
        System.out.println("  Load Factor:        " + String.format("%.2f", stats.getLoadFactor())
                + " (Threshold: 0.75)");
        System.out.println("  Non-Empty Buckets:  " + stats.getNonEmptyBuckets());
        System.out.println("  Longest Chain:      " + stats.getLongestChain());
        System.out.println("  Resize Count:       " + stats.getResizeCount());
    }

    private void handleBucketDump() {
        HashStats stats = context.getBooksById().stats();
        System.out.println("=== Bucket Chain Dump (Capacity: " + stats.getCapacity() + ") ===");
        for (int i = 0; i < stats.getCapacity(); i++) {
            CustomLinkedList<String> keys = context.getBooksById().bucketKeys(i);
            if (!keys.isEmpty()) {
                System.out.print("  Bucket [" + Format.padLeft(String.valueOf(i), 2) + "]: ");
                boolean first = true;
                for (String k : keys) {
                    if (!first) System.out.print(" -> ");
                    System.out.print(k);
                    first = false;
                }
                System.out.println();
            }
        }
    }

    private void handleStackSnapshot() {
        CustomLinkedList<Action> stackList = context.getUndoStack().printTopToBottom();
        if (stackList.isEmpty()) {
            System.out.println("[INFO] Undo stack is currently empty.");
            return;
        }
        System.out.println("=== Undo Stack (Top to Bottom, Size: " + stackList.size() + ") ===");
        int depth = 1;
        for (Action action : stackList) {
            System.out.println("  " + depth + ". " + action);
            depth++;
        }
    }

    private void handleQueueSnapshot() {
        String bookId = io.readNonEmpty("Enter book ID: ", "Book ID");
        Book book = context.getBooksById().get(bookId);
        if (book == null) {
            System.out.println("[ERROR] Book " + bookId + " not found.");
            return;
        }
        CustomQueue<String> queue = book.getReservations();
        if (queue.isEmpty()) {
            System.out.println("[INFO] No members queued for book " + bookId + ".");
            return;
        }
        System.out.println("=== Reservation Queue for " + bookId + " (Front to Rear, Size: "
                + queue.size() + ") ===");
        int pos = 1;
        for (String memberId : queue) {
            System.out.println("  " + (pos == 1 ? "FRONT -> " : "         ") + pos + ". Member: " + memberId);
            pos++;
        }
    }
}
