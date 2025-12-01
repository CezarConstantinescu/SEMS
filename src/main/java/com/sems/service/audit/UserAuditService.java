package com.sems.service.audit;

import com.sems.model.User;

public interface UserAuditService {

    void recordUserUpdated(User user, String by);

}
