package com.sems.service.audit.impl;

import com.sems.model.User;
import com.sems.service.audit.UserAuditService;

public class SimpleUserAuditService implements UserAuditService {

    @Override
    public void recordUserUpdated(User user, String by) {
        System.out.println("[AUDIT] User updated by " + by + ": " + user);
    }
}
