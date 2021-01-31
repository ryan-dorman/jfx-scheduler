package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import java.time.ZonedDateTime;

/**
 * A model for storing appointment data as it is moved between the data and business layers.
 */
public class Appointment {
    private long id;
    private String title;
    private String description;
    private String location;
    private String type;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private long customerId;
    private long userId;
    private long contactId;

    /**
     * No-op constructor, accepts no parameters.
     */
    public Appointment() {
    }

    /**
     * Constructor accepts appointment id to allow for storage of the auto-generated id coming from the data layer.
     * @param id Unique auto generated id created by data layer.
     * @param title The title of the appointment.
     * @param description A description of the appointment.
     * @param location The location that the appointment will occur at.
     * @param type The type of appointment.
     * @param start When the appointment is scheduled to start.
     * @param end When the appointment is scheduled to end.
     * @param customerId The unique customer id for the customer associated with this appointment.
     * @param userId The unique user id for the used associated with this appointment.
     * @param contactId The unique contact id for the contact associated with this appointment.
     */
    public Appointment(long id, String title, String description, String location, String type, ZonedDateTime start, ZonedDateTime end, long customerId, long userId, long contactId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    public Appointment(String title, String description, String location, String type, ZonedDateTime start, ZonedDateTime end, long customerId, long userId, long contactId) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }
}
