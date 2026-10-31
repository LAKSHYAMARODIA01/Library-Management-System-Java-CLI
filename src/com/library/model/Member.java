package com.library.model;

import com.library.ds.CustomLinkedList;
import com.library.util.Validator;

/**
 * Domain model representing a registered library member.
 */
public class Member {

    private final String id;
    private String name;
    private String email;
    private String phone;
    private double finesCollected;
    private final CustomLinkedList<Loan> loans;

    /**
     * Constructs a new Member with validated details.
     * Complexity: O(1)
     *
     * @param id    unique member ID (e.g. M001)
     * @param name  full member name
     * @param email email address
     * @param phone 10-digit phone number
     */
    public Member(String id, String name, String email, String phone) {
        Validator.validateNonEmpty(id, "Member ID");
        Validator.validateNonEmpty(name, "Name");
        Validator.validateEmail(email);
        Validator.validatePhone(phone);

        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.finesCollected = 0.0;
        this.loans = new CustomLinkedList<>();
    }

    /**
     * Full constructor for restoring state during persistence or demo loading.
     * Complexity: O(1)
     *
     * @param id             unique member ID
     * @param name           full member name
     * @param email          email address
     * @param phone          10-digit phone number
     * @param finesCollected accumulated fines collected
     */
    public Member(String id, String name, String email, String phone, double finesCollected) {
        Validator.validateNonEmpty(id, "Member ID");
        Validator.validateNonEmpty(name, "Name");
        Validator.validateEmail(email);
        Validator.validatePhone(phone);

        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.finesCollected = finesCollected;
        this.loans = new CustomLinkedList<>();
    }

    /**
     * Returns the member ID.
     * Complexity: O(1)
     *
     * @return member ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the member's full name.
     * Complexity: O(1)
     *
     * @return member name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the member's name.
     * Complexity: O(1)
     *
     * @param name new name
     */
    public void setName(String name) {
        Validator.validateNonEmpty(name, "Name");
        this.name = name;
    }

    /**
     * Returns the member's email address.
     * Complexity: O(1)
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the member's email address.
     * Complexity: O(1)
     *
     * @param email new email address
     */
    public void setEmail(String email) {
        Validator.validateEmail(email);
        this.email = email;
    }

    /**
     * Returns the member's 10-digit phone number.
     * Complexity: O(1)
     *
     * @return phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Updates the member's phone number.
     * Complexity: O(1)
     *
     * @param phone new 10-digit phone number
     */
    public void setPhone(String phone) {
        Validator.validatePhone(phone);
        this.phone = phone;
    }

    /**
     * Returns total fines collected from this member.
     * Complexity: O(1)
     *
     * @return fines collected
     */
    public double getFinesCollected() {
        return finesCollected;
    }

    /**
     * Adds an amount to collected fines.
     * Complexity: O(1)
     *
     * @param amount fine amount to add
     */
    public void addFines(double amount) {
        this.finesCollected += amount;
    }

    /**
     * Subtracts an amount from collected fines (e.g. for undo).
     * Complexity: O(1)
     *
     * @param amount fine amount to subtract
     */
    public void subtractFines(double amount) {
        this.finesCollected = Math.max(0.0, this.finesCollected - amount);
    }

    /**
     * Returns the list of active loans held by this member.
     * Complexity: O(1)
     *
     * @return active loans list
     */
    public CustomLinkedList<Loan> getLoans() {
        return loans;
    }

    /**
     * Returns a string representation of the member.
     * Complexity: O(1)
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "[" + id + "] " + name + " (" + email + ", " + phone
                + ", Active Loans: " + loans.size()
                + ", Fines Paid: Rs " + String.format("%.0f", finesCollected) + ")";
    }
}
