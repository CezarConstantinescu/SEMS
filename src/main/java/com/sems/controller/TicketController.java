package com.sems.controller;

import com.sems.model.Ticket;
import com.sems.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketRepository ticketRepository;

    @Autowired
    public TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    public List<Ticket> list() {
        return ticketRepository.findAll();
    }

    @GetMapping("/{id}")
    public Ticket get(@PathVariable Long id) {
        return ticketRepository.findById(id);
    }
}
