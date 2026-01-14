package com.sems.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sems.frontend.dto.VenueDTO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * REST Service Consumer for Venues API.
 * Handles all communication with the backend Venue REST endpoints.
 */
@Service
public class VenueRestService {
    private static final Logger LOGGER = Logger.getLogger(VenueRestService.class.getName());

    @Value("${api.base.url:http://localhost:8080}")
    private String apiBaseUrl;

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public VenueRestService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Fetch all venues from the backend API.
     *
     * @return list of VenueDTO objects
     */
    public List<VenueDTO> getAllVenues() {
        List<VenueDTO> venues = new ArrayList<>();
        try {
            String url = apiBaseUrl + "/api/venues";
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    venues = objectMapper.readValue(responseBody, new TypeReference<List<VenueDTO>>() {});
                    LOGGER.info("Successfully fetched " + venues.size() + " venues from API");
                } else {
                    LOGGER.log(Level.WARNING, "API returned status: " + response.code());
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error fetching venues from API", e);
        }
        return venues;
    }

    /**
     * Fetch a single venue by ID from the backend API.
     *
     * @param venueId the venue ID
     * @return VenueDTO object or null if not found
     */
    public VenueDTO getVenueById(Long venueId) {
        try {
            String url = apiBaseUrl + "/api/venues/" + venueId;
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    return objectMapper.readValue(responseBody, VenueDTO.class);
                } else {
                    LOGGER.log(Level.WARNING, "Venue not found: " + venueId);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error fetching venue " + venueId, e);
        }
        return null;
    }
}
