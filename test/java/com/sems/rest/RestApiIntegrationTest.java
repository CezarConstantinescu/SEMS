package com.sems.rest;

import com.sems.model.Concert;
import com.sems.model.Ticket;
import com.sems.model.User;
import com.sems.model.Venue;
import com.sems.persistence.EntityManagerUtil;
import com.sems.repository.EventRepository;
import com.sems.repository.TicketRepository;
import com.sems.repository.UserRepository;
import com.sems.repository.VenueRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private VenueRepository venueRepository;

    @BeforeEach
    void setup() {
        // no-op; tests will create their own data
    }

    @AfterAll
    static void cleanup() {
        EntityManagerUtil.close();
    }

    private String baseUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void eventsEndpointReturnsCreatedEvent() {
        Venue v = new Venue("REST Venue" + System.currentTimeMillis(), "Addr", 100);
        venueRepository.save(v);

        Concert c = new Concert("REST Concert " + System.currentTimeMillis(),
                java.time.LocalDateTime.now().plusDays(1),
                java.time.LocalDateTime.now().plusDays(1).plusHours(3),
                v,
                "Artist",
                "Rock");
        eventRepository.save(c);

        ResponseEntity<String> resp = restTemplate.getForEntity(baseUrl("/api/events"), String.class);
        assertEquals(200, resp.getStatusCodeValue());
        assertTrue(resp.getBody().contains(c.getName()));
    }

    @Test
    void usersEndpointReturnsCreatedUser() {
        User u = new User("REST User" + System.currentTimeMillis(), "rest.user" + System.currentTimeMillis() + "@example.com");
        userRepository.save(u);

        ResponseEntity<String> resp = restTemplate.getForEntity(baseUrl("/api/users"), String.class);
        assertEquals(200, resp.getStatusCodeValue());
        assertTrue(resp.getBody().contains(u.getEmail()));
    }

    @Test
    void ticketsEndpointReturnsCreatedTicket() {
        Venue v = new Venue("Ticket Venue" + System.currentTimeMillis(), "Addr", 100);
        venueRepository.save(v);

        Concert c = new Concert("Ticket Concert " + System.currentTimeMillis(),
                java.time.LocalDateTime.now().plusDays(2),
                java.time.LocalDateTime.now().plusDays(2).plusHours(2),
                v,
                "Artist",
                "Jazz");
        eventRepository.save(c);

        User u = new User("Ticket User" + System.currentTimeMillis(), "tuser" + System.currentTimeMillis() + "@example.com");
        userRepository.save(u);

        Ticket t = new Ticket("REST-TKT-" + System.currentTimeMillis(), new BigDecimal("33.33"), u, c);
        ticketRepository.save(t);

        ResponseEntity<String> resp = restTemplate.getForEntity(baseUrl("/api/tickets"), String.class);
        assertEquals(200, resp.getStatusCodeValue());
        assertTrue(resp.getBody().contains(t.getTicketNumber()));
    }

    @Test
    void venuesEndpointReturnsCreatedVenue() {
        Venue v = new Venue("Venue REST " + System.currentTimeMillis(), "Addr", 250);
        venueRepository.save(v);

        ResponseEntity<String> resp = restTemplate.getForEntity(baseUrl("/api/venues"), String.class);
        assertEquals(200, resp.getStatusCodeValue());
        assertTrue(resp.getBody().contains(v.getName()));
    }
}
