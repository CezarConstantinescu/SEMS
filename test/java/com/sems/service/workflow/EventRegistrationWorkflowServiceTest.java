package com.sems.service.workflow;

import com.sems.factory.EventAggregateFactory;
import com.sems.factory.UserAggregateFactory;
import com.sems.model.Event;
import com.sems.model.Ticket;
import com.sems.model.User;
import com.sems.persistence.EntityManagerUtil;
import com.sems.service.workflow.impl.EventRegistrationWorkflowServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventRegistrationWorkflowServiceTest {

    @AfterAll
    static void cleanup() {
        EntityManagerUtil.close();
    }

    @Test
    void registerForEventWorks() throws Exception {
        String base = "WorkflowReg" + System.currentTimeMillis();
        Event e = EventAggregateFactory.createConcertAggregate(base, 0, true);
        User u = UserAggregateFactory.createUserWithTickets(base + "User", 0, null, true);

        EventRegistrationWorkflowService svc = new EventRegistrationWorkflowServiceImpl();
        Ticket t = svc.registerForEvent(u.getId(), e.getId(), null);

        assertNotNull(t);
        assertEquals(u.getId(), t.getUser().getId());
        assertEquals(e.getId(), t.getEvent().getId());
    }
}
