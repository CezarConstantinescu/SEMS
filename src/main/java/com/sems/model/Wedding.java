package com.sems.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a Wedding event in the Social Events Management System.
 * Extends the base Event class with wedding-specific attributes.
 */
@Entity
@Table(name = "weddings")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Wedding extends Event {

    @Column(nullable = false)
    private String brideName;

    @Column(nullable = false)
    private String groomName;

    private String weddingTheme;

    private String cateringService;

    private Integer expectedGuests;

    private Boolean hasReception = true;

    // Default constructor required by JPA
    public Wedding() {
        super();
    }

    public Wedding(String name, LocalDateTime startDateTime, LocalDateTime endDateTime, 
                   Venue venue, String brideName, String groomName) {
        super(name, startDateTime, endDateTime, venue);
        this.brideName = brideName;
        this.groomName = groomName;
    }

    @Override
    public String getEventType() {
        return "WEDDING";
    }

    // Getters and Setters
    public String getBrideName() {
        return brideName;
    }

    public void setBrideName(String brideName) {
        this.brideName = brideName;
    }

    public String getGroomName() {
        return groomName;
    }

    public void setGroomName(String groomName) {
        this.groomName = groomName;
    }

    public String getWeddingTheme() {
        return weddingTheme;
    }

    public void setWeddingTheme(String weddingTheme) {
        this.weddingTheme = weddingTheme;
    }

    public String getCateringService() {
        return cateringService;
    }

    public void setCateringService(String cateringService) {
        this.cateringService = cateringService;
    }

    public Integer getExpectedGuests() {
        return expectedGuests;
    }

    public void setExpectedGuests(Integer expectedGuests) {
        this.expectedGuests = expectedGuests;
    }

    public Boolean getHasReception() {
        return hasReception;
    }

    public void setHasReception(Boolean hasReception) {
        this.hasReception = hasReception;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Wedding wedding = (Wedding) o;
        return Objects.equals(brideName, wedding.brideName) && Objects.equals(groomName, wedding.groomName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), brideName, groomName);
    }

    @Override
    public String toString() {
        return "Wedding{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", brideName='" + brideName + '\'' +
                ", groomName='" + groomName + '\'' +
                ", weddingTheme='" + weddingTheme + '\'' +
                ", expectedGuests=" + expectedGuests +
                ", hasReception=" + hasReception +
                ", startDateTime=" + getStartDateTime() +
                ", endDateTime=" + getEndDateTime() +
                ", venue=" + (getVenue() != null ? getVenue().getName() : "null") +
                '}';
    }
}
