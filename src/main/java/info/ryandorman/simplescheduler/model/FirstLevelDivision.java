package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import java.time.ZonedDateTime;

public class FirstLevelDivision extends Base {
    private String name;
    private int countryId;

    public FirstLevelDivision() {
    }

    public FirstLevelDivision(int id, String name, int countryId, ZonedDateTime created, String createdBy,
                              ZonedDateTime updated, String updatedBy) {
        super(id, created, createdBy, updated, updatedBy);
        this.name = name;
        this.countryId = countryId;
    }

    public FirstLevelDivision(String name, int countryId) {
        this.name = name;
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return "FirstLevelDivision{" +
                "id=" + id +
                ", created=" + created +
                ", createdBy='" + createdBy + '\'' +
                ", updated=" + updated +
                ", updatedBy='" + updatedBy + '\'' +
                ", name='" + name + '\'' +
                ", countryId=" + countryId +
                '}';
    }
}
