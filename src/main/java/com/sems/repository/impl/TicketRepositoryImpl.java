package com.sems.repository.impl;

import org.springframework.stereotype.Repository;

import com.sems.model.Ticket;
import com.sems.persistence.EntityManagerUtil;
import com.sems.repository.TicketRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Repository
public class TicketRepositoryImpl implements TicketRepository {

    @Override
    public Ticket findById(Long id) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            return em.find(Ticket.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Ticket> findAll() {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            TypedQuery<Ticket> q = em.createQuery("SELECT t FROM Ticket t", Ticket.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Ticket ticket) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // Ensure referenced User and Event are managed in this persistence context
            if (ticket.getUser() != null && ticket.getUser().getId() != null) {
                ticket.setUser(em.find(ticket.getUser().getClass(), ticket.getUser().getId()));
            }
            if (ticket.getEvent() != null && ticket.getEvent().getId() != null) {
                ticket.setEvent(em.find(ticket.getEvent().getClass(), ticket.getEvent().getId()));
            }

            if (ticket.getId() == null) em.persist(ticket);
            else em.merge(ticket);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Ticket ticket) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Ticket m = em.find(Ticket.class, ticket.getId());
            if (m != null) em.remove(m);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Ticket findByTicketNumber(String ticketNumber) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            TypedQuery<Ticket> q = em.createQuery("SELECT t FROM Ticket t WHERE t.ticketNumber = :num", Ticket.class);
            q.setParameter("num", ticketNumber);
            List<Ticket> res = q.getResultList();
            return res.isEmpty() ? null : res.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public long countByEventId(Long eventId) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            TypedQuery<Long> q = em.createQuery("SELECT COUNT(t) FROM Ticket t WHERE t.event.id = :eid", Long.class);
            q.setParameter("eid", eventId);
            Long res = q.getSingleResult();
            return res == null ? 0L : res.longValue();
        } finally {
            em.close();
        }
    }
}
