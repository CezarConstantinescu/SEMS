package com.sems.service.business;

import com.sems.model.Event;

import java.util.List;

public interface RecommendationService {

    List<Event> recommendForUser(Long userId, int limit);

}
