package com.sems.service.business.impl;

import com.sems.model.Event;
import com.sems.persistence.EntityManagerUtil;
import com.sems.service.business.RecommendationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class RecommendationServiceImpl implements RecommendationService {

    @Override
    public List<Event> recommendForUser(Long userId, int limit) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            // Very simple strategy: return next upcoming events
            TypedQuery<Event> q = em.createQuery("SELECT e FROM Event e ORDER BY e.startDateTime", Event.class);
            q.setMaxResults(limit);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
