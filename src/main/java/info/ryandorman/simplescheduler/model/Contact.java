package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

/**
 * Holds Contact data.
 */
public class Contact extends Base {
    private String name;
    private String email;

    /**
     * Accepts no parameters to allow for Contact creation and dynamic property assignment post-creation.
     */
    public Contact() {
    }

    /**
     * Accepts all parameters to allow for population of existing Contact data.
     *
     * @param id    Unique identifier
     * @param name  Name used to identify Contact
     * @param email Email address for Contact
     */
    public Contact(int id, String name, String email) {
        super(id, null, null, null, null);
        this.name = name;
        this.email = email;
    }

    /**
     * Gets name used to identify Contact.
     *
     * @return Name used to identify Contact
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name used to identify Contact.
     *
     * @param name Name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets email address for Contact.
     *
     * @return Email address for Contact
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email address for Contact.
     *
     * @param email Email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Provides meaningful string representation of Country properties and their values.
     *
     * @return String representation of Country properties
     */
    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", created=" + created +
                ", createdBy='" + createdBy + '\'' +
                ", updated=" + updated +
                ", updatedBy='" + updatedBy + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
