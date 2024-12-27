package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.model.Country;

import java.util.List;

/**
 * Allows predictable access to persistent Country data.
 */
public interface CountryDao {
    /**
     * Gets a <code>java.util.List</code> of all Countries.
     *
     * @return All Countries
     */
    List<Country> getAll();
}
