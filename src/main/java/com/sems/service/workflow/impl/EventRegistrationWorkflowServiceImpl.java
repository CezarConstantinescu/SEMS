package com.sems.service.workflow.impl;

import com.sems.model.*;
import com.sems.repository.EventRepository;
import com.sems.repository.TicketRepository;
import com.sems.repository.UserRepository;
import com.sems.repository.impl.EventRepositoryImpl;
import com.sems.repository.impl.TicketRepositoryImpl;
import com.sems.repository.impl.UserRepositoryImpl;
import com.sems.service.audit.TicketAuditService;
import com.sems.service.audit.impl.SimpleTicketAuditService;
import com.sems.service.business.PricingService;
import com.sems.service.business.impl.DefaultPricingService;
import com.sems.service.validation.EventValidationService;
import com.sems.service.validation.impl.EventValidationServiceImpl;
import com.sems.service.workflow.EventRegistrationWorkflowService;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class EventRegistrationWorkflowServiceImpl implements EventRegistrationWorkflowService {

    private final EventValidationService validationService;
    private final PricingService pricingService;
    private final TicketAuditService auditService;

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public EventRegistrationWorkflowServiceImpl() {
        this.validationService = new EventValidationServiceImpl();
        this.pricingService = new DefaultPricingService();
        this.auditService = new SimpleTicketAuditService();

        this.eventRepository = new EventRepositoryImpl();
        this.userRepository = new UserRepositoryImpl();
        this.ticketRepository = new TicketRepositoryImpl();
    }

    public EventRegistrationWorkflowServiceImpl(EventValidationService validationService,
                                                PricingService pricingService,
                                                TicketAuditService auditService,
                                                EventRepository eventRepository,
                                                UserRepository userRepository,
                                                TicketRepository ticketRepository) {
        this.validationService = validationService;
        this.pricingService = pricingService;
        this.auditService = auditService;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket registerForEvent(Long userId, Long eventId, BigDecimal price) throws Exception {
        User user = userRepository.findById(userId);
        Event event = eventRepository.findByIdWithVenue(eventId);

        if (user == null) throw new IllegalArgumentException("User not found: " + userId);
        if (event == null) throw new IllegalArgumentException("Event not found: " + eventId);

        validationService.validateEventIsInFuture(event);

        long booked = ticketRepository.countByEventId(eventId);
        Integer capacity = event.getVenue() != null ? event.getVenue().getCapacity() : Integer.MAX_VALUE;
        if (booked >= (capacity != null ? capacity : Integer.MAX_VALUE)) {
            throw new IllegalStateException("Event is fully booked");
        }

        BigDecimal finalPrice = price != null ? price : pricingService.calculatePrice(event, user);

        String ticketNumber = "TKT-" + System.currentTimeMillis();
        Ticket ticket = new Ticket(ticketNumber, finalPrice, user, event);

        // maintain both sides in-memory
        user.addTicket(ticket);
        event.addTicket(ticket);

        // persist via repository which will attach managed User/Event before persisting
        ticketRepository.save(ticket);

        auditService.recordTicketPurchase(ticket, user);

        return ticket;
    }
}
