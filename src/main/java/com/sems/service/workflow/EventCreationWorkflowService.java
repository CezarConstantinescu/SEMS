package com.sems.service.workflow;

import com.sems.model.Event;

public interface EventCreationWorkflowService {

    /**
     * Creates an event and performs related setup (assign venue, generate tickets, notify admin).
     * Returns the persisted Event instance.
     */
    Event createEvent(Event event, Long venueId) throws Exception;
}
