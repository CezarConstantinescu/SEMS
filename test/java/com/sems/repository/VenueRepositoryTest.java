package com.sems.repository;

import com.sems.factory.VenueAggregateFactory;
import com.sems.model.Venue;
import com.sems.persistence.EntityManagerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VenueRepositoryTest {

    @AfterAll
    static void cleanup() {
        EntityManagerUtil.close();
    }

    @Test
    void createVenueWithEvents() {
        String base = "VenueRepo" + System.currentTimeMillis();
        Venue v = VenueAggregateFactory.createVenueWithEvents(base, 2, 2, true);

        VenueRepository repo = new com.sems.repository.impl.VenueRepositoryImpl();
        Venue found = repo.findById(v.getId());
        assertNotNull(found);
        assertEquals(v.getName(), found.getName());
    }
}
