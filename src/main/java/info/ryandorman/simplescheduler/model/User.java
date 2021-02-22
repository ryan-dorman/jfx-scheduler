package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import java.time.ZonedDateTime;

/**
 * Data model to hold User data.
 */
public class User extends Base {
    private String name;
    private String password;

    /**
     * Accepts no parameters to allow for User creation and dynamic property assignment post-creation.
     */
    public User() {
    }

    /**
     * Accepts all parameters to allow for population of existing User data.
     *
     * @param id Unique identifier
     * @param name Name used to identify User
     * @param password Character combination used to authenticate User
     * @param created Date and time User was created
     * @param createdBy Source that created the User
     * @param updated Date and time User was last updated
     * @param updatedBy Source that updated the User
     */
    public User(int id, String name, String password, ZonedDateTime created, String createdBy, ZonedDateTime updated,
                String updatedBy) {
        super(id, created, createdBy, updated, updatedBy);
        this.name = name;
        this.password = password;
    }

    /**
     * Gets name of User.
     *
     * @return Name of User
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of User.
     *
     * @param name Name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets character combination used to authenticate User.
     *
     * @return Character combination used to authenticate User
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets character combination used to authenticate User.
     *
     * @param password Character combination to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Provides useful string representation of User properties and their values.
     *
     * @return String representation of User properties
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", created=" + created +
                ", createdBy='" + createdBy + '\'' +
                ", updated=" + updated +
                ", updatedBy='" + updatedBy + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
