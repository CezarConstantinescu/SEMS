package com.sems.service.workflow.impl;

import com.sems.model.Event;
import com.sems.model.Ticket;
import com.sems.model.Venue;
import com.sems.repository.EventRepository;
import com.sems.repository.TicketRepository;
import com.sems.repository.VenueRepository;
import com.sems.repository.impl.EventRepositoryImpl;
import com.sems.repository.impl.TicketRepositoryImpl;
import com.sems.repository.impl.VenueRepositoryImpl;
import com.sems.service.audit.EventAuditService;
import com.sems.service.audit.impl.SimpleEventAuditService;
import com.sems.service.workflow.EventCreationWorkflowService;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class EventCreationWorkflowServiceImpl implements EventCreationWorkflowService {

    private final EventAuditService auditService;

    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    public EventCreationWorkflowServiceImpl() {
        this.auditService = new SimpleEventAuditService();
        this.venueRepository = new VenueRepositoryImpl();
        this.eventRepository = new EventRepositoryImpl();
        this.ticketRepository = new TicketRepositoryImpl();
    }

    public EventCreationWorkflowServiceImpl(EventAuditService auditService,
                                            VenueRepository venueRepository,
                                            EventRepository eventRepository,
                                            TicketRepository ticketRepository) {
        this.auditService = auditService;
        this.venueRepository = venueRepository;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Event createEvent(Event event, Long venueId) throws Exception {
        Venue venue = venueRepository.findById(venueId);
        if (venue == null) throw new IllegalArgumentException("Venue not found: " + venueId);

        event.setVenue(venue);

        // persist event first so it has an id
        eventRepository.save(event);

        // Simple ticket generation example: create a handful of placeholder tickets
        int cap = venue.getCapacity() != null ? venue.getCapacity() : 10;
        for (int i = 0; i < Math.min(10, cap); i++) {
            String ticketNum = "EVT-" + System.currentTimeMillis() + "-" + i;
            Ticket t = new Ticket(ticketNum, BigDecimal.ZERO, null, event);
            event.addTicket(t);
            ticketRepository.save(t);
        }

        auditService.recordEventCreated(event, "system");

        return event;
    }
}
