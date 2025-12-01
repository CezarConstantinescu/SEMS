package com.sems.service.audit;

import com.sems.model.Ticket;
import com.sems.model.User;

public interface TicketAuditService {

    void recordTicketPurchase(Ticket ticket, User by);

    void recordTicketCancellation(Ticket ticket, User by);
}
