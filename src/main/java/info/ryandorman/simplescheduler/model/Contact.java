package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

public class Contact extends BaseEntity {
    private String name;
    private String email;

    public Contact() {
    }

    public Contact(Long id, String name, String email) {
        super(id);
        this.name = name;
        this.email = email;
    }

    public Contact(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
