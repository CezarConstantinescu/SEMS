package com.sems.repository;

import com.sems.model.Venue;

import java.util.List;

public interface VenueRepository {

    Venue findById(Long id);

    List<Venue> findAll();

    void save(Venue venue);

    void delete(Venue venue);

    Venue findByName(String name);
}
