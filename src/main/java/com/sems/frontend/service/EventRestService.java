package com.sems.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sems.frontend.dto.EventDTO;
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
 * REST Service Consumer for Events API.
 * Handles all communication with the backend Event REST endpoints.
 */
@Service
public class EventRestService {
    private static final Logger LOGGER = Logger.getLogger(EventRestService.class.getName());

    @Value("${api.base.url:http://localhost:8080}")
    private String apiBaseUrl;

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public EventRestService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Fetch all events from the backend API.
     *
     * @return list of EventDTO objects
     */
    public List<EventDTO> getAllEvents() {
        List<EventDTO> events = new ArrayList<>();
        try {
            String url = apiBaseUrl + "/api/events";
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    events = objectMapper.readValue(responseBody, new TypeReference<List<EventDTO>>() {});
                    LOGGER.info("Successfully fetched " + events.size() + " events from API");
                } else {
                    LOGGER.log(Level.WARNING, "API returned status: " + response.code());
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error fetching events from API", e);
        }
        return events;
    }

    /**
     * Fetch a single event by ID from the backend API.
     *
     * @param eventId the event ID
     * @return EventDTO object or null if not found
     */
    public EventDTO getEventById(Long eventId) {
        try {
            String url = apiBaseUrl + "/api/events/" + eventId;
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    return objectMapper.readValue(responseBody, EventDTO.class);
                } else {
                    LOGGER.log(Level.WARNING, "Event not found: " + eventId);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error fetching event " + eventId, e);
        }
        return null;
    }
}
