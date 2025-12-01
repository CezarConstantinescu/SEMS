package com.sems.service.validation;

import com.sems.model.Event;

public interface EventValidationService {

    void validateEventIsInFuture(Event event);

    void validateVenueCapacity(Event event) throws IllegalStateException;
}
