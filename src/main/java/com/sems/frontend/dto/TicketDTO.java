package com.sems.frontend.dto;

/**
 * DTO for Ticket resource from REST API (client-side representation).
 */
public class TicketDTO {
    private Long id;
    private Long eventId;
    private String eventTitle;
    private Long userId;
    private String userName;
    private String status; // AVAILABLE, SOLD, RESERVED

    public TicketDTO() {}

    public TicketDTO(Long id, Long eventId, String eventTitle, Long userId, String userName, String status) {
        this.id = id;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.userId = userId;
        this.userName = userName;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
                "id=" + id +
                ", eventTitle='" + eventTitle + '\'' +
                ", userName='" + userName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
