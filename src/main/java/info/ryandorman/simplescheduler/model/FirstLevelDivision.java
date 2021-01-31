package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

public class FirstLevelDivision {
    private long id;
    private String name;
    private long countryId;

    public FirstLevelDivision() {
    }

    public FirstLevelDivision(long id, String name, long countryId) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
