package com.sems.repository;

import com.sems.factory.EventAggregateFactory;
import com.sems.model.Event;
import com.sems.persistence.EntityManagerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventRepositoryTest {

    @AfterAll
    static void cleanup() {
        EntityManagerUtil.close();
    }

    @Test
    void createAndFindEvent() {
        String base = "RepoEventTest" + System.currentTimeMillis();
        Event concert = EventAggregateFactory.createConcertAggregate(base, 2, true);

        EventRepository repo = new com.sems.repository.impl.EventRepositoryImpl();

        Event loaded = repo.findByIdWithVenue(concert.getId());
        assertNotNull(loaded);
        assertNotNull(loaded.getVenue());

        List<Event> byName = repo.findByName(concert.getName());
        assertFalse(byName.isEmpty());
    }
}
