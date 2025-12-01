package com.sems.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utility class for managing EntityManager instances.
 * Provides a singleton EntityManagerFactory and methods for obtaining EntityManager instances.
 */
public class EntityManagerUtil {

    private static final String PERSISTENCE_UNIT_NAME = "SEMS-PU";
    private static EntityManagerFactory entityManagerFactory;

    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Exception e) {
            System.err.println("Initial EntityManagerFactory creation failed: " + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private EntityManagerUtil() {
    }

    /**
     * Returns the singleton EntityManagerFactory instance.
     * @return the EntityManagerFactory
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    /**
     * Creates a new EntityManager instance.
     * @return a new EntityManager
     */
    public static EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    /**
     * Closes the EntityManagerFactory.
     * Should be called when the application shuts down.
     */
    public static void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
