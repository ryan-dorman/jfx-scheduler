package info.ryandorman.simplescheduler.dao;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import info.ryandorman.simplescheduler.model.FirstLevelDivision;

import java.util.List;

/**
 * Allows easy access to persistent FirstLevelDivision data.
 */
public interface FirstLevelDivisionDao {
    /**
     * Gets a <code>java.util.List</code> of FirstLevelDivisions associated with a unique Country identifier.
     * @param countryId Unique Country identifier
     * @return All FirstLevelDivisions associated with unique Country identifier
     */
    List<FirstLevelDivision> getByCountryId(int countryId);
}
