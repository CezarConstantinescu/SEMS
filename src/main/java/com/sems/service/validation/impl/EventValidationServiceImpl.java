package com.sems.service.validation.impl;

import com.sems.model.Event;
import com.sems.service.validation.EventValidationService;

import java.time.LocalDateTime;

public class EventValidationServiceImpl implements EventValidationService {

    @Override
    public void validateEventIsInFuture(Event event) {
        if (event == null) throw new IllegalArgumentException("Event is null");
        if (event.getStartDateTime() == null) throw new IllegalArgumentException("Event startDateTime is null");
        if (!event.getStartDateTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Event start time must be in the future");
        }
    }

    @Override
    public void validateVenueCapacity(Event event) throws IllegalStateException {
        if (event == null || event.getVenue() == null) return;
        int capacity = event.getVenue().getCapacity() != null ? event.getVenue().getCapacity() : Integer.MAX_VALUE;
        if (event.getTickets() != null && event.getTickets().size() > capacity) {
            throw new IllegalStateException("Venue capacity exceeded for event: " + event.getName());
        }
    }
}
