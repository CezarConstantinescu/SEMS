package com.sems.controller;

import com.sems.dto.VenueResponseDTO;
import com.sems.model.Venue;
import com.sems.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    private final VenueRepository venueRepository;

    @Autowired
    public VenueController(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @GetMapping
    public List<VenueResponseDTO> list() {
        return venueRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public VenueResponseDTO get(@PathVariable Long id) {
        Venue venue = venueRepository.findById(id);
        return venue != null ? toDTO(venue) : null;
    }

    /**
     * Convert Venue entity to VenueResponseDTO to avoid lazy initialization.
     */
    private VenueResponseDTO toDTO(Venue venue) {
        return new VenueResponseDTO(
                venue.getId(),
                venue.getName(),
                venue.getAddress(),
                venue.getCapacity()
        );
    }
}
