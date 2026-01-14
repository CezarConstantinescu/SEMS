package com.sems.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sems.frontend.dto.TicketDTO;
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
 * REST Service Consumer for Tickets API.
 * Handles all communication with the backend Ticket REST endpoints.
 */
@Service
public class TicketRestService {
    private static final Logger LOGGER = Logger.getLogger(TicketRestService.class.getName());

    @Value("${api.base.url:http://localhost:8080}")
    private String apiBaseUrl;

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public TicketRestService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Fetch all tickets from the backend API.
     *
     * @return list of TicketDTO objects
     */
    public List<TicketDTO> getAllTickets() {
        List<TicketDTO> tickets = new ArrayList<>();
        try {
            String url = apiBaseUrl + "/api/tickets";
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    tickets = objectMapper.readValue(responseBody, new TypeReference<List<TicketDTO>>() {});
                    LOGGER.info("Successfully fetched " + tickets.size() + " tickets from API");
                } else {
                    LOGGER.log(Level.WARNING, "API returned status: " + response.code());
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error fetching tickets from API", e);
        }
        return tickets;
    }

    /**
     * Fetch a single ticket by ID from the backend API.
     *
     * @param ticketId the ticket ID
     * @return TicketDTO object or null if not found
     */
    public TicketDTO getTicketById(Long ticketId) {
        try {
            String url = apiBaseUrl + "/api/tickets/" + ticketId;
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    return objectMapper.readValue(responseBody, TicketDTO.class);
                } else {
                    LOGGER.log(Level.WARNING, "Ticket not found: " + ticketId);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error fetching ticket " + ticketId, e);
        }
        return null;
    }
}
