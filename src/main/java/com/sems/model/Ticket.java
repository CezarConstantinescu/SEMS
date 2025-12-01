package com.sems.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a ticket for an event in the Social Events Management System.
 * A ticket belongs to a user and is associated with an event.
 */
@Entity
@Table(name = "tickets")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ticketNumber;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime purchaseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonBackReference(value = "event-tickets")
    private Event event;

    // Default constructor required by JPA
    public Ticket() {
    }

    public Ticket(String ticketNumber, BigDecimal price, User user, Event event) {
        this.ticketNumber = ticketNumber;
        this.price = price;
        this.purchaseDate = LocalDateTime.now();
        this.user = user;
        this.event = event;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) && Objects.equals(ticketNumber, ticket.ticketNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ticketNumber);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", ticketNumber='" + ticketNumber + '\'' +
                ", price=" + price +
                ", purchaseDate=" + purchaseDate +
                ", status=" + status +
                '}';
    }
}
