package com.sems.service.business;

import com.sems.factory.EventAggregateFactory;
import com.sems.model.Event;
import com.sems.model.Ticket;
import com.sems.persistence.EntityManagerUtil;
import com.sems.repository.TicketRepository;
import com.sems.repository.impl.TicketRepositoryImpl;
import com.sems.service.business.impl.EventStatsServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class EventStatsServiceTest {

    @AfterAll
    static void cleanup() {
        EntityManagerUtil.close();
    }

    @Test
    void revenueAndCount() {
        String base = "StatsEvt" + System.currentTimeMillis();
        Event e = EventAggregateFactory.createConcertAggregate(base, 0, true);

        // add two tickets with prices
        TicketRepository tr = new TicketRepositoryImpl();
        Ticket t1 = new Ticket("S-T1-" + System.currentTimeMillis(), new BigDecimal("10.00"), null, e);
        Ticket t2 = new Ticket("S-T2-" + System.currentTimeMillis(), new BigDecimal("15.00"), null, e);
        tr.save(t1);
        tr.save(t2);

        EventStatsService svc = new EventStatsServiceImpl();
        long count = svc.countAttendees(e.getId());
        assertTrue(count >= 2);

        BigDecimal revenue = svc.revenueForEvent(e.getId());
        assertTrue(revenue.compareTo(new BigDecimal("0")) > 0);
    }
}
