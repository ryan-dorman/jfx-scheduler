package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import java.time.ZonedDateTime;

public class Country extends Base {
    private String name;

    public Country() {
    }

    public Country(Long id, String name, ZonedDateTime created, String createdBy, ZonedDateTime updated,
                   String updatedBy) {
        super(id, created, createdBy, updated, updatedBy);
        this.name = name;
    }

    public Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
