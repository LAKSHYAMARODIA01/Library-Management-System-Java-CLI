package com.library.ui;

import com.library.ds.CustomQueue;
import com.library.service.ReservationService;
import com.library.util.LibraryException;

/**
 * Menu screen handling reservation waitlists and cancellations (FR-R1 to FR-R3).
 */
public class ReservationMenu {

    private final ReservationService reservationService;
    private final ConsoleIO io;

    /**
     * Constructs a ReservationMenu instance.
     * Complexity: O(1)
     *
     * @param reservationService reservation service
     * @param io                 console IO helper
     */
    public ReservationMenu(ReservationService reservationService, ConsoleIO io) {
        this.reservationService = reservationService;
        this.io = io;
    }

    /**
     * Displays the reservation menu loop.
     * Complexity: O(n)
     */
    public void display() {
        while (true) {
            System.out.println("--- Reservations ---");
            System.out.println(" 1. Reserve a Book");
            System.out.println(" 2. Cancel Reservation");
            System.out.println(" 3. View Book Reservation Queue");
            System.out.println(" 0. Back");
            int choice = io.readIntInRange("Enter choice: ", 0, 3);
            if (choice == 0) return;
            handleChoice(choice);
            System.out.println();
        }
    }

    private void handleChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> handleReserve();
                case 2 -> handleCancel();
                case 3 -> handleViewQueue();
            }
        } catch (LibraryException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void handleReserve() {
        String bookId = io.readNonEmpty("Enter book ID: ", "Book ID");
        String memberId = io.readNonEmpty("Enter member ID: ", "Member ID");
        int pos = reservationService.reserveBook(bookId, memberId);
        System.out.println("[OK] Member " + memberId + " reserved book " + bookId
                + " at queue position " + pos + ".");
    }

    private void handleCancel() {
        String bookId = io.readNonEmpty("Enter book ID: ", "Book ID");
        String memberId = io.readNonEmpty("Enter member ID: ", "Member ID");
        reservationService.cancelReservation(bookId, memberId);
        System.out.println("[OK] Reservation for member " + memberId + " on book " + bookId
                + " has been cancelled.");
    }

    private void handleViewQueue() {
        String bookId = io.readNonEmpty("Enter book ID: ", "Book ID");
        CustomQueue<String> queue = reservationService.getReservationQueue(bookId);
        if (queue.isEmpty()) {
            System.out.println("[INFO] No active reservations for book " + bookId + ".");
            return;
        }
        System.out.println("Reservation Queue for Book " + bookId + " (Total: " + queue.size() + "):");
        int pos = 1;
        for (String memberId : queue) {
            System.out.println("  " + pos + ". Member ID: " + memberId);
            pos++;
        }
    }
}
