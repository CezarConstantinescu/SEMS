package com.sems.repository;

import com.sems.model.User;

import java.util.List;

public interface UserRepository {

    User findById(Long id);

    List<User> findAll();

    void save(User user);

    void delete(User user);

    User findByEmail(String email);
}
