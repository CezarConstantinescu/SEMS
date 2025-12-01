package com.sems.repository.impl;

import org.springframework.stereotype.Repository;

import com.sems.model.Event;
import com.sems.persistence.EntityManagerUtil;
import com.sems.repository.EventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Repository
public class EventRepositoryImpl implements EventRepository {
    
    // Mark as a Spring component to allow injection into controllers/services
    // Note: This repository still uses EntityManagerUtil for persistence.

    @Override
    public Event findById(Long id) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            return em.find(Event.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public Event findByIdWithVenue(Long id) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            TypedQuery<Event> q = em.createQuery("SELECT e FROM Event e JOIN FETCH e.venue WHERE e.id = :id", Event.class);
            q.setParameter("id", id);
            List<Event> res = q.getResultList();
            return res.isEmpty() ? null : res.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Event> findAll() {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            TypedQuery<Event> q = em.createQuery("SELECT e FROM Event e ORDER BY e.startDateTime", Event.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Event event) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (event.getId() == null) {
                em.persist(event);
            } else {
                em.merge(event);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Event event) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Event managed = em.find(Event.class, event.getId());
            if (managed != null) em.remove(managed);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Event> findByName(String name) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            TypedQuery<Event> q = em.createQuery("SELECT e FROM Event e WHERE e.name = :name", Event.class);
            q.setParameter("name", name);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
