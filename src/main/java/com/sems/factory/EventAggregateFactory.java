package com.sems.factory;

import com.sems.model.*;
import com.sems.persistence.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Factory helpers to create Event aggregates (Venue + Event + Tickets + optional Users).
 * Methods optionally persist created entities when `persist=true`.
 */
public final class EventAggregateFactory {

    private EventAggregateFactory() {}

    public static Concert createConcertAggregate(String baseName, int ticketCount, boolean persist) {
        LocalDateTime now = LocalDateTime.now().plusDays(1);
        Venue venue = new Venue(baseName + " Venue", "123 Factory St", Math.max(100, ticketCount));
        Concert concert = new Concert(
                baseName + " Concert",
                now,
                now.plusHours(3),
                venue,
                baseName + " Artist",
                "Genre"
        );
        concert.setTourName(baseName + " Tour");

        if (!persist) {
            // create in-memory tickets and users
            for (int i = 0; i < ticketCount; i++) {
                User u = new User(baseName + " User " + i, baseName.toLowerCase() + i + "@example.com");
                String tn = "TKT-F-" + System.currentTimeMillis() + "-" + i;
                Ticket t = new Ticket(tn, new BigDecimal("50.00"), u, concert);
                u.addTicket(t);
                concert.addTicket(t);
            }
            return concert;
        }

        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(venue);
            em.persist(concert);

            for (int i = 0; i < ticketCount; i++) {
                User u = new User(baseName + " User " + i, baseName.toLowerCase() + i + "@example.com");
                em.persist(u);
                String tn = "TKT-F-" + System.currentTimeMillis() + "-" + i;
                Ticket t = new Ticket(tn, new BigDecimal("50.00"), u, concert);
                u.addTicket(t);
                concert.addTicket(t);
                em.persist(t);
            }

            tx.commit();
            return concert;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public static Wedding createWeddingAggregate(String baseName, int ticketCount, boolean persist) {
        LocalDateTime now = LocalDateTime.now().plusDays(7);
        Venue venue = new Venue(baseName + " Venue", "456 Factory Ave", Math.max(50, ticketCount));
        Wedding wedding = new Wedding(
                baseName + " Wedding",
                now,
                now.plusHours(8),
                venue,
                "Bride " + baseName,
                "Groom " + baseName
        );
        wedding.setWeddingTheme("Theme " + baseName);

        if (!persist) {
            for (int i = 0; i < ticketCount; i++) {
                User u = new User(baseName + " Guest " + i, baseName.toLowerCase() + "g" + i + "@example.com");
                String tn = "TKT-W-" + System.currentTimeMillis() + "-" + i;
                Ticket t = new Ticket(tn, BigDecimal.ZERO, u, wedding);
                u.addTicket(t);
                wedding.addTicket(t);
            }
            return wedding;
        }

        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(venue);
            em.persist(wedding);

            for (int i = 0; i < ticketCount; i++) {
                User u = new User(baseName + " Guest " + i, baseName.toLowerCase() + "g" + i + "@example.com");
                em.persist(u);
                String tn = "TKT-W-" + System.currentTimeMillis() + "-" + i;
                Ticket t = new Ticket(tn, BigDecimal.ZERO, u, wedding);
                u.addTicket(t);
                wedding.addTicket(t);
                em.persist(t);
            }

            tx.commit();
            return wedding;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
