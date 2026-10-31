package com.library.service;

import com.library.util.LibraryException;
import java.time.LocalDate;

/**
 * Service managing the simulated calendar date of the library system.
 */
public class ClockService {

    private LocalDate simDate;

    /**
     * Constructs ClockService with the default reference date (2026-10-26).
     * Complexity: O(1)
     */
    public ClockService() {
        this.simDate = LocalDate.of(2026, 10, 26);
    }

    /**
     * Returns the current simulated system date.
     * Complexity: O(1)
     *
     * @return current sim date
     */
    public LocalDate getSimDate() {
        return simDate;
    }

    /**
     * Advances the simulated system date by N days (1 to 365).
     * Complexity: O(1)
     *
     * @param days number of days to advance
     * @throws LibraryException if days is out of range
     */
    public void advanceDays(int days) {
        if (days < 1 || days > 365) {
            throw new LibraryException("Days to advance must be between 1 and 365.");
        }
        this.simDate = this.simDate.plusDays(days);
    }

    /**
     * Directly overrides the simulated date (used for test setups and state restores).
     * Complexity: O(1)
     *
     * @param date new simulation date
     */
    public void setSimDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        this.simDate = date;
    }
}
