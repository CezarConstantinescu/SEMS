package com.sems.factory;

import com.sems.factory.EventAggregateFactory;
import com.sems.factory.UserAggregateFactory;
import com.sems.factory.VenueAggregateFactory;
import com.sems.model.Concert;
import com.sems.model.User;
import com.sems.model.Venue;
import com.sems.persistence.EntityManagerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AggregateFactoryTest {

    @AfterAll
    static void cleanup() {
        EntityManagerUtil.close();
    }

    @Test
    void createConcertInMemory() {
        Concert c = EventAggregateFactory.createConcertAggregate("AFTest", 3, false);
        assertNotNull(c);
        assertEquals(3, c.getTickets().size());
    }

    @Test
    void createUserPersisted() {
        User u = UserAggregateFactory.createUserWithTickets("AFUser" + System.currentTimeMillis(), 2, null, true);
        assertNotNull(u.getId());
    }

    @Test
    void createVenuePersisted() {
        Venue v = VenueAggregateFactory.createVenueWithEvents("AFVenue" + System.currentTimeMillis(), 1, 1, true);
        assertNotNull(v.getId());
    }
}
