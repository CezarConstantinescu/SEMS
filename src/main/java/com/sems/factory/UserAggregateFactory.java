package com.sems.factory;

import com.sems.model.Event;
import com.sems.model.Ticket;
import com.sems.model.User;
import com.sems.persistence.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.math.BigDecimal;

/**
 * Factory helpers to create User aggregates (User + Tickets + optional Events).
 */
public final class UserAggregateFactory {
    private UserAggregateFactory() {}

    public static User createUserWithTickets(String name, int ticketCount, Event eventIfAny, boolean persist) {
        User user = new User(name, name.toLowerCase().replaceAll("\\s+",".") + "@example.com");

        if (!persist) {
            for (int i = 0; i < ticketCount; i++) {
                String tn = "UTKT-" + System.currentTimeMillis() + "-" + i;
                Ticket t = new Ticket(tn, new BigDecimal("30.00"), user, eventIfAny);
                user.addTicket(t);
                if (eventIfAny != null) eventIfAny.addTicket(t);
            }
            return user;
        }

        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(user);

            for (int i = 0; i < ticketCount; i++) {
                String tn = "UTKT-" + System.currentTimeMillis() + "-" + i;
                Ticket t = new Ticket(tn, new BigDecimal("30.00"), user, eventIfAny);
                user.addTicket(t);
                em.persist(t);
            }

            tx.commit();
            return user;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
