package com.sems.repository;

import com.sems.factory.UserAggregateFactory;
import com.sems.model.User;
import com.sems.persistence.EntityManagerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {

    @AfterAll
    static void cleanup() {
        EntityManagerUtil.close();
    }

    @Test
    void createAndFindUserByEmail() {
        String name = "RepoUser" + System.currentTimeMillis();
        User u = UserAggregateFactory.createUserWithTickets(name, 1, null, true);

        UserRepository repo = new com.sems.repository.impl.UserRepositoryImpl();

        User found = repo.findByEmail(u.getEmail());
        assertNotNull(found);
        assertEquals(u.getEmail(), found.getEmail());
    }
}
