package com.sems.service.business;

import com.sems.model.Event;

import java.math.BigDecimal;
import java.util.List;

public interface EventStatsService {

    long countAttendees(Long eventId);

    BigDecimal revenueForEvent(Long eventId);

    double occupancyRateForVenue(Long venueId);

    List<Event> findTopEvents(int limit);
}
