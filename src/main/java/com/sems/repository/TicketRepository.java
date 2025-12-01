package com.sems.repository;

import com.sems.model.Ticket;

import java.util.List;

public interface TicketRepository {

    Ticket findById(Long id);

    List<Ticket> findAll();

    void save(Ticket ticket);

    void delete(Ticket ticket);

    Ticket findByTicketNumber(String ticketNumber);

    long countByEventId(Long eventId);
}
