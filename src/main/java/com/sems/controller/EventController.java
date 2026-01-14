package com.sems.controller;

import com.sems.dto.EventResponseDTO;
import com.sems.model.Event;
import com.sems.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepository;

    @Autowired
    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public List<EventResponseDTO> list() {
        return eventRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EventResponseDTO get(@PathVariable Long id) {
        Event event = eventRepository.findById(id);
        return event != null ? toDTO(event) : null;
    }

    /**
     * Convert Event entity to EventResponseDTO to avoid lazy initialization.
     */
    private EventResponseDTO toDTO(Event event) {
        return new EventResponseDTO(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getStartDateTime(),
                event.getEndDateTime(),
                event.getVenue() != null ? event.getVenue().getId() : null,
                event.getVenue() != null ? event.getVenue().getName() : null,
                event.getClass().getSimpleName()
        );
    }
}
