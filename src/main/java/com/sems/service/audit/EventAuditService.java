package com.sems.service.audit;

import com.sems.model.Event;

public interface EventAuditService {

    void recordEventCreated(Event event, String by);

    void recordEventUpdated(Event event, String by);

    void recordEventDeleted(Event event, String by);
}
