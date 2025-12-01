package com.sems.service.workflow;

import com.sems.model.Ticket;

import java.math.BigDecimal;

public interface EventRegistrationWorkflowService {

    /**
     * Registers a user for an event and returns the created Ticket.
     * Price may be null to let the pricing service compute it.
     */
    Ticket registerForEvent(Long userId, Long eventId, BigDecimal price) throws Exception;
}
