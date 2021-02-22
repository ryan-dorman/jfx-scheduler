package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import java.time.ZonedDateTime;

/**
 * Holds Country data.
 */
public class Country extends Base {
    private String name;

    /**
     * Accepts no parameters to allow for Country creation and dynamic property assignment post-creation.
     */
    public Country() {
    }

    /**
     * Accepts all parameters to allow for population of existing Country data.
     *
     * @param id        Unique identifier
     * @param name      Name used to identify Country
     * @param created   Date and time Country was created
     * @param createdBy Source that created Country
     * @param updated   Date and time Country was last updated
     * @param updatedBy Source that updated Country
     */
    public Country(int id, String name, ZonedDateTime created, String createdBy, ZonedDateTime updated,
                   String updatedBy) {
        super(id, created, createdBy, updated, updatedBy);
        this.name = name;
    }

    /**
     * Gets name used to identify Country.
     *
     * @return Name used to identify Country
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name used to identify Country.
     *
     * @param name Name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Provides meaningful string representation of Country properties and their values.
     *
     * @return String representation of Country properties
     */
    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", created=" + created +
                ", createdBy='" + createdBy + '\'' +
                ", updated=" + updated +
                ", updatedBy='" + updatedBy + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
