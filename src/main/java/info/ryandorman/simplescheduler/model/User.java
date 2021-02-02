package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import java.time.ZonedDateTime;

public class User extends Base {
    private String name;
    private String password;

    public User() {
    }

    public User(Long id, String name, String password, ZonedDateTime created, String createdBy, ZonedDateTime updated,
                String updatedBy) {
        super(id, created, createdBy, updated, updatedBy);
        this.name = name;
        this.password = password;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
