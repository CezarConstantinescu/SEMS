package com.sems.service.audit.impl;

import com.sems.model.Ticket;
import com.sems.model.User;
import com.sems.service.audit.TicketAuditService;

public class SimpleTicketAuditService implements TicketAuditService {

    @Override
    public void recordTicketPurchase(Ticket ticket, User by) {
        System.out.println("[AUDIT] Ticket purchased by " + (by != null ? by.getEmail() : "unknown") + ": " + ticket);
    }

    @Override
    public void recordTicketCancellation(Ticket ticket, User by) {
        System.out.println("[AUDIT] Ticket cancelled by " + (by != null ? by.getEmail() : "unknown") + ": " + ticket);
    }
}
