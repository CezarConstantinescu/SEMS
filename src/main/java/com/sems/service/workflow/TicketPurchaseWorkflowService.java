package com.sems.service.workflow;

import com.sems.model.Ticket;

import java.math.BigDecimal;

public interface TicketPurchaseWorkflowService {

    /**
     * Purchase a ticket for a user (external payment is assumed handled elsewhere).
     */
    Ticket purchaseTicket(Long userId, Long eventId, BigDecimal offeredPrice) throws Exception;
}
