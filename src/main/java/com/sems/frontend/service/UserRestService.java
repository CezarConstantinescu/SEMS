package com.sems.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sems.frontend.dto.UserDTO;
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
 * REST Service Consumer for Users API.
 * Handles all communication with the backend User REST endpoints.
 */
@Service
public class UserRestService {
    private static final Logger LOGGER = Logger.getLogger(UserRestService.class.getName());

    @Value("${api.base.url:http://localhost:8080}")
    private String apiBaseUrl;

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public UserRestService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Fetch all users from the backend API.
     *
     * @return list of UserDTO objects
     */
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = new ArrayList<>();
        try {
            String url = apiBaseUrl + "/api/users";
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    users = objectMapper.readValue(responseBody, new TypeReference<List<UserDTO>>() {});
                    LOGGER.info("Successfully fetched " + users.size() + " users from API");
                } else {
                    LOGGER.log(Level.WARNING, "API returned status: " + response.code());
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error fetching users from API", e);
        }
        return users;
    }

    /**
     * Fetch a single user by ID from the backend API.
     *
     * @param userId the user ID
     * @return UserDTO object or null if not found
     */
    public UserDTO getUserById(Long userId) {
        try {
            String url = apiBaseUrl + "/api/users/" + userId;
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    return objectMapper.readValue(responseBody, UserDTO.class);
                } else {
                    LOGGER.log(Level.WARNING, "User not found: " + userId);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error fetching user " + userId, e);
        }
        return null;
    }
}
