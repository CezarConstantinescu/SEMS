package com.sems.service.business.impl;

import com.sems.model.Event;
import com.sems.persistence.EntityManagerUtil;
import com.sems.service.business.EventStatsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.List;

public class EventStatsServiceImpl implements EventStatsService {

    @Override
    public long countAttendees(Long eventId) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            TypedQuery<Long> q = em.createQuery("SELECT COUNT(t) FROM Ticket t WHERE t.event.id = :eid", Long.class);
            q.setParameter("eid", eventId);
            return q.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public BigDecimal revenueForEvent(Long eventId) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            TypedQuery<BigDecimal> q = em.createQuery("SELECT COALESCE(SUM(t.price), 0) FROM Ticket t WHERE t.event.id = :eid", BigDecimal.class);
            q.setParameter("eid", eventId);
            return q.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public double occupancyRateForVenue(Long venueId) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            TypedQuery<Long> seatsQ = em.createQuery("SELECT COALESCE(SUM(e.tickets.size),0) FROM Event e WHERE e.venue.id = :vid", Long.class);
            seatsQ.setParameter("vid", venueId);
            Long booked = seatsQ.getSingleResult();

            TypedQuery<Integer> capQ = em.createQuery("SELECT v.capacity FROM Venue v WHERE v.id = :vid", Integer.class);
            capQ.setParameter("vid", venueId);
            Integer capacity = capQ.getSingleResult();

            if (capacity == null || capacity == 0) return 0.0;
            return (double) booked / capacity;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Event> findTopEvents(int limit) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            TypedQuery<Event> q = em.createQuery("SELECT e FROM Event e ORDER BY e.startDateTime", Event.class);
            q.setMaxResults(limit);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
