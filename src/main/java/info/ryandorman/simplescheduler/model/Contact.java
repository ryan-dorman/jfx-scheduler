package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import java.time.ZonedDateTime;

public class Contact extends Base {
    private String name;
    private String email;

    public Contact() {
    }

    public Contact(Long id, String name, String email, ZonedDateTime created, String createdBy, ZonedDateTime updated,
                   String updatedBy) {
        super(id, created, createdBy, updated, updatedBy);
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
