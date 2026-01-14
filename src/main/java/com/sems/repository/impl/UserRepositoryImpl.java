package com.sems.repository.impl;

import org.springframework.stereotype.Repository;

import com.sems.model.User;
import com.sems.persistence.EntityManagerUtil;
import com.sems.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Override
    public User findById(Long id) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> findAll() {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            TypedQuery<User> q = em.createQuery("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.tickets t LEFT JOIN FETCH t.event", User.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void save(User user) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (user.getId() == null) em.persist(user);
            else em.merge(user);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(User user) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User m = em.find(User.class, user.getId());
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
    public User findByEmail(String email) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            TypedQuery<User> q = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            q.setParameter("email", email);
            List<User> res = q.getResultList();
            return res.isEmpty() ? null : res.get(0);
        } finally {
            em.close();
        }
    }
}
