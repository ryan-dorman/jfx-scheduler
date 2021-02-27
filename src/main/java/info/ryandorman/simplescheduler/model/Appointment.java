package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import java.time.ZonedDateTime;

/**
 * Holds Appointment data.
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
     * Accepts no parameters to allow for Contact creation and dynamic property assignment post-creation.
     */
    public Appointment() {
    }

    /**
     * Accepts all parameters to allow for population of existing Contact data.
     *
     * @param id          Unique identifier
     * @param title       Title of Appointment
     * @param description Description of Appointment
     * @param location    Location of Appointment
     * @param type        Type of Appointment
     * @param start       Start date and time of Appointment
     * @param end         End date and time of Appointment
     * @param customer    Customer associated with Appointment
     * @param user        User associated with Appointment
     * @param contact     Contact associated with Appointment
     * @param created     Date and time Appointment was created
     * @param createdBy   Source that created Appointment
     * @param updated     Date and time Appointment was last updated
     * @param updatedBy   Source that updated Appointment
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
     * Gets title of Appointment.
     *
     * @return Title of Appointment
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title of Appointment.
     *
     * @param title Title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets description of Appointment.
     *
     * @return Description of Appointment
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description of Appointment.
     *
     * @param description Description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets location of Appointment.
     *
     * @return Location of Appointment
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets location of Appointment.
     *
     * @param location Location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets type of Appointment.
     *
     * @return Type of Appointment
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type of Appointment.
     *
     * @param type Type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets start date and time of Appointment.
     *
     * @return Start date and time of Appointment
     */
    public ZonedDateTime getStart() {
        return start;
    }

    /**
     * Sets start date and time of Appointment.
     *
     * @param start Start date and time to set
     */
    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    /**
     * Gets end date and time of Appointment.
     *
     * @return End date and time of Appointment
     */
    public ZonedDateTime getEnd() {
        return end;
    }

    /**
     * Sets end date and time of Appointment.
     *
     * @param end End date and time to set
     */
    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    /**
     * Gets customer associated with Appointment.
     *
     * @return Customer associated with Appointment
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets customer associated with Appointment.
     *
     * @param customer Customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Gets user associated with Appointment.
     *
     * @return User associated with Appointment
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user associated with Appointment
     *
     * @param user User to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets contact associated with Appointment.
     *
     * @return Contact associated with Appointment
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Sets contact associated with Appointment.
     *
     * @param contact Contact to set
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * Provides meaningful string representation of Appointment properties and their values.
     *
     * @return String representation of Appointment properties
     */
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
