package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import java.time.ZonedDateTime;

/**
 * Data model for storing appointment data as it is moved between the data and business layers.
 */
public class Appointment extends Base {
    private String title;
    private String description;
    private String location;
    private String type;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private int customerId;
    private int userId;
    private int contactId;

    /**
     * Constructor accepts no parameters to allow for empty class creation and dynamic property assignment
     * post-creation.
     */
    public Appointment() {
    }

    /**
     * Constructor accepts all object properties and all <code>Base</code> properties to allow for easy object
     * creation when reading Appointment entities into the model with the DAOs.
     *
     * @param id          Unique identifier, <em>should</em> be assigned by the data layer
     * @param title       Title of the appointment
     * @param description Description of the appointment
     * @param location    Location where the appointment will take place
     * @param type        Type of appointment
     * @param start       Start date and time of the appointment
     * @param end         End date and time of the appointment
     * @param customerId  Unique id for the customer associated with the appointment
     * @param userId      Unique id for the user associated with the appointment
     * @param contactId   Unique id for the contact associated with the appointment
     * @param created     The date and time the appointment was created at, <em>should</em> be assigned at data layer
     * @param createdBy   The source that created the appointment
     * @param updated     The date and time the appointment was last updated at, <em>should</em> be assigned at data
     *                    layer
     * @param updatedBy   The source that updated the appointment
     */
    public Appointment(int id, String title, String description, String location, String type, ZonedDateTime start,
                       ZonedDateTime end, int customerId, int userId, int contactId, ZonedDateTime created,
                       String createdBy, ZonedDateTime updated, String updatedBy) {
        super(id, created, createdBy, updated, updatedBy);
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

    /**
     * Constructor accepts all Appointment properties but none of the <code>Base</code> properties. Allows easy
     * creation of Appointment objects with data coming from the view layer.
     *
     * @param title       Title of the appointment
     * @param description Description of the appointment
     * @param location    Location where the appointment will take place
     * @param type        Type of appointment
     * @param start       Start date and time of the appointment
     * @param end         End date and time of the appointment
     * @param customerId  Unique id for the customer associated with the appointment
     * @param userId      Unique id for the user associated with the appointment
     * @param contactId   Unique id for the contact associated with the appointment
     */
    public Appointment(String title, String description, String location, String type, ZonedDateTime start,
                       ZonedDateTime end, int customerId, int userId, int contactId) {
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

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", type='" + type + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", customerId=" + customerId +
                ", userId=" + userId +
                ", contactId=" + contactId +
                ", id=" + id +
                ", created=" + created +
                ", createdBy='" + createdBy + '\'' +
                ", updated=" + updated +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
