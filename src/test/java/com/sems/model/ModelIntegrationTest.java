package com.sems.model;

import com.sems.persistence.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the SEMS domain model using JPA and H2 database.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ModelIntegrationTest {

    private static Venue testVenue;
    private static User testUser;

    @BeforeAll
    static void setUp() {
        // Create test data
        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            testVenue = new Venue("Test Venue", "123 Test Street", 1000);
            em.persist(testVenue);

            testUser = new User("Test User", "test@email.com");
            em.persist(testUser);

            tx.commit();
        } finally {
            em.close();
        }
    }

    @AfterAll
    static void tearDown() {
        EntityManagerUtil.close();
    }

    @Test
    @Order(1)
    void testCreateConcert() {
        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Concert concert = new Concert(
                    "Test Concert",
                    LocalDateTime.of(2024, 6, 1, 20, 0),
                    LocalDateTime.of(2024, 6, 1, 23, 0),
                    testVenue,
                    "Test Artist",
                    "Rock"
            );
            concert.setTourName("Test Tour");

            em.persist(concert);
            tx.commit();

            assertNotNull(concert.getId());
            assertEquals("CONCERT", concert.getEventType());
            assertEquals("Test Artist", concert.getArtist());
        } finally {
            em.close();
        }
    }

    @Test
    @Order(2)
    void testCreateWedding() {
        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Wedding wedding = new Wedding(
                    "Test Wedding",
                    LocalDateTime.of(2024, 7, 1, 14, 0),
                    LocalDateTime.of(2024, 7, 1, 22, 0),
                    testVenue,
                    "Jane",
                    "John"
            );
            wedding.setWeddingTheme("Garden");
            wedding.setExpectedGuests(100);

            em.persist(wedding);
            tx.commit();

            assertNotNull(wedding.getId());
            assertEquals("WEDDING", wedding.getEventType());
            assertEquals("Jane", wedding.getBrideName());
        } finally {
            em.close();
        }
    }

    @Test
    @Order(3)
    void testQueryAllEvents() {
        EntityManager em = EntityManagerUtil.createEntityManager();

        try {
            TypedQuery<Event> query = em.createQuery(
                    "SELECT e FROM Event e ORDER BY e.startDateTime",
                    Event.class
            );
            List<Event> events = query.getResultList();

            assertFalse(events.isEmpty());
            assertTrue(events.size() >= 2); // At least concert and wedding

            // Check polymorphism
            boolean hasConcert = false;
            boolean hasWedding = false;
            for (Event event : events) {
                if (event instanceof Concert) {
                    hasConcert = true;
                    assertEquals("CONCERT", event.getEventType());
                } else if (event instanceof Wedding) {
                    hasWedding = true;
                    assertEquals("WEDDING", event.getEventType());
                }
            }
            assertTrue(hasConcert, "Should have at least one Concert");
            assertTrue(hasWedding, "Should have at least one Wedding");
        } finally {
            em.close();
        }
    }

    @Test
    @Order(4)
    void testCreateTicket() {
        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Fetch an event
            TypedQuery<Event> query = em.createQuery(
                    "SELECT e FROM Event e",
                    Event.class
            );
            Event event = query.getResultList().get(0);

            // Fetch user
            User user = em.find(User.class, testUser.getId());

            Ticket ticket = new Ticket("TEST-001", new BigDecimal("50.00"), user, event);
            em.persist(ticket);
            tx.commit();

            assertNotNull(ticket.getId());
            assertEquals(TicketStatus.ACTIVE, ticket.getStatus());
        } finally {
            em.close();
        }
    }

    @Test
    @Order(5)
    void testVenueHasUsers() {
        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Venue venue = em.find(Venue.class, testVenue.getId());
            User user = em.find(User.class, testUser.getId());

            venue.addAttendee(user);
            tx.commit();

            // Verify
            em.clear();
            Venue refreshedVenue = em.find(Venue.class, testVenue.getId());
            assertFalse(refreshedVenue.getAttendees().isEmpty());
        } finally {
            em.close();
        }
    }

    @Test
    @Order(6)
    void testInheritanceJoinedStrategy() {
        EntityManager em = EntityManagerUtil.createEntityManager();

        try {
            // Query specific subtypes
            TypedQuery<Concert> concertQuery = em.createQuery(
                    "SELECT c FROM Concert c",
                    Concert.class
            );
            List<Concert> concerts = concertQuery.getResultList();
            assertFalse(concerts.isEmpty());

            TypedQuery<Wedding> weddingQuery = em.createQuery(
                    "SELECT w FROM Wedding w",
                    Wedding.class
            );
            List<Wedding> weddings = weddingQuery.getResultList();
            assertFalse(weddings.isEmpty());

            // All events (polymorphic query)
            TypedQuery<Event> eventQuery = em.createQuery(
                    "SELECT e FROM Event e",
                    Event.class
            );
            List<Event> allEvents = eventQuery.getResultList();

            assertTrue(allEvents.size() >= concerts.size() + weddings.size());
        } finally {
            em.close();
        }
    }
}
