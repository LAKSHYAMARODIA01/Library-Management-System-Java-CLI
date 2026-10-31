package com.library.ui;

import com.library.model.Action;
import com.library.service.BookService;
import com.library.service.CirculationService;
import com.library.service.LibraryContext;
import com.library.service.ReportService;
import com.library.service.ReservationService;
import com.library.service.UndoService;
import com.library.util.LibraryException;

/**
 * Main application dashboard and router for all subsystem menus.
 */
public class MainMenu {

    private final LibraryContext context;
    private final ConsoleIO io;
    private final BookMenu bookMenu;
    private final MemberMenu memberMenu;
    private final CirculationMenu circulationMenu;
    private final ReservationMenu reservationMenu;
    private final UndoService undoService;
    private final ReportMenu reportMenu;
    private final InsightsMenu insightsMenu;
    private final AdminMenu adminMenu;

    /**
     * Constructs the MainMenu coordinating all library subsystems.
     * Complexity: O(1)
     *
     * @param context shared library context
     * @param io      console IO helper
     */
    public MainMenu(LibraryContext context, ConsoleIO io) {
        this.context = context;
        this.io = io;
        BookService bookService = new BookService(context);
        this.bookMenu = new BookMenu(bookService, io);
        this.memberMenu = new MemberMenu(new com.library.service.MemberService(context), context, io);
        CirculationService cs = new CirculationService(context);
        ReservationService rs = new ReservationService(context);
        this.circulationMenu = new CirculationMenu(cs, rs, io);
        this.reservationMenu = new ReservationMenu(rs, io);
        this.undoService = new UndoService(context);
        this.reportMenu = new ReportMenu(new ReportService(context), io);
        this.insightsMenu = new InsightsMenu(context, io);
        this.adminMenu = new AdminMenu(context, io);
    }

    /**
     * Runs the primary interactive menu loop.
     * Complexity: O(n)
     */
    public void run() {
        while (true) {
            printHeader();
            printOptions();
            int choice = io.readIntInRange("Enter choice: ", 0, 8);
            if (choice == 0) {
                System.out.println("Thank you for using the Library Management System. Goodbye!");
                return;
            }
            handleTopLevelChoice(choice);
            System.out.println();
        }
    }

    private void printHeader() {
        System.out.println("==============================================");
        System.out.println("        LIBRARY MANAGEMENT SYSTEM");
        System.out.println("   Date: " + context.getClockService().getSimDate()
                + "     Undo available: " + undoService.getUndoCount());
        System.out.println("==============================================");
    }

    private void printOptions() {
        System.out.println(" 1. Book Management");
        System.out.println(" 2. Member Management");
        System.out.println(" 3. Circulation (Issue / Return)");
        System.out.println(" 4. Reservations");
        System.out.println(" 5. Undo Last Action");
        System.out.println(" 6. Reports and History");
        System.out.println(" 7. DSA Insights");
        System.out.println(" 8. Admin");
        System.out.println(" 0. Exit");
    }

    private void handleTopLevelChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> bookMenu.display();
                case 2 -> memberMenu.display();
                case 3 -> circulationMenu.display();
                case 4 -> reservationMenu.display();
                case 5 -> handleUndoAction();
                case 6 -> reportMenu.display();
                case 7 -> insightsMenu.display();
                case 8 -> adminMenu.display();
            }
        } catch (LibraryException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void handleUndoAction() {
        if (undoService.getUndoCount() == 0) {
            System.out.println("[INFO] Nothing to undo.");
            return;
        }
        Action peeked = undoService.peek();
        System.out.println("[INFO] Most recent action: " + peeked);
        if (io.confirm("Are you sure you want to undo this action? (y/n): ")) {
            Action undone = undoService.undoLastAction();
            if (undone != null) {
                System.out.println("[OK] Successfully reversed action: " + undone.getType()
                        + " for book " + undone.getBookId());
            }
        } else {
            System.out.println("[INFO] Undo operation cancelled.");
        }
    }
}
