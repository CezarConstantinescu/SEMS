package com.sems.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a Concert event in the Social Events Management System.
 * Extends the base Event class with concert-specific attributes.
 */
@Entity
@Table(name = "concerts")
public class Concert extends Event {

    @Column(nullable = false)
    private String artist;

    @Column(nullable = false)
    private String genre;

    private String tourName;

    private Boolean isSeatedEvent = true;

    // Default constructor required by JPA
    public Concert() {
        super();
    }

    public Concert(String name, LocalDateTime startDateTime, LocalDateTime endDateTime, 
                   Venue venue, String artist, String genre) {
        super(name, startDateTime, endDateTime, venue);
        this.artist = artist;
        this.genre = genre;
    }

    @Override
    public String getEventType() {
        return "CONCERT";
    }

    // Getters and Setters
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public Boolean getIsSeatedEvent() {
        return isSeatedEvent;
    }

    public void setIsSeatedEvent(Boolean seatedEvent) {
        isSeatedEvent = seatedEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Concert concert = (Concert) o;
        return Objects.equals(artist, concert.artist) && Objects.equals(genre, concert.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), artist, genre);
    }

    @Override
    public String toString() {
        return "Concert{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", artist='" + artist + '\'' +
                ", genre='" + genre + '\'' +
                ", tourName='" + tourName + '\'' +
                ", isSeatedEvent=" + isSeatedEvent +
                ", startDateTime=" + getStartDateTime() +
                ", endDateTime=" + getEndDateTime() +
                ", venue=" + (getVenue() != null ? getVenue().getName() : "null") +
                '}';
    }
}
