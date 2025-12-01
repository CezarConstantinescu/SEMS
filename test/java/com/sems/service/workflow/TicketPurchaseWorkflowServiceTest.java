package com.sems.service.workflow;

import com.sems.factory.EventAggregateFactory;
import com.sems.factory.UserAggregateFactory;
import com.sems.model.Event;
import com.sems.model.Ticket;
import com.sems.model.User;
import com.sems.persistence.EntityManagerUtil;
import com.sems.service.workflow.impl.TicketPurchaseWorkflowServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TicketPurchaseWorkflowServiceTest {

    @AfterAll
    static void cleanup() {
        EntityManagerUtil.close();
    }

    @Test
    void purchaseTicketCreatesTicket() throws Exception {
        String base = "WorkflowPur" + System.currentTimeMillis();
        Event e = EventAggregateFactory.createConcertAggregate(base, 0, true);
        User u = UserAggregateFactory.createUserWithTickets(base + "User", 0, null, true);

        TicketPurchaseWorkflowService svc = new TicketPurchaseWorkflowServiceImpl();
        Ticket t = svc.purchaseTicket(u.getId(), e.getId(), new BigDecimal("99.99"));

        assertNotNull(t);
        assertEquals(u.getId(), t.getUser().getId());
        assertEquals(e.getId(), t.getEvent().getId());
        assertEquals(0, new BigDecimal("99.99").compareTo(t.getPrice()));
    }
}
