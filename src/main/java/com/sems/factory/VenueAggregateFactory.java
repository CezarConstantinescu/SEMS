package com.sems.factory;

import com.sems.model.*;
import com.sems.persistence.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Factory helpers to create Venue aggregates (Venue + Events + Attendees).
 */
public final class VenueAggregateFactory {
    private VenueAggregateFactory() {}

    public static Venue createVenueWithEvents(String baseName, int eventCount, int ticketsPerEvent, boolean persist) {
        Venue v = new Venue(baseName + " Venue", "99 Venue Rd", Math.max(100, ticketsPerEvent));

        if (!persist) {
            for (int i = 0; i < eventCount; i++) {
                LocalDateTime s = LocalDateTime.now().plusDays(i + 1);
                Concert c = new Concert(baseName + " Concert " + i, s, s.plusHours(2), v, "Artist " + i, "Genre");
                for (int t = 0; t < ticketsPerEvent; t++) {
                    String tn = "V-TKT-" + System.currentTimeMillis() + "-" + i + "-" + t;
                    Ticket ticket = new Ticket(tn, new BigDecimal("20.00"), null, c);
                    c.addTicket(ticket);
                }
                v.addEvent(c);
            }
            return v;
        }

        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(v);
            for (int i = 0; i < eventCount; i++) {
                LocalDateTime s = LocalDateTime.now().plusDays(i + 1);
                Concert c = new Concert(baseName + " Concert " + i, s, s.plusHours(2), v, "Artist " + i, "Genre");
                em.persist(c);
                for (int t = 0; t < ticketsPerEvent; t++) {
                    String tn = "V-TKT-" + System.currentTimeMillis() + "-" + i + "-" + t;
                    Ticket ticket = new Ticket(tn, new BigDecimal("20.00"), null, c);
                    c.addTicket(ticket);
                    em.persist(ticket);
                }
                v.addEvent(c);
            }

            tx.commit();
            return v;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
