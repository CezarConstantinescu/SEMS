package com.sems.service.workflow.impl;

import com.sems.model.Event;
import com.sems.model.Ticket;
import com.sems.model.User;
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
import com.sems.service.validation.TicketValidationService;
import com.sems.service.validation.impl.TicketValidationServiceImpl;
import com.sems.service.workflow.TicketPurchaseWorkflowService;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class TicketPurchaseWorkflowServiceImpl implements TicketPurchaseWorkflowService {

    private final TicketValidationService ticketValidationService;
    private final PricingService pricingService;
    private final TicketAuditService auditService;

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    public TicketPurchaseWorkflowServiceImpl() {
        this.ticketValidationService = new TicketValidationServiceImpl();
        this.pricingService = new DefaultPricingService();
        this.auditService = new SimpleTicketAuditService();

        this.userRepository = new UserRepositoryImpl();
        this.eventRepository = new EventRepositoryImpl();
        this.ticketRepository = new TicketRepositoryImpl();
    }

    @Override
    public Ticket purchaseTicket(Long userId, Long eventId, BigDecimal offeredPrice) throws Exception {
        User user = userRepository.findById(userId);
        Event event = eventRepository.findByIdWithVenue(eventId);

        if (user == null) throw new IllegalArgumentException("User not found: " + userId);
        if (event == null) throw new IllegalArgumentException("Event not found: " + eventId);

        // Validate ticket constraints
        ticketValidationService.validatePriceNonNegative(offeredPrice != null ? offeredPrice : BigDecimal.ZERO);

        BigDecimal price = offeredPrice != null ? offeredPrice : pricingService.calculatePrice(event, user);

        // Here you would call payment provider; assume success

        String ticketNumber = "PUR-" + System.currentTimeMillis();
        Ticket ticket = new Ticket(ticketNumber, price, user, event);

        user.addTicket(ticket);
        event.addTicket(ticket);

        ticketRepository.save(ticket);

        auditService.recordTicketPurchase(ticket, user);

        return ticket;
    }
}
