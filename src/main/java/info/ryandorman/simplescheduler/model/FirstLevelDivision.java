package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import java.time.ZonedDateTime;

public class FirstLevelDivision extends Base {
    private String name;
    private long countryId;

    public FirstLevelDivision() {
    }

    public FirstLevelDivision(Long id, String name, long countryId, ZonedDateTime created, String createdBy,
                              ZonedDateTime updated, String updatedBy) {
        super(id, created, createdBy, updated, updatedBy);
        this.name = name;
        this.countryId = countryId;
    }

    public FirstLevelDivision(String name, long countryId) {
        this.name = name;
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCountryId() {
        return countryId;
    }

    public void setCountryId(long countryId) {
        this.countryId = countryId;
    }
}
