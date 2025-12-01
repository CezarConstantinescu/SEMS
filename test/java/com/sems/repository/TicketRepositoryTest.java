package com.sems.repository;

import com.sems.factory.EventAggregateFactory;
import com.sems.model.Concert;
import com.sems.model.Ticket;
import com.sems.model.User;
import com.sems.persistence.EntityManagerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TicketRepositoryTest {

    @AfterAll
    static void cleanup() {
        EntityManagerUtil.close();
    }

    @Test
    void saveAndFindTicket() {
        String base = "TicketRepo" + System.currentTimeMillis();
        Concert c = EventAggregateFactory.createConcertAggregate(base, 0, true);

        // create ticket manually
        User u = new User("TR User", "tr.user@example.com");
        com.sems.repository.UserRepository ur = new com.sems.repository.impl.UserRepositoryImpl();
        ur.save(u);

        Ticket t = new Ticket("TNUM-" + System.currentTimeMillis(), new java.math.BigDecimal("42.00"), u, c);

        TicketRepository repo = new com.sems.repository.impl.TicketRepositoryImpl();
        repo.save(t);

        Ticket found = repo.findByTicketNumber(t.getTicketNumber());
        assertNotNull(found);
        assertEquals(t.getTicketNumber(), found.getTicketNumber());

        long count = repo.countByEventId(c.getId());
        assertTrue(count >= 1);
    }
}
