package com.sems.service.validation;

import java.math.BigDecimal;

public interface TicketValidationService {

    void validateSeatNumber(String seatNumber) throws IllegalArgumentException;

    void validatePriceNonNegative(BigDecimal price) throws IllegalArgumentException;
}
