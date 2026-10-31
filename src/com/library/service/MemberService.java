package com.library.service;

import com.library.ds.CustomLinkedList;
import com.library.model.Book;
import com.library.model.Member;
import com.library.util.LibraryException;
import com.library.util.Validator;

/**
 * Service managing member registrations, updates, lookups, and account constraints.
 */
public class MemberService {

    private final LibraryContext context;

    /**
     * Constructs a MemberService with the given library context.
     * Complexity: O(1)
     *
     * @param context shared library context
     */
    public MemberService(LibraryContext context) {
        this.context = context;
    }

    /**
     * Registers a new library member (FR-M1).
     * Complexity: O(1) average
     *
     * @param name  full name
     * @param email valid email address
     * @param phone 10-digit phone number
     * @return registered Member instance
     */
    public Member registerMember(String name, String email, String phone) {
        Validator.validateNonEmpty(name, "Name");
        Validator.validateEmail(email);
        Validator.validatePhone(phone);

        String memberId = context.getIdGenerator().nextMemberId();
        Member member = new Member(memberId, name, email, phone);
        context.getMembersById().put(memberId, member);
        return member;
    }

    /**
     * Updates an existing member's contact information (FR-M2).
     * Complexity: O(1) average
     *
     * @param memberId identifier of the member
     * @param email    new email address (or null to keep unchanged)
     * @param phone    new 10-digit phone number (or null to keep unchanged)
     */
    public void updateMember(String memberId, String email, String phone) {
        Member member = findById(memberId);
        if (email != null && !email.trim().isEmpty()) {
            Validator.validateEmail(email);
            member.setEmail(email.trim());
        }
        if (phone != null && !phone.trim().isEmpty()) {
            Validator.validatePhone(phone);
            member.setPhone(phone.trim());
        }
    }

    /**
     * Removes a member from the system (FR-M3), enforcing BR-9.
     * Complexity: O(total books * queue length)
     *
     * @param memberId identifier of the member
     */
    public void removeMember(String memberId) {
        Member member = findById(memberId);
        if (!member.getLoans().isEmpty()) {
            throw new LibraryException("Cannot remove member " + memberId
                    + ": member has active loans (BR-9).");
        }
        for (Book book : context.getCatalog()) {
            if (book.getReservations().contains(memberId)) {
                throw new LibraryException("Cannot remove member " + memberId
                        + ": member is in reservation queue for book " + book.getId() + " (BR-9).");
            }
        }
        context.getMembersById().remove(memberId);
    }

    /**
     * Finds a member by their unique identifier (FR-M4).
     * Complexity: O(1) average
     *
     * @param memberId member identifier
     * @return matching Member instance
     */
    public Member findById(String memberId) {
        Validator.validateNonEmpty(memberId, "Member ID");
        Member member = context.getMembersById().get(memberId);
        if (member == null) {
            throw new LibraryException("Member " + memberId + " not found.");
        }
        return member;
    }

    /**
     * Returns a list of all registered members (FR-M5).
     * Complexity: O(n + capacity)
     *
     * @return list of members
     */
    public CustomLinkedList<Member> getAllMembers() {
        return context.getMembersById().values();
    }
}
