package com.sems.repository.impl;

import org.springframework.stereotype.Repository;

import com.sems.model.Venue;
import com.sems.persistence.EntityManagerUtil;
import com.sems.repository.VenueRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Repository
public class VenueRepositoryImpl implements VenueRepository {

    @Override
    public Venue findById(Long id) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            return em.find(Venue.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Venue> findAll() {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            TypedQuery<Venue> q = em.createQuery("SELECT v FROM Venue v", Venue.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Venue venue) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (venue.getId() == null) em.persist(venue);
            else em.merge(venue);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Venue venue) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Venue m = em.find(Venue.class, venue.getId());
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
    public Venue findByName(String name) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            TypedQuery<Venue> q = em.createQuery("SELECT v FROM Venue v WHERE v.name = :name", Venue.class);
            q.setParameter("name", name);
            List<Venue> res = q.getResultList();
            return res.isEmpty() ? null : res.get(0);
        } finally {
            em.close();
        }
    }
}
