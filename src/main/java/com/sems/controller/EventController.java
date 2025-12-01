package com.sems.controller;

import com.sems.model.Event;
import com.sems.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepository;

    @Autowired
    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public List<Event> list() {
        return eventRepository.findAll();
    }

    @GetMapping("/{id}")
    public Event get(@PathVariable Long id) {
        return eventRepository.findById(id);
    }
}
