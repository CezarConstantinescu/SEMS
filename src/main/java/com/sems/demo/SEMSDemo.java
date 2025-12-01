package com.sems.demo;

import com.sems.model.*;
import com.sems.persistence.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Demo class that demonstrates the Social Events Management System functionality.
 * Shows how to create, persist, and query events using Jakarta Persistence and Hibernate.
 */
public class SEMSDemo {

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("Social Events Management System - Demo");
        System.out.println("==============================================\n");

        try {
            // Create sample data
            createSampleData();

            // Query and display events
            queryAndDisplayEvents();

            // Query by event type
            queryEventsByType();

            // Query users with tickets
            queryUsersWithTickets();

        } finally {
            // Close the EntityManagerFactory
            EntityManagerUtil.close();
            System.out.println("\n==============================================");
            System.out.println("Demo completed successfully!");
            System.out.println("==============================================");
        }
    }

    /**
     * Creates sample data including venues, events, users, and tickets.
     */
    private static void createSampleData() {
        System.out.println("--- Creating Sample Data ---\n");

        EntityManager em = EntityManagerUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Create venues
            Venue concertHall = new Venue("Grand Concert Hall", "123 Music Avenue, New York, NY", 5000);
            concertHall.setDescription("A premier concert venue with state-of-the-art acoustics");

            Venue weddingVenue = new Venue("Rose Garden Estate", "456 Garden Lane, Los Angeles, CA", 300);
            weddingVenue.setDescription("Elegant outdoor venue perfect for romantic weddings");

            Venue arena = new Venue("City Arena", "789 Sports Blvd, Chicago, IL", 20000);
            arena.setDescription("Large multi-purpose arena for major events");

            em.persist(concertHall);
            em.persist(weddingVenue);
            em.persist(arena);

            // Create concerts
            Concert rockConcert = new Concert(
                    "Rock Night 2024",
                    LocalDateTime.of(2024, 6, 15, 20, 0),
                    LocalDateTime.of(2024, 6, 15, 23, 30),
                    concertHall,
                    "The Rolling Stones",
                    "Rock"
            );
            rockConcert.setTourName("Hackney Diamonds Tour");
            rockConcert.setDescription("Experience legendary rock music live!");

            Concert popConcert = new Concert(
                    "Pop Sensation Tour",
                    LocalDateTime.of(2024, 7, 20, 19, 0),
                    LocalDateTime.of(2024, 7, 20, 22, 0),
                    arena,
                    "Taylor Swift",
                    "Pop"
            );
            popConcert.setTourName("Eras Tour");
            popConcert.setIsSeatedEvent(false);
            popConcert.setDescription("The biggest pop tour of the decade!");

            em.persist(rockConcert);
            em.persist(popConcert);

            // Create weddings
            Wedding summerWedding = new Wedding(
                    "Johnson-Smith Wedding",
                    LocalDateTime.of(2024, 8, 10, 14, 0),
                    LocalDateTime.of(2024, 8, 10, 23, 0),
                    weddingVenue,
                    "Emily Johnson",
                    "Michael Smith"
            );
            summerWedding.setWeddingTheme("Rustic Garden");
            summerWedding.setCateringService("Gourmet Delights Catering");
            summerWedding.setExpectedGuests(150);
            summerWedding.setDescription("A beautiful summer garden wedding");

            Wedding winterWedding = new Wedding(
                    "Brown-Davis Wedding",
                    LocalDateTime.of(2024, 12, 21, 15, 0),
                    LocalDateTime.of(2024, 12, 21, 22, 0),
                    weddingVenue,
                    "Sarah Brown",
                    "James Davis"
            );
            winterWedding.setWeddingTheme("Winter Wonderland");
            winterWedding.setCateringService("Elite Events Catering");
            winterWedding.setExpectedGuests(200);
            winterWedding.setHasReception(true);
            winterWedding.setDescription("A magical winter wedding celebration");

            em.persist(summerWedding);
            em.persist(winterWedding);

            // Create users
            User user1 = new User("John Doe", "john.doe@email.com");
            User user2 = new User("Jane Smith", "jane.smith@email.com");
            User user3 = new User("Bob Wilson", "bob.wilson@email.com");

            em.persist(user1);
            em.persist(user2);
            em.persist(user3);

            // Create tickets
            Ticket ticket1 = new Ticket("TKT-001", new BigDecimal("150.00"), user1, rockConcert);
            Ticket ticket2 = new Ticket("TKT-002", new BigDecimal("150.00"), user1, popConcert);
            Ticket ticket3 = new Ticket("TKT-003", new BigDecimal("200.00"), user2, popConcert);
            Ticket ticket4 = new Ticket("TKT-004", new BigDecimal("0.00"), user3, summerWedding);

            user1.addTicket(ticket1);
            user1.addTicket(ticket2);
            user2.addTicket(ticket3);
            user3.addTicket(ticket4);

            em.persist(ticket1);
            em.persist(ticket2);
            em.persist(ticket3);
            em.persist(ticket4);

            // Add users as venue attendees
            concertHall.addAttendee(user1);
            arena.addAttendee(user1);
            arena.addAttendee(user2);
            weddingVenue.addAttendee(user3);

            tx.commit();
            System.out.println("Sample data created successfully!\n");

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Queries and displays all events.
     */
    private static void queryAndDisplayEvents() {
        System.out.println("--- All Events ---\n");

        EntityManager em = EntityManagerUtil.createEntityManager();

        try {
            TypedQuery<Event> query = em.createQuery(
                    "SELECT e FROM Event e JOIN FETCH e.venue ORDER BY e.startDateTime",
                    Event.class
            );
            List<Event> events = query.getResultList();

            for (Event event : events) {
                System.out.println("Event: " + event.getName());
                System.out.println("  Type: " + event.getEventType());
                System.out.println("  Venue: " + event.getVenue().getName());
                System.out.println("  Date: " + event.getStartDateTime().toLocalDate());
                System.out.println("  Time: " + event.getStartDateTime().toLocalTime() + 
                                   " - " + event.getEndDateTime().toLocalTime());
                System.out.println();
            }

        } finally {
            em.close();
        }
    }

    /**
     * Queries events by their specific type (Concert or Wedding).
     */
    private static void queryEventsByType() {
        System.out.println("--- Concerts ---\n");

        EntityManager em = EntityManagerUtil.createEntityManager();

        try {
            // Query concerts
            TypedQuery<Concert> concertQuery = em.createQuery(
                    "SELECT c FROM Concert c JOIN FETCH c.venue",
                    Concert.class
            );
            List<Concert> concerts = concertQuery.getResultList();

            for (Concert concert : concerts) {
                System.out.println("Concert: " + concert.getName());
                System.out.println("  Artist: " + concert.getArtist());
                System.out.println("  Genre: " + concert.getGenre());
                System.out.println("  Tour: " + concert.getTourName());
                System.out.println("  Venue: " + concert.getVenue().getName() + 
                                   " (Capacity: " + concert.getVenue().getCapacity() + ")");
                System.out.println();
            }

            System.out.println("--- Weddings ---\n");

            // Query weddings
            TypedQuery<Wedding> weddingQuery = em.createQuery(
                    "SELECT w FROM Wedding w JOIN FETCH w.venue",
                    Wedding.class
            );
            List<Wedding> weddings = weddingQuery.getResultList();

            for (Wedding wedding : weddings) {
                System.out.println("Wedding: " + wedding.getName());
                System.out.println("  Couple: " + wedding.getBrideName() + " & " + wedding.getGroomName());
                System.out.println("  Theme: " + wedding.getWeddingTheme());
                System.out.println("  Expected Guests: " + wedding.getExpectedGuests());
                System.out.println("  Venue: " + wedding.getVenue().getName());
                System.out.println("  Catering: " + wedding.getCateringService());
                System.out.println();
            }

        } finally {
            em.close();
        }
    }

    /**
     * Queries users and their tickets.
     */
    private static void queryUsersWithTickets() {
        System.out.println("--- Users and Their Tickets ---\n");

        EntityManager em = EntityManagerUtil.createEntityManager();

        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.tickets",
                    User.class
            );
            List<User> users = query.getResultList();

            for (User user : users) {
                System.out.println("User: " + user.getName() + " (" + user.getEmail() + ")");
                System.out.println("  Tickets:");
                
                if (user.getTickets().isEmpty()) {
                    System.out.println("    No tickets purchased");
                } else {
                    for (Ticket ticket : user.getTickets()) {
                        // Need to fetch event details
                        EntityManager em2 = EntityManagerUtil.createEntityManager();
                        Event event = em2.find(Event.class, ticket.getEvent().getId());
                        System.out.println("    - " + ticket.getTicketNumber() + 
                                           ": " + event.getName() + 
                                           " ($" + ticket.getPrice() + ")");
                        em2.close();
                    }
                }
                System.out.println();
            }

        } finally {
            em.close();
        }
    }
}
