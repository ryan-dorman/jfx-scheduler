package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import java.time.ZonedDateTime;

public class FirstLevelDivision extends Base {
    private String name;
    private Country country;

    public FirstLevelDivision() {
    }

    public FirstLevelDivision(int id, String name, Country country, ZonedDateTime created, String createdBy,
                              ZonedDateTime updated, String updatedBy) {
        super(id, created, createdBy, updated, updatedBy);
        this.name = name;
        this.country = country;
    }

    public FirstLevelDivision(String name, Country country) {
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "FirstLevelDivision{" +
                "id=" + id +
                ", created=" + created +
                ", createdBy='" + createdBy + '\'' +
                ", updated=" + updated +
                ", updatedBy='" + updatedBy + '\'' +
                ", country=" + country +
                '}';
    }
}
