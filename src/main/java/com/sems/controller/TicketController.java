package com.sems.controller;

import com.sems.dto.TicketResponseDTO;
import com.sems.model.Ticket;
import com.sems.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketRepository ticketRepository;

    @Autowired
    public TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    public List<TicketResponseDTO> list() {
        return ticketRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TicketResponseDTO get(@PathVariable Long id) {
        Ticket ticket = ticketRepository.findById(id);
        return ticket != null ? toDTO(ticket) : null;
    }

    /**
     * Convert Ticket entity to TicketResponseDTO to avoid lazy initialization.
     */
    private TicketResponseDTO toDTO(Ticket ticket) {
        return new TicketResponseDTO(
                ticket.getId(),
                ticket.getEvent() != null ? ticket.getEvent().getId() : null,
                ticket.getEvent() != null ? ticket.getEvent().getName() : null,
                ticket.getUser() != null ? ticket.getUser().getId() : null,
                ticket.getUser() != null ? ticket.getUser().getName() : null,
                ticket.getStatus() != null ? ticket.getStatus().toString() : null
        );
    }
}
