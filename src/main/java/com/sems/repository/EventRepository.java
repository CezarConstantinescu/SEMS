package com.sems.repository;

import com.sems.model.Event;

import java.util.List;

public interface EventRepository {

    Event findById(Long id);

    /**
     * Finds an event with its venue eagerly fetched to avoid lazy-loading outside transaction.
     */
    Event findByIdWithVenue(Long id);

    List<Event> findAll();

    void save(Event event);

    void delete(Event event);

    List<Event> findByName(String name);
}
