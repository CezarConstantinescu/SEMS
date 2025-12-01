package com.sems.service.audit.impl;

import com.sems.model.Event;
import com.sems.service.audit.EventAuditService;

public class SimpleEventAuditService implements EventAuditService {

    @Override
    public void recordEventCreated(Event event, String by) {
        System.out.println("[AUDIT] Event created by " + by + ": " + event);
    }

    @Override
    public void recordEventUpdated(Event event, String by) {
        System.out.println("[AUDIT] Event updated by " + by + ": " + event);
    }

    @Override
    public void recordEventDeleted(Event event, String by) {
        System.out.println("[AUDIT] Event deleted by " + by + ": " + event);
    }
}
