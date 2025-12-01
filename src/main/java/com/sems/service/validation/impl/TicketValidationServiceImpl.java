package com.sems.service.validation.impl;

import com.sems.service.validation.TicketValidationService;

import java.math.BigDecimal;

public class TicketValidationServiceImpl implements TicketValidationService {

    @Override
    public void validateSeatNumber(String seatNumber) throws IllegalArgumentException {
        // Model doesn't have seat numbers by default; validate format if present
        if (seatNumber == null) return;
        if (seatNumber.trim().isEmpty()) throw new IllegalArgumentException("Seat number cannot be empty");
    }

    @Override
    public void validatePriceNonNegative(BigDecimal price) throws IllegalArgumentException {
        if (price == null) return;
        if (price.signum() == -1) throw new IllegalArgumentException("Price cannot be negative");
    }
}
