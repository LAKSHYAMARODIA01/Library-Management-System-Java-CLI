package com.library.ui;

import com.library.ds.CustomLinkedList;
import com.library.model.Loan;
import com.library.model.Member;
import com.library.service.LibraryContext;
import com.library.service.MemberService;
import com.library.util.LibraryException;
import java.time.LocalDate;

/**
 * Menu screen handling library member management operations (FR-M1 to FR-M6).
 */
public class MemberMenu {

    private final MemberService memberService;
    private final LibraryContext context;
    private final ConsoleIO io;

    /**
     * Constructs a MemberMenu instance.
     * Complexity: O(1)
     *
     * @param memberService member service
     * @param context       shared library context
     * @param io            console IO helper
     */
    public MemberMenu(MemberService memberService, LibraryContext context, ConsoleIO io) {
        this.memberService = memberService;
        this.context = context;
        this.io = io;
    }

    /**
     * Displays the member management menu loop.
     * Complexity: O(n)
     */
    public void display() {
        while (true) {
            System.out.println("--- Member Management ---");
            System.out.println(" 1. Register Member");
            System.out.println(" 2. Update Member Contact");
            System.out.println(" 3. Remove Member");
            System.out.println(" 4. Find Member by ID");
            System.out.println(" 5. List All Members");
            System.out.println(" 6. View Member Profile");
            System.out.println(" 0. Back");
            int choice = io.readIntInRange("Enter choice: ", 0, 6);
            if (choice == 0) return;
            handleChoice(choice);
            System.out.println();
        }
    }

    private void handleChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> handleRegister();
                case 2 -> handleUpdate();
                case 3 -> handleRemove();
                case 4 -> handleFindById();
                case 5 -> handleListAll();
                case 6 -> handleViewProfile();
            }
        } catch (LibraryException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void handleRegister() {
        String name = io.readNonEmpty("Enter member name: ", "Name");
        String email = io.readNonEmpty("Enter email: ", "Email");
        String phone = io.readNonEmpty("Enter 10-digit phone: ", "Phone");
        Member member = memberService.registerMember(name, email, phone);
        System.out.println("[OK] Member registered successfully: " + member.getId());
    }

    private void handleUpdate() {
        String id = io.readNonEmpty("Enter member ID: ", "Member ID");
        String email = io.readLine("Enter new email (blank to skip): ");
        String phone = io.readLine("Enter new phone (blank to skip): ");
        memberService.updateMember(id, email, phone);
        System.out.println("[OK] Member contact updated successfully.");
    }

    private void handleRemove() {
        String id = io.readNonEmpty("Enter member ID to remove: ", "Member ID");
        memberService.removeMember(id);
        System.out.println("[OK] Member " + id + " removed successfully.");
    }

    private void handleFindById() {
        String id = io.readNonEmpty("Enter member ID: ", "Member ID");
        Member m = memberService.findById(id);
        System.out.println("[OK] Member: " + m);
    }

    private void handleListAll() {
        CustomLinkedList<Member> members = memberService.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("[INFO] No members registered.");
            return;
        }
        System.out.println(Format.pad("ID", 7) + Format.pad("Name", 22) + Format.pad("Email", 26)
                + Format.pad("Phone", 14) + Format.pad("Loans", 8) + Format.pad("Fines Paid", 12));
        System.out.println(Format.divider(89));
        for (Member m : members) {
            System.out.println(Format.pad(m.getId(), 7)
                    + Format.pad(m.getName(), 22)
                    + Format.pad(m.getEmail(), 26)
                    + Format.pad(m.getPhone(), 14)
                    + Format.pad(String.valueOf(m.getLoans().size()), 8)
                    + Format.pad(Format.currency(m.getFinesCollected()), 12));
        }
    }

    private void handleViewProfile() {
        String id = io.readNonEmpty("Enter member ID: ", "Member ID");
        Member m = memberService.findById(id);
        LocalDate simDate = context.getClockService().getSimDate();
        System.out.println("=== Member Profile: " + m.getId() + " ===");
        System.out.println("Name:        " + m.getName());
        System.out.println("Email:       " + m.getEmail());
        System.out.println("Phone:       " + m.getPhone());
        System.out.println("Fines Paid:  " + Format.currency(m.getFinesCollected()));
        System.out.println("Active Loans (" + m.getLoans().size() + "):");
        if (m.getLoans().isEmpty()) {
            System.out.println("  None");
        } else {
            for (Loan l : m.getLoans()) {
                boolean overdue = simDate.isAfter(l.getDueDate());
                System.out.println("  - Book " + l.getBookId() + " (Issued: " + l.getIssueDate()
                        + ", Due: " + l.getDueDate() + ")" + (overdue ? " [OVERDUE]" : ""));
            }
        }
    }
}
