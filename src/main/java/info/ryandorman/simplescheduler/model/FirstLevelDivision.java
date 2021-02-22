package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import java.time.ZonedDateTime;

/**
 * Holds FirstLevelDivision data (i.e., Regions or States within Countries).
 */
public class FirstLevelDivision extends Base {
    private String name;
    private Country country;

    /**
     * Accepts no parameters to allow for User creation and dynamic property assignment post-creation.
     */
    public FirstLevelDivision() {
    }

    /**
     * Accepts all parameters to allow for population of existing FirstLevelDivision data.
     *
     * @param id        Unique identifier
     * @param name      Name used to identify the FirstLevelDivision
     * @param country   Country the FirstLevelDivision falls within
     * @param created   Date and time FirstLevelDivision was created
     * @param createdBy Source that created FirstLevelDivision
     * @param updated   Date and time FirstLevelDivision was last updated
     * @param updatedBy Source that updated FirstLevelDivision
     */
    public FirstLevelDivision(int id, String name, Country country, ZonedDateTime created, String createdBy,
                              ZonedDateTime updated, String updatedBy) {
        super(id, created, createdBy, updated, updatedBy);
        this.name = name;
        this.country = country;
    }

    /**
     * Gets name used to identify the FirstLevelDivision.
     *
     * @return Name used to identify the FirstLevelDivision
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name used to identify the FirstLevelDivision.
     *
     * @param name Name to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Gets Country the FirstLevelDivision falls within.
     *
     * @return Country the division falls within
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Sets Country the FirstLevelDivision falls within.
     *
     * @param country Country to set
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * Provides meaningful string representation of FirstLevelDivision properties and their values.
     *
     * @return String representation of FirstLevelDivision properties
     */
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
