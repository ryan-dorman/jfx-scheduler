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
    private Customer customer;
    private User user;
    private Contact contact;

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
     * @param customer    Customer associated with the appointment
     * @param user        User associated with the appointment
     * @param contact     Contact associated with the appointment
     * @param created     The date and time the appointment was created at, <em>should</em> be assigned at data layer
     * @param createdBy   The source that created the appointment
     * @param updated     The date and time the appointment was last updated at, <em>should</em> be assigned at data
     *                    layer
     * @param updatedBy   The source that updated the appointment
     */
    public Appointment(int id, String title, String description, String location, String type, ZonedDateTime start,
                       ZonedDateTime end, Customer customer, User user, Contact contact, ZonedDateTime created,
                       String createdBy, ZonedDateTime updated, String updatedBy) {
        super(id, created, createdBy, updated, updatedBy);
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customer = customer;
        this.user = user;
        this.contact = contact;
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
     * @param customer    Customer associated with the appointment
     * @param user        User associated with the appointment
     * @param contact     Contact associated with the appointment
     */
    public Appointment(String title, String description, String location, String type, ZonedDateTime start,
                       ZonedDateTime end, Customer customer, User user, Contact contact) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customer = customer;
        this.user = user;
        this.contact = contact;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
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
                ", customer=" + customer +
                ", user=" + user +
                ", contact=" + contact +
                ", id=" + id +
                ", created=" + created +
                ", createdBy='" + createdBy + '\'' +
                ", updated=" + updated +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
