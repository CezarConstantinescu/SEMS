package com.sems.service.workflow;

import com.sems.model.Event;
import com.sems.model.Venue;
import com.sems.persistence.EntityManagerUtil;
import com.sems.repository.VenueRepository;
import com.sems.repository.impl.VenueRepositoryImpl;
import com.sems.service.workflow.impl.EventCreationWorkflowServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventCreationWorkflowServiceTest {

    @AfterAll
    static void cleanup() {
        EntityManagerUtil.close();
    }

    @Test
    void createEventPersistsTickets() throws Exception {
        // create a venue first
        Venue v = new Venue("TestVenue" + System.currentTimeMillis(), "Addr", 100);
        VenueRepository vr = new VenueRepositoryImpl();
        vr.save(v);

        EventCreationWorkflowService svc = new EventCreationWorkflowServiceImpl();

        Event e = new com.sems.model.Concert("New Concert", java.time.LocalDateTime.now().plusDays(3), java.time.LocalDateTime.now().plusDays(3).plusHours(2), v, "Artist", "Pop");
        Event persisted = svc.createEvent(e, v.getId());

        assertNotNull(persisted.getId());
        assertFalse(persisted.getTickets().isEmpty());
    }
}
